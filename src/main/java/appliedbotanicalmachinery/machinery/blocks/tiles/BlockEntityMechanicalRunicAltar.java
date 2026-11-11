package appliedbotanicalmachinery.machinery.blocks.tiles;

import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.ValueInput;

import appliedbotanicalmachinery.util.Recipes;

import appliedbotanicalmachinery.util.Inventories;

import com.google.common.collect.Range;
import com.google.common.collect.Streams;
import appliedbotanicalmachinery.machinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.common.lib.BotaniaTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityMechanicalRunicAltar extends WorkingTile<RecipeInput, RunicAltarRecipe> {

    public static final int MAX_MANA_PER_TICK = 100;

    private final BaseItemStackHandler inventory;

    private final List<Integer> slotsUsed = new ArrayList<>();

    public BlockEntityMechanicalRunicAltar(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, BotaniaRecipeTypes.RUNE_TYPE, pos, state, LibXServerConfig.MaxManaCapacity.mechanicalRunicAltar, 1, 17);
        this.inventory = BaseItemStackHandler.builder(33)
                .validator(stack -> stack.getItem() == BotaniaBlocks.livingrock.asItem(), 0)
                .validator(stack -> this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.RUNE_TYPE, stack), Range.closedOpen(1, 17))
                .output(Range.closedOpen(17, 33))
                .contentsChanged(() -> {
                    this.setChanged();
                    this.setDispatchable();
                    this.needsRecipeUpdate();
                })
                .build();
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IAdvancedItemHandlerModifiable> inventory) {
        return slot -> slot >= 17 && slot <= 33;
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide()) {
            this.runRecipeTick(this.slotsUsed::clear, (stack, slot) -> this.slotsUsed.add(slot), (stack, slot) -> {
            });
        } else if (this.level != null && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.industrialAgglomerationFactory) {
            if (this.getMaxProgress() > 0 && this.getProgress() >= (this.getMaxProgress() - (5 * this.getMaxManaPerTick()))) {
                for (int i = 0; i < 5; ++i) {
                    SparkleParticleData data = SparkleParticleData.sparkle(this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), 10);
                    this.level.addParticle(data, this.worldPosition.getX() + 0.3 + (this.level.getRandom().nextDouble() * 0.4), this.worldPosition.getY() + 0.7, this.worldPosition.getZ() + 0.3 + (this.level.getRandom().nextDouble() * 0.4), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    protected boolean canMatchRecipes() {
        return !Inventories.get(this.inventory, 0).isEmpty();
    }

    @Override
    protected List<ItemStack> resultItems(RunicAltarRecipe recipe, List<ItemStack> stacks) {
        return Streams.concat(
                stacks.stream().filter(s -> s.is(BotaniaTags.Items.RUNES)).map(ItemStack::copy),
                super.resultItems(recipe, stacks).stream()
        ).toList();
    }

    @Override
    protected void onCrafted(RunicAltarRecipe recipe) {
        Inventories.extract(this.inventory, 0, 1, false);
    }

    @Override
    protected int getMaxProgress(RunicAltarRecipe recipe) {
        return recipe.getManaUsage();
    }

    @Override
    public int getMaxManaPerTick() {
        return MAX_MANA_PER_TICK * LibXServerConfig.WorkingDurationMultiplier.mechanicalRunicAltar;
    }

    public boolean isSlotUsedCurrently(int slot) {
        return this.slotsUsed.contains(slot);
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        this.slotsUsed.clear();
        this.slotsUsed.addAll(Arrays.stream(nbt.getIntArray(TileTags.SLOTS_USED).orElse(new int[0])).boxed().toList());
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.putIntArray(TileTags.SLOTS_USED, this.slotsUsed.stream().mapToInt(Integer::intValue).toArray());
    }


}
