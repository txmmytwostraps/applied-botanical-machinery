package appliedbotanicalmachinery.machinery.blocks.base;

import appliedbotanicalmachinery.util.Recipes;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public abstract class RecipeTile<I extends RecipeInput, T extends Recipe<I>> extends BotanicalTile {

    private final RecipeType<T> recipeType;
    private final int firstInputSlot;
    private final int firstOutputSlot;

    protected T recipe;
    private boolean needsRecipeUpdate;

    public RecipeTile(BlockEntityType<?> blockEntityType, RecipeType<T> recipeType, BlockPos pos, BlockState state, int manaCap, int firstInputSlot, int firstOutputSlot) {
        super(blockEntityType, pos, state, manaCap);
        this.recipeType = recipeType;
        this.firstInputSlot = firstInputSlot;
        this.firstOutputSlot = firstOutputSlot;
        this.needsRecipeUpdate = true;
    }

    protected void updateRecipeIfNeeded() {
        this.updateRecipeIfNeeded(() -> {}, (stack, slot) -> {});
    }

    protected void updateRecipeIfNeeded(Runnable doUpdate, BiConsumer<ItemStack, Integer> usedStacks) {
        if (this.level == null || this.level.isClientSide()) return;
        if (this.needsRecipeUpdate) {
            this.needsRecipeUpdate = false;
            doUpdate.run();
            this.updateRecipe(usedStacks);
        }
    }

    protected void updateRecipe() {
        this.updateRecipe((stack, slot) -> {});
    }

    protected void updateRecipe(BiConsumer<ItemStack, Integer> usedStacks) {
        if (this.level == null || this.level.isClientSide()) return;
        if (!this.canMatchRecipes()) {
            this.recipe = null;
            return;
        }
        IAdvancedItemHandlerModifiable inventory = this.getInventory().getUnrestricted();
        List<ItemStack> stacks = IntStream.range(this.firstInputSlot, this.firstOutputSlot).mapToObj(slot -> Inventories.get(inventory, slot)).toList();
        for (RecipeHolder<T> holder : Recipes.all(this.level, this.recipeType)) {
            T recipe = holder.value();
            if (this.matchRecipe(recipe, stacks)) {
                List<ItemStack> consumedStacks = new ArrayList<>();
                for (Ingredient ingredient : this.ingredientsOf(recipe)) {
                    for (int stackIdx = 0; stackIdx < stacks.size(); stackIdx += 1) {
                        if (ingredient.test(stacks.get(stackIdx))) {
                            ItemStack theStack = stacks.get(stackIdx).copy();
                            theStack.setCount(1);
                            consumedStacks.add(theStack.copy());
                            usedStacks.accept(theStack, this.firstInputSlot + stackIdx);
                            break;
                        }
                    }
                }
                List<ItemStack> resultItems = this.resultItems(recipe, consumedStacks);
                if (resultItems.isEmpty() || inventory.hasSpaceFor(resultItems, this.firstOutputSlot, inventory.size())) {
                    this.recipe = recipe;
                } else {
                    this.recipe = null;
                }
                return;
            }
        }
        this.recipe = null;
    }

    protected void craftRecipe() {
        this.craftRecipe((stack, slot) -> {});
    }

    protected void craftRecipe(BiConsumer<ItemStack, Integer> usedStacks) {
        if (this.level == null || this.level.isClientSide()) return;
        if (this.recipe != null) {
            IAdvancedItemHandlerModifiable inventory = this.getInventory().getUnrestricted();
            List<ItemStack> consumedStacks = new ArrayList<>();
            for (Ingredient ingredient : this.ingredientsOf(this.recipe)) {
                for (int slot = this.firstInputSlot; slot < this.firstOutputSlot; slot++) {
                    if (ingredient.test(Inventories.get(inventory, slot))) {
                        ItemStack extracted = Inventories.extract(inventory, slot, 1, false);
                        if (!extracted.isEmpty()) {
                            consumedStacks.add(extracted.copy());
                            usedStacks.accept(extracted, slot);
                        }
                        break;
                    }
                }
            }
            for (ItemStack result : this.resultItems(this.recipe, consumedStacks)) {
                this.putIntoOutputOrDrop(result.copy());
            }
            this.onCrafted(this.recipe);
            this.recipe = null;
            this.needsRecipeUpdate();
        }
    }

    protected boolean canMatchRecipes() {
        return true;
    }

    // May not modify the stacks
    protected boolean matchRecipe(T recipe, List<ItemStack> stacks) {
        return Recipes.matches(this.ingredientsOf(recipe), stacks, false);
    }

    protected void onCrafted(T recipe) {

    }

    // May not modify the stacks
    protected List<ItemStack> resultItems(T recipe, List<ItemStack> stacks) {
        //noinspection DataFlowIssue
        ItemStack stack = this.resultOf(recipe);
        if (stack.isEmpty()) return List.of();
        return List.of(stack.copy());
    }

    protected void putIntoOutputOrDrop(ItemStack stack) {
        if (this.level == null || this.level.isClientSide()) return;
        IAdvancedItemHandlerModifiable inventory = this.getInventory().getUnrestricted();
        ItemStack left = stack.copy();
        for (int slot = this.firstOutputSlot; slot < inventory.size(); slot++) {
            left = Inventories.insert(inventory, slot, left, false);
            if (left.isEmpty()) return;
        }
        if (!left.isEmpty()) {
            ItemEntity ie = new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.7, this.worldPosition.getZ() + 0.5, left.copy());
            this.level.addFreshEntity(ie);
        }
    }

    protected void tryPushToAdjacentInventory() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        if (!this.hasItemsInOutputSlots()) {
            return;
        }

        List<IItemHandler> itemHandlers = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            BlockPos target = this.worldPosition.relative(dir);
            if (this.level.getBlockEntity(target) == null) {
                continue;
            }

            if (!LibXServerConfig.autoExportAllowed.test(BuiltInRegistries.BLOCK.getKey(this.level.getBlockState(target).getBlock()))) {
                continue;
            }

            IItemHandler itemHandler = this.level.getCapability(Capabilities.Item.BLOCK, target, dir.getOpposite());
            if (itemHandler == null) {
                continue;
            }

            itemHandlers.add(itemHandler);
        }

        IAdvancedItemHandlerModifiable inventory = this.getInventory().getUnrestricted();
        for (int i = this.firstOutputSlot; i < inventory.size(); i++) {
            ItemStack stack = Inventories.get(inventory, i);
            for (IItemHandler itemHandler : itemHandlers) {
                stack = Inventories.insert(itemHandler, stack, false);
                if (stack.isEmpty()) {
                    break;
                }
            }

            Inventories.set(inventory, i, stack);
        }
    }

    protected boolean hasItemsInOutputSlots() {
        IAdvancedItemHandlerModifiable inv = this.getInventory().getUnrestricted();
        int slots = inv.size();
        for (int i = this.firstOutputSlot; i < slots; i++) {
            if (!Inventories.get(inv, i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public void needsRecipeUpdate() {
        this.needsRecipeUpdate = true;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.needsRecipeUpdate = true;
    }

    /**
     * The ingredients of a recipe of this machine. Override for recipes that are not Botania recipes.
     */
    protected List<Ingredient> ingredientsOf(T recipe) {
        return Recipes.ingredients(recipe);
    }

    /**
     * The result of a recipe of this machine. Override for recipes that are not Botania recipes.
     */
    protected ItemStack resultOf(T recipe) {
        //noinspection DataFlowIssue
        return Recipes.result(recipe, this.level.registryAccess());
    }
}
