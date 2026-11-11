package appliedbotanicalmachinery.machinery.blocks.tiles;

import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.ValueInput;

import appliedbotanicalmachinery.util.Recipes;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.machinery.blocks.base.RecipeTile;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityMechanicalManaPool extends RecipeTile<RecipeInput, ManaInfusionRecipe> {

    private static List<Item> CACHED_CATALYSTS;

    private final BaseItemStackHandler inventory;

    private int cooldown = 0;
    private boolean checkWithCatalyst = false;

    public BlockEntityMechanicalManaPool(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, BotaniaRecipeTypes.MANA_INFUSION_TYPE, pos, state, LibXServerConfig.MaxManaCapacity.mechanicalManaPool, 1, 2);
        this.inventory = BaseItemStackHandler.builder(3)
                .validator(stack -> this.getCatalysts().contains(stack.getItem()), 0)
                .validator(stack -> this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.MANA_INFUSION_TYPE, stack))
                .slotLimit(1, 0)
                .output(2)
                .contentsChanged(() -> {
                    this.setChanged();
                    this.setDispatchable();
                    this.needsRecipeUpdate();
                })
                .build();
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide()) {
            this.updateRecipeIfNeeded();
            if (this.cooldown > 0) {
                this.cooldown -= 1;
                this.setChanged();
                this.setDispatchable();
            }
            if (this.cooldown <= 0 && this.recipe != null) {
                this.craftRecipe();
            }
            this.tryPushToAdjacentInventory();
        } else if (this.level != null && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.industrialAgglomerationFactory) {
            double particleChance = (this.getCurrentMana() / (double) this.getMaxMana()) * 0.1D;
            if (Math.random() < particleChance) {
                float red = 0.0F;
                float green = 0.7764706F;
                float blue = 1.0F;
                WispParticleData data = WispParticleData.wisp((float) Math.random() / 3.0F, red, green, blue, 2.0F);
                this.level.addParticle(data, this.worldPosition.getX() + 0.3D + (this.level.getRandom().nextDouble() * 0.4), this.worldPosition.getY() + 0.5D + (this.level.getRandom().nextDouble() * 0.25D), this.worldPosition.getZ() + 0.3D + (this.level.getRandom().nextDouble() * 0.4), 0, this.level.getRandom().nextFloat() / 25, 0);
            }
        }
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IAdvancedItemHandlerModifiable> inventory) {
        return slot -> slot == 2;
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected void updateRecipe(BiConsumer<ItemStack, Integer> usedStacks) {
        this.recipe = null;
        this.checkWithCatalyst = true;
        super.updateRecipe(usedStacks);
        this.checkWithCatalyst = false;
        if (this.recipe == null) {
            super.updateRecipe(usedStacks);
        }
    }

    @Override
    protected boolean matchRecipe(ManaInfusionRecipe recipe, List<ItemStack> stacks) {
        if (recipe.getManaToConsume() > this.getCurrentMana()) return false;
        Item catalystItem = (this.checkWithCatalyst && !Inventories.get(this.inventory, 0).isEmpty()) ? Inventories.get(this.inventory, 0).getItem() : null;
        Block catalyst = catalystItem == null ? null : Block.byItem(catalystItem);
        if (catalyst == null && recipe.getRecipeCatalyst() != null) {
            return false;
        } else if (catalyst != null && (recipe.getRecipeCatalyst() == null || !recipe.getRecipeCatalyst().test(catalyst.defaultBlockState()))) {
            return false;
        }
        return super.matchRecipe(recipe, stacks);
    }

    @Override
    protected void onCrafted(ManaInfusionRecipe recipe) {
        this.cooldown = Math.max(1, LibXServerConfig.WorkingDurationMultiplier.mechanicalManaPool);
        this.receiveMana(-recipe.getManaToConsume());
    }

    @Override
    public int getComparatorOutput() {
        if (Inventories.get(this.inventory, 0).isEmpty()) return 0;
        Item item = Inventories.get(this.inventory, 0).getItem();
        if (this.getCatalysts().contains(item)) {
            return Mth.clamp(1 + this.getCatalysts().indexOf(item), 0, 15);
        } else {
            return 0;
        }
    }

    @Override
    public void receiveMana(int i) {
        if (Inventories.get(this.inventory, 0).getItem() == BotaniaBlocks.manaVoid.asItem()) {
            super.receiveMana(Math.min(i, this.getAvailableSpaceForMana()));
        } else {
            super.receiveMana(i);
        }

        if (this.recipe == null && !Inventories.get(this.inventory, 1).isEmpty()) {
            this.needsRecipeUpdate();
        }
    }

    @Override
    public boolean isFull() {
        return Inventories.get(this.inventory, 0).getItem() != BotaniaBlocks.manaVoid.asItem() && super.isFull();
    }

    @Override
    public int getAvailableSpaceForMana() {
        return Inventories.get(this.inventory, 0).getItem() == BotaniaBlocks.manaVoid.asItem() ? this.getMaxMana() : super.getAvailableSpaceForMana();
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        this.cooldown = nbt.getIntOr(TileTags.COOLDOWN, 0);
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(TileTags.COOLDOWN, this.cooldown);
    }



    public static void invalidateCatalysts() {
        CACHED_CATALYSTS = null;
    }

    public List<Item> getCatalysts() {
        if (CACHED_CATALYSTS == null) {
            if (this.level == null) {
                return List.of();
            }

            List<Item> catalysts = new ArrayList<>();
            Recipes.all(this.level, BotaniaRecipeTypes.MANA_INFUSION_TYPE).stream().map(RecipeHolder::value)
                    .forEach(recipe -> {
                        if (recipe.getRecipeCatalyst() != null) {
                            recipe.getRecipeCatalyst().getDisplayedStacks().stream().map(ItemStack::getItem).forEach(item -> {
                                if (!catalysts.contains(item)) {
                                    catalysts.add(item);
                                }
                            });
                        }
                    });
            CACHED_CATALYSTS = List.copyOf(catalysts);
        }

        return CACHED_CATALYSTS;
    }
}
