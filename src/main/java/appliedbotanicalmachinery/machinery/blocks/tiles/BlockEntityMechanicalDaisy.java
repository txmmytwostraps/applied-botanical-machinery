package appliedbotanicalmachinery.machinery.blocks.tiles;

import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;
import org.moddingx.libx.inventory.FilterItemHandler;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.api.recipe.PureDaisyRecipe;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityMechanicalDaisy extends BlockEntityBase implements TickingBlock {

    private int ticksToNextUpdate = 5;
    // Negative value = recipe completed
    private int[] workingTicks = new int[8];
    private final InventoryHandler inventory = new InventoryHandler();

    // The canExtract function makes it so that hoppers can only extract items when the recipe is done.
    private final IItemHandler hopperInventory = new FilterItemHandler(this.inventory, slot -> this.workingTicks[slot] < 0, null);

    public BlockEntityMechanicalDaisy(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        if (nbt.contains(TileTags.INVENTORY)) {
            this.inventory.deserializeNBT(registries, nbt.getCompound(TileTags.INVENTORY));
        }
        if (nbt.contains(TileTags.WORKING_TICKS)) {
            this.workingTicks = nbt.getIntArray(TileTags.WORKING_TICKS);
        }
    }

    @Override
    public void tick() {
        boolean hasSpawnedParticles = false;
        for (int i = 0; i < 8; i++) {
            PureDaisyRecipe recipe = this.getRecipe(i);
            if (recipe != null && this.workingTicks[i] >= 0) {
                //noinspection ConstantConditions
                if (!this.level.isClientSide) {
                    if (this.workingTicks[i] >= recipe.getTime() * LibXServerConfig.WorkingDurationMultiplier.mechanicalDaisy) {
                        BlockState state = recipe.getOutputState();
                        if (state.getBlock().asItem() != Items.AIR) {
                            //noinspection deprecation
                            this.inventory.setStackInSlot(i, state.getBlock().getCloneItemStack(this.level, this.worldPosition, state));
                        } else if (state.getFluidState().getType() != Fluids.EMPTY) {
                            this.inventory.setStackInSlot(i, new FluidStack(state.getFluidState().getType(), 1000));
                        }
                        this.workingTicks[i] = -1;
                        this.setDispatchable();
                    } else {
                        this.workingTicks[i] += 1;
                    }
                } else if (!hasSpawnedParticles && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.mechanicalDaisy) {
                    hasSpawnedParticles = true;
                    double x = this.worldPosition.getX() + Math.random();
                    double y = this.worldPosition.getY() + Math.random() + 0.25D;
                    double z = this.worldPosition.getZ() + Math.random();
                    WispParticleData data = WispParticleData.wisp((float) Math.random() / 2.0F, 1.0F, 1.0F, 1.0F);
                    this.level.addParticle(data, x, y, z, 0.0D, 0.0D, 0.0D);
                }
            } else {
                if (this.workingTicks[i] < 0 && !this.inventory.getStackInSlot(i).isEmpty()) {
                    this.workingTicks[i] = -1;
                } else {
                    this.workingTicks[i] = 0;
                }
            }
        }
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            if (this.ticksToNextUpdate <= 0) {
                this.ticksToNextUpdate = 5;
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
            } else {
                this.ticksToNextUpdate -= 1;
            }
        }
    }

    @Nullable
    private PureDaisyRecipe getRecipe(int slot) {
        BlockState state = this.getState(slot);
        if (state == null) {
            return null;
        }

        return this.getRecipe(state);
    }

    @Nullable
    public BlockState getState(int slot) {
        BlockState state = null;

        ItemStack stack = this.inventory.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof BlockItem) {
                state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            }
        } else {
            FluidStack fluid = this.inventory.getFluidInTank(slot);
            if (!fluid.isEmpty() && fluid.getAmount() >= 1000) {
                state = fluid.getFluid().defaultFluidState().createLegacyBlock();
            }
        }
        return state;
    }

    @Nullable
    public PureDaisyRecipe getRecipe(BlockState state) {
        if (this.level == null)
            return null;

        for (RecipeHolder<PureDaisyRecipe> holder : this.level.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.PURE_DAISY_TYPE)) {
            if (holder.value().matches(this.level, this.worldPosition, null, state)) {
                return holder.value();
            }
        }
        return null;
    }

    public InventoryHandler getInventory() {
        return this.inventory;
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        // If the side is null (e.g we're in the gui) we return the normal inventory.
        // For world interactions (direction != null) we return the inventory that block slots of not finished recipes.
        return side == null ? this.inventory : this.hopperInventory;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(TileTags.INVENTORY, this.inventory.serializeNBT(registries));
        nbt.putIntArray(TileTags.WORKING_TICKS, this.workingTicks);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        if (this.level != null && !this.level.isClientSide) return;
        super.handleUpdateTag(nbt, registries);
        if (nbt.contains(TileTags.INVENTORY)) {
            this.inventory.deserializeNBT(registries, nbt.getCompound(TileTags.INVENTORY));
        }
        if (nbt.contains(TileTags.WORKING_TICKS)) {
            this.workingTicks = nbt.getIntArray(TileTags.WORKING_TICKS);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.put(TileTags.INVENTORY, this.inventory.serializeNBT(registries));
        nbt.putIntArray(TileTags.WORKING_TICKS, this.workingTicks);
        return nbt;
    }

    public class InventoryHandler extends ItemStackHandler implements IFluidHandler {

        private final List<FluidStack> fluids = new ArrayList<>(8);

        public InventoryHandler() {
            super(8);
            for (int i = 0; i < 8; i++) {
                this.fluids.add(FluidStack.EMPTY);
            }
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if (!stack.isEmpty())
                this.fluids.set(slot, FluidStack.EMPTY);
            super.setStackInSlot(slot, stack);
        }

        public void setStackInSlot(int slot, @Nonnull FluidStack stack) {
            this.fluids.set(slot, stack);
            if (!stack.isEmpty())
                super.setStackInSlot(slot, ItemStack.EMPTY);
            else
                this.onContentsChanged(slot); // setStackInSlot calls this as well
        }

        @Override
        public int getSlots() {
            return 8;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!this.fluids.get(slot).isEmpty()) {
                // Slot is occupied by a fluid.
                return stack;
            } else {
                return super.insertItem(slot, stack, simulate);
            }
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!this.fluids.get(slot).isEmpty()) {
                // Slot is occupied by a fluid.
                return ItemStack.EMPTY;
            } else {
                return super.extractItem(slot, amount, simulate);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !stack.isEmpty() && stack.getItem() instanceof BlockItem && BlockEntityMechanicalDaisy.this.getRecipe(((BlockItem) stack.getItem()).getBlock().defaultBlockState()) != null;
        }

        @Override
        public int getTanks() {
            return 8;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            if (!this.getStackInSlot(tank).isEmpty()) {
                // There's an item in here
                return FluidStack.EMPTY;
            } else {
                return this.fluids.get(tank);
            }
        }

        @Override
        public int getTankCapacity(int tank) {
            return 1000;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return !stack.isEmpty() && BlockEntityMechanicalDaisy.this.getRecipe(stack.getFluid().defaultFluidState().createLegacyBlock()) != null;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int leftToFill = resource.getAmount();

            // Try to deposit the fluid to slots already filled with it
            for (int i = 0; i < 8; i++) {
                if (leftToFill <= 0)
                    break;
                if (!this.getStackInSlot(i).isEmpty())
                    continue;
                if (this.fluids.get(i).getFluid() == resource.getFluid()) {
                    int transfer = Math.min(leftToFill, this.getTankCapacity(i) - this.fluids.get(i).getAmount());
                    leftToFill -= transfer;
                    if (action == FluidAction.EXECUTE) {
                        this.fluids.get(i).setAmount(this.fluids.get(i).getAmount() + transfer);
                        this.onContentsChanged(i);
                    }
                }
            }

            // If that did not work we use an empty slot
            for (int i = 0; i < 8; i++) {
                if (leftToFill <= 0)
                    break;
                if (!this.getStackInSlot(i).isEmpty())
                    continue;
                if (this.fluids.get(i).isEmpty()) {
                    int transfer = Math.min(leftToFill, this.getTankCapacity(i));
                    leftToFill -= transfer;
                    if (action == FluidAction.EXECUTE) {
                        this.fluids.set(i, new FluidStack(resource.getFluid(), transfer));
                        this.onContentsChanged(i);
                    }
                }
            }
            return resource.getAmount() - leftToFill;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            int leftToDrain = resource.getAmount();

            for (int i = 0; i < 8; i++) {
                if (leftToDrain <= 0)
                    break;
                if (!this.getStackInSlot(i).isEmpty())
                    continue;
                if (this.fluids.get(i).getFluid() == resource.getFluid()) {
                    int transfer = Math.min(this.fluids.get(i).getAmount(), leftToDrain);
                    leftToDrain -= transfer;
                    if (action == FluidAction.EXECUTE) {
                        this.fluids.get(i).setAmount(this.fluids.get(i).getAmount() - transfer);
                        if (this.fluids.get(i).getAmount() <= 0)
                            this.fluids.set(i, FluidStack.EMPTY);
                        this.onContentsChanged(i);
                    }
                }
            }

            if (resource.getAmount() - leftToDrain > 0) {
                return new FluidStack(resource.getFluid(), resource.getAmount() - leftToDrain);
            } else {
                return FluidStack.EMPTY;
            }
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            int leftToDrain = maxDrain;
            Fluid drainFluid = null;

            for (int i = 0; i < 8; i++) {
                if (leftToDrain <= 0)
                    break;
                if (!this.getStackInSlot(i).isEmpty())
                    continue;
                if (drainFluid == null || drainFluid == this.fluids.get(i).getFluid()) {
                    int transfer = Math.min(this.fluids.get(i).getAmount(), leftToDrain);
                    leftToDrain -= transfer;
                    if (transfer > 0)
                        drainFluid = this.fluids.get(i).getFluid();
                    if (action == FluidAction.EXECUTE) {
                        this.fluids.get(i).setAmount(this.fluids.get(i).getAmount() - transfer);
                        if (this.fluids.get(i).getAmount() <= 0)
                            this.fluids.set(i, FluidStack.EMPTY);
                        this.onContentsChanged(i);
                    }
                }
            }

            if (drainFluid == null) {
                return FluidStack.EMPTY;
            } else {
                return new FluidStack(drainFluid, maxDrain - leftToDrain);
            }
        }

        @Override
        public CompoundTag serializeNBT(@Nonnull HolderLookup.Provider registries) {
            CompoundTag nbt = super.serializeNBT(registries);
            ListTag tag = new ListTag();
            for (int i = 0; i < 8; i++) {
                tag.add(i, this.fluids.get(i).saveOptional(registries));
            }
            nbt.put("fluids", tag);
            return nbt;
        }

        @Override
        public void deserializeNBT(@Nonnull HolderLookup.Provider registries, @Nonnull CompoundTag nbt) {
            super.deserializeNBT(registries, nbt);
            if (nbt.contains("fluids")) {
                ListTag tag = nbt.getList("fluids", CompoundTag.TAG_COMPOUND);
                for (int i = 0; i < 8; i++) {
                    CompoundTag fluidNbt = tag.getCompound(i);
                    this.fluids.set(i, FluidStack.parseOptional(registries, fluidNbt));
                }
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (slot >= 0 && slot < 8 && this.getStackInSlot(slot).isEmpty() && this.getFluidInTank(slot).isEmpty()) {
                BlockEntityMechanicalDaisy.this.workingTicks[slot] = 0;
            }
            BlockEntityMechanicalDaisy.this.setChanged();
            BlockEntityMechanicalDaisy.this.setDispatchable();
        }
    }
}
