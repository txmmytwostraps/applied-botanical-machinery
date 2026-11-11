package appliedbotanicalmachinery.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import velrondevs.botania.api.recipe.BotaniaRecipe;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Recipe lookups for both sides. The server reads its recipe manager; the client reads the recipe types the
 * server synced (Botania Revived syncs its own types and keeps them in {@link BotaniaRecipeTypes}).
 */
public final class Recipes {

    private Recipes() {
    }

    public static <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> all(@Nullable Level level, RecipeType<T> type) {
        return BotaniaRecipeTypes.recipeMap(level).byType(type);
    }

    /**
     * The ingredients of a recipe. Botania recipes keep them in {@link BotaniaRecipe#getIngredients()} and report
     * no placement info, so {@code placementInfo()} is only used for other recipes.
     */
    public static List<Ingredient> ingredients(Recipe<?> recipe) {
        if (recipe instanceof BotaniaRecipe botaniaRecipe) {
            return botaniaRecipe.getIngredients();
        }
        return recipe.placementInfo().ingredients();
    }

    /**
     * Whether all ingredients of the recipe are in the stacks (LibX's RecipeHelper.matches, on {@link #ingredients}).
     *
     * @param exactMatch false if the stacks hold more items than the recipe needs
     */
    public static boolean matches(Recipe<?> recipe, List<ItemStack> stacks, boolean exactMatch) {
        return matches(ingredients(recipe), stacks, exactMatch);
    }

    public static boolean matches(List<Ingredient> ingredients, List<ItemStack> stacks, boolean exactMatch) {
        List<Integer> countsLeft = new ArrayList<>();
        for (ItemStack stack : stacks) {
            countsLeft.add(stack.isEmpty() ? 0 : stack.getCount());
        }
        ingredientLoop:
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < stacks.size(); i++) {
                if (countsLeft.get(i) > 0 && ingredient.test(stacks.get(i))) {
                    countsLeft.set(i, countsLeft.get(i) - 1);
                    continue ingredientLoop;
                }
            }
            return false;
        }
        return !exactMatch || countsLeft.stream().noneMatch(count -> count > 0);
    }

    /**
     * Whether the stack is an ingredient of any recipe of the type (LibX's RecipeHelper.isItemValidInput).
     */
    public static <I extends RecipeInput, T extends Recipe<I>> boolean isItemValidInput(@Nullable Level level, RecipeType<T> type, ItemStack stack) {
        for (RecipeHolder<T> holder : all(level, type)) {
            for (Ingredient ingredient : ingredients(holder.value())) {
                if (ingredient.test(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The result of a Botania recipe ({@code getResultItem(registries)}); empty for other recipes.
     */
    public static ItemStack result(Recipe<?> recipe, HolderLookup.Provider registries) {
        if (recipe instanceof BotaniaRecipe botaniaRecipe) {
            return botaniaRecipe.getResultItem(registries);
        }
        return ItemStack.EMPTY;
    }
}
