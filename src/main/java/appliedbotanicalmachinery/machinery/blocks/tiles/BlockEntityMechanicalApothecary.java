package appliedbotanicalmachinery.machinery.blocks.tiles;

import com.google.common.collect.Range;
import appliedbotanicalmachinery.machinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.moddingx.libx.base.tile.TickingBlock;
import org.moddingx.libx.crafting.RecipeHelper;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.CustomApothecaryColor;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.registry.BotaniaSounds;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityMechanicalApothecary extends WorkingTile<RecipeInput, PetalApothecaryRecipe> implements TickingBlock {

    public static final int WORKING_DURATION = 20;
    public static final int FLUID_CAPACITY = 8000;

    private final BaseItemStackHandler inventory;

    private final ApothecaryFluidTank fluidInventory = new ApothecaryFluidTank(FLUID_CAPACITY, fluidStack -> Fluids.WATER.isSame(fluidStack.getFluid()));

    private ItemStack currentOutput = ItemStack.EMPTY;

    public BlockEntityMechanicalApothecary(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, BotaniaRecipeTypes.PETAL_TYPE, pos, state, 0, 1, 17);
        this.inventory = BaseItemStackHandler.builder(21)
                .validator(stack -> stack.is(BotaniaTags.Items.SEED_APOTHECARY_REAGENT), 0)
                .validator(stack -> this.level != null && RecipeHelper.isItemValidInput(this.level.getRecipeManager(), BotaniaRecipeTypes.PETAL_TYPE, stack), Range.closedOpen(1, 17))
                .output(17, 18, 19, 20)
                .contentsChanged(() -> {
                    this.setChanged();
                    this.setDispatchable();
                    this.needsRecipeUpdate();
                })
                .build();
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            this.runRecipeTick();
            if (this.recipe != null) {
                this.currentOutput = this.recipe.getResultItem(this.level.registryAccess()).copy();
                this.setChanged();
                this.setDispatchable();
            } else if (!this.currentOutput.isEmpty()) {
                this.currentOutput = ItemStack.EMPTY;
                this.setChanged();
                this.setDispatchable();
            }
        } else if (this.level != null && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.mechanicalApothecary) {
            if (this.fluidInventory.getFluidAmount() > 0) {
                if (this.getMaxProgress() > 0 && this.getProgress() > this.getMaxProgress() - 5) {
                    for (int i = 0; i < 5; i++) {
                        SparkleParticleData data = SparkleParticleData.sparkle(this.level.random.nextFloat(), this.level.random.nextFloat(), this.level.random.nextFloat(), this.level.random.nextFloat(), 10);
                        this.level.addParticle(data, this.worldPosition.getX() + 0.3 + (this.level.random.nextDouble() * 0.4), this.worldPosition.getY() + 0.6, this.worldPosition.getZ() + 0.3 + (this.level.random.nextDouble() * 0.4), 0.0D, 0.0D, 0.0D);
                    }
                    this.level.playLocalSound(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, BotaniaSounds.altarCraft, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                } else {
                    for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
                        ItemStack stack = this.inventory.getStackInSlot(slot);
                        if (stack.isEmpty()) {
                            continue;
                        }

                        if (this.level.random.nextFloat() >= 0.97f) {
                            int color = stack.getItem() instanceof CustomApothecaryColor item ? item.getParticleColor(stack) : 0x888888;
                            float red = (float) (color >> 16 & 255) / 255f;
                            float green = (float) (color >> 8 & 255) / 255f;
                            float blue = (float) (color & 255) / 255f;
                            if (Math.random() >= 0.75) {
                                this.level.playSound(null, this.worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 10.0F);
                            }
                            SparkleParticleData data = SparkleParticleData.sparkle(this.level.random.nextFloat(), red, green, blue, 10);
                            this.level.addParticle(data, this.worldPosition.getX() + 0.3 + (this.level.random.nextDouble() * 0.4), this.worldPosition.getY() + 0.6, this.worldPosition.getZ() + 0.3 + (this.level.random.nextDouble() * 0.4), 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory) {
        return slot -> slot >= 17 && slot <= 20;
    }

    @Nonnull
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Nonnull
    public FluidTank getFluidInventory() {
        return this.fluidInventory;
    }

    @Override
    public boolean actAsMana() {
        return false;
    }

    @Override
    protected int getAndApplyProgressThisTick() {
        return 1;
    }

    @Override
    protected boolean canMatchRecipes() {
        if (this.inventory.getStackInSlot(0).isEmpty()) return false;
        FluidStack fluid = this.getFluidInventory().getFluid();

        return !fluid.isEmpty() && fluid.getFluid() == Fluids.WATER && fluid.getAmount() >= FluidType.BUCKET_VOLUME;
    }

    @Override
    protected void onCrafted(PetalApothecaryRecipe recipe) {
        this.inventory.extractItem(0, 1, false);
        FluidStack fluid = this.getFluidInventory().getFluid().copy();
        if (fluid.getFluid() != Fluids.WATER) return;
        int newAmount = Math.max(0, fluid.getAmount() - FluidType.BUCKET_VOLUME);
        fluid.setAmount(newAmount);
        this.fluidInventory.setFluid(fluid);
    }

    @Override
    protected int getMaxProgress(PetalApothecaryRecipe recipe) {
        return WORKING_DURATION * LibXServerConfig.WorkingDurationMultiplier.mechanicalApothecary;
    }

    @Override
    public int getMaxManaPerTick() {
        return 1;
    }

    public ItemStack getCurrentOutput() {
        return this.currentOutput;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.fluidInventory.setFluid(FluidStack.parseOptional(registries, nbt.getCompound(TileTags.FLUID)));
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(TileTags.FLUID, this.getFluidInventory().getFluid().saveOptional(registries));
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.handleUpdateTag(nbt, registries);
        if (this.level != null && !this.level.isClientSide) return;
        this.fluidInventory.setFluid(FluidStack.parseOptional(registries, nbt.getCompound(TileTags.FLUID)));
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.put(TileTags.FLUID, this.getFluidInventory().getFluid().saveOptional(registries));
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
        return nbt;
    }

    private class ApothecaryFluidTank extends FluidTank {

        public ApothecaryFluidTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            BlockEntityMechanicalApothecary.this.setChanged();
            BlockEntityMechanicalApothecary.this.setDispatchable();
            BlockEntityMechanicalApothecary.this.needsRecipeUpdate();
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    }
}
