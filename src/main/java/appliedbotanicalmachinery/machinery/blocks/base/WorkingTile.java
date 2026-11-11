package appliedbotanicalmachinery.machinery.blocks.base;

import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.ValueInput;

import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public abstract class WorkingTile<I extends RecipeInput, T extends Recipe<I>> extends RecipeTile<I, T> {

    private int progress;
    private int maxProgress;
    
    public WorkingTile(BlockEntityType<?> blockEntityType, RecipeType<T> recipeType, BlockPos pos, BlockState state, int manaCap, int firstInputSlot, int firstOutputSlot) {
        super(blockEntityType, recipeType, pos, state, manaCap, firstInputSlot, firstOutputSlot);
        this.progress = 0;
        this.maxProgress = 0;
    }

    protected void runRecipeTick() {
        this.runRecipeTick(() -> {}, (stack, slot) -> {}, (stack, slot) -> {});
    }

    protected void runRecipeTick(Runnable doUpdate, BiConsumer<ItemStack, Integer> updateStack, BiConsumer<ItemStack, Integer> consumeStack) {
        if (this.level != null && !this.level.isClientSide()) {
            this.updateRecipeIfNeeded(doUpdate, updateStack);
            if (this.recipe != null) {
                int newMaxProgress = this.getMaxProgress(this.recipe);
                if (newMaxProgress != this.maxProgress) {
                    this.maxProgress = newMaxProgress;
                    this.setChanged();
                    this.setDispatchable();
                }
                this.progress += this.getAndApplyProgressThisTick();
                if (this.progress >= this.getMaxProgress(this.recipe)) {
                    this.progress = 0;
                    this.craftRecipe(consumeStack);
                }
                this.setChanged();
                this.setDispatchable();
            } else if (this.progress != 0 || this.maxProgress != 0) {
                this.progress = 0;
                this.maxProgress = 0;
                this.setChanged();
                this.setDispatchable();
            }

            this.tryPushToAdjacentInventory();
        }
    }

    public int getProgress() {
        return this.progress;
    }
    
    public int getMaxProgress() {
        return this.maxProgress;
    }
    
    protected int getAndApplyProgressThisTick() {
        int manaToTransfer = Math.min(Math.min(this.getCurrentMana(), this.getMaxManaPerTick()), this.getMaxProgress(this.recipe) - this.progress);
        this.receiveMana(-manaToTransfer);
        return manaToTransfer;
    }

    protected abstract int getMaxProgress(T recipe);

    public abstract int getMaxManaPerTick();

    @Override
    public int getComparatorOutput() {
        return this.getProgress() > 0 ? 15 : 0;
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        this.progress = nbt.getIntOr(TileTags.PROGRESS, 0);
        this.maxProgress = nbt.getIntOr(TileTags.MAX_PROGRESS, 0);
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(TileTags.PROGRESS, this.progress);
        nbt.putInt(TileTags.MAX_PROGRESS, this.maxProgress);
    }


}
