package appliedbotanicalmachinery.machinery.blocks.tiles;

import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;

import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

import net.neoforged.neoforge.transfer.fluid.FluidResource;

import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import net.neoforged.neoforge.transfer.item.ItemResource;

import net.neoforged.neoforge.transfer.ResourceHandler;

import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.ValueInput;

import appliedbotanicalmachinery.util.Recipes;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;
import org.moddingx.libx.inventory.FilterItemHandler;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.api.recipe.PureDaisyRecipe;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityMechanicalDaisy extends BlockEntityBase implements TickingBlock {

    private int ticksToNextUpdate = 5;
    // Negative value = recipe completed
    private int[] workingTicks = new int[8];
    private final InventoryHandler inventory = new InventoryHandler();

    // The canExtract function makes it so that hoppers can only extract items when the recipe is done.
    private final ResourceHandler<ItemResource> hopperInventory = new FilterItemHandler(this.inventory, slot -> this.workingTicks[slot] < 0, null);

    public BlockEntityMechanicalDaisy(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        nbt.child(TileTags.INVENTORY).ifPresent(input -> this.inventory.deserialize(Inventories.upgrade(input)));
        this.workingTicks = nbt.getIntArray(TileTags.WORKING_TICKS).filter(ticks -> ticks.length == 8).orElse(this.workingTicks);
    }

    @Override
    public void tick() {
        boolean hasSpawnedParticles = false;
        for (int i = 0; i < 8; i++) {
            PureDaisyRecipe recipe = this.getRecipe(i);
            if (recipe != null && this.workingTicks[i] >= 0) {
                //noinspection ConstantConditions
                if (!this.level.isClientSide()) {
                    if (this.workingTicks[i] >= recipe.getTime() * LibXServerConfig.WorkingDurationMultiplier.mechanicalDaisy) {
                        BlockState state = recipe.getOutputState();
                        if (state.getBlock().asItem() != Items.AIR) {
                            //noinspection deprecation
                            Inventories.set(this.inventory, i, state.getCloneItemStack(this.level, this.worldPosition, false));
                        } else if (state.getFluidState().getType() != Fluids.EMPTY) {
                            this.inventory.setFluid(i, new FluidStack(state.getFluidState().getType(), 1000));
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
                if (this.workingTicks[i] < 0 && !Inventories.get(this.inventory, i).isEmpty()) {
                    this.workingTicks[i] = -1;
                } else {
                    this.workingTicks[i] = 0;
                }
            }
        }
        //noinspection ConstantConditions
        if (!this.level.isClientSide()) {
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

        ItemStack stack = Inventories.get(this.inventory, slot);
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

        for (RecipeHolder<PureDaisyRecipe> holder : Recipes.all(this.level, BotaniaRecipeTypes.PURE_DAISY_TYPE)) {
            if (holder.value().matches(this.level, this.worldPosition, null, state)) {
                return holder.value();
            }
        }
        return null;
    }

    public InventoryHandler getInventory() {
        return this.inventory;
    }

    public ResourceHandler<ItemResource> getItemHandler(@Nullable Direction side) {
        // If the side is null (e.g we're in the gui) we return the normal inventory.
        // For world interactions (direction != null) we return the inventory that block slots of not finished recipes.
        return side == null ? this.inventory : this.hopperInventory;
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        this.inventory.serialize(nbt.child(TileTags.INVENTORY));
        nbt.putIntArray(TileTags.WORKING_TICKS, this.workingTicks);
    }



    /**
     * Eight slots that each hold either one block item or up to 1000 mB of a fluid.
     */
    public class InventoryHandler extends ItemStacksResourceHandler implements IAdvancedItemHandlerModifiable {

        private final Fluids fluids = new Fluids();

        public InventoryHandler() {
            super(8);
        }

        /**
         * The fluid side of the slots, exposed as fluid capability.
         */
        public ResourceHandler<FluidResource> getFluidHandler() {
            return this.fluids;
        }

        @Override
        public void set(int slot, @Nonnull ItemResource resource, int amount) {
            if (!resource.isEmpty()) {
                this.fluids.set(slot, FluidResource.EMPTY, 0);
            }
            super.set(slot, resource, amount);
        }

        public void setFluid(int slot, @Nonnull FluidStack stack) {
            this.fluids.set(slot, stack.isEmpty() ? FluidResource.EMPTY : FluidResource.of(stack), stack.isEmpty() ? 0 : stack.getAmount());
            if (!stack.isEmpty()) {
                super.set(slot, ItemResource.EMPTY, 0);
            }
        }

        @Override
        public int insert(int slot, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext transaction) {
            // A slot holding a fluid takes no items.
            return this.fluids.getResource(slot).isEmpty() ? super.insert(slot, resource, amount, transaction) : 0;
        }

        @Override
        public int extract(int slot, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext transaction) {
            return this.fluids.getResource(slot).isEmpty() ? super.extract(slot, resource, amount, transaction) : 0;
        }

        @Override
        protected int getCapacity(int slot, @Nonnull ItemResource resource) {
            return 1;
        }

        @Override
        public boolean isValid(int slot, @Nonnull ItemResource resource) {
            return !resource.isEmpty() && resource.getItem() instanceof BlockItem blockItem && BlockEntityMechanicalDaisy.this.getRecipe(blockItem.getBlock().defaultBlockState()) != null;
        }

        @Nonnull
        public FluidStack getFluidInTank(int tank) {
            if (!this.getResource(tank).isEmpty()) {
                // An item is in this slot
                return FluidStack.EMPTY;
            }
            FluidResource resource = this.fluids.getResource(tank);
            return resource.isEmpty() ? FluidStack.EMPTY : resource.toStack(this.fluids.getAmountAsInt(tank));
        }

        public int getTankCapacity(int tank) {
            return 1000;
        }

        @Override
        public void serialize(@Nonnull ValueOutput output) {
            super.serialize(output);
            this.fluids.serialize(output.child("fluids"));
        }

        @Override
        public void deserialize(@Nonnull ValueInput input) {
            super.deserialize(input);
            this.fluids.deserialize(input.childOrEmpty("fluids"));
        }

        @Override
        protected void onContentsChanged(int slot, @Nonnull ItemStack previousContents) {
            this.changed(slot);
        }

        private void changed(int slot) {
            if (slot >= 0 && slot < 8 && this.getResource(slot).isEmpty() && this.fluids.getResource(slot).isEmpty()) {
                BlockEntityMechanicalDaisy.this.workingTicks[slot] = 0;
            }
            BlockEntityMechanicalDaisy.this.setChanged();
            BlockEntityMechanicalDaisy.this.setDispatchable();
        }

        private class Fluids extends FluidStacksResourceHandler {

            Fluids() {
                super(8, 1000);
            }

            @Override
            public boolean isValid(int tank, @Nonnull FluidResource resource) {
                return !resource.isEmpty() && BlockEntityMechanicalDaisy.this.getRecipe(resource.getFluid().defaultFluidState().createLegacyBlock()) != null;
            }

            @Override
            public int insert(int tank, @Nonnull FluidResource resource, int amount, @Nonnull TransactionContext transaction) {
                // A slot holding an item takes no fluid.
                return InventoryHandler.this.getResource(tank).isEmpty() ? super.insert(tank, resource, amount, transaction) : 0;
            }

            @Override
            public int insert(@Nonnull FluidResource resource, int amount, @Nonnull TransactionContext transaction) {
                // Fill tanks that already hold this fluid first, then empty ones.
                int inserted = 0;
                for (int pass = 0; pass < 2 && inserted < amount; pass++) {
                    for (int tank = 0; tank < this.size() && inserted < amount; tank++) {
                        boolean same = resource.equals(this.getResource(tank));
                        if (pass == 0 ? same : this.getResource(tank).isEmpty()) {
                            inserted += this.insert(tank, resource, amount - inserted, transaction);
                        }
                    }
                }
                return inserted;
            }

            @Override
            protected void onContentsChanged(int tank, @Nonnull FluidStack previousContents) {
                InventoryHandler.this.changed(tank);
            }
        }
    }
}
