package appliedbotanicalmachinery.machinery.blocks.tiles;

import com.google.common.collect.Range;
import appliedbotanicalmachinery.machinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.moddingx.libx.crafting.RecipeHelper;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.brew.BrewItem;
import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.registry.BotaniaItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityMechanicalBrewery extends WorkingTile<RecipeInput, BotanicalBreweryRecipe> {

    public static final int MAX_MANA_PER_TICK = 50;
    public static final List<Item> BREW_CONTAINER = List.of(BotaniaItems.vial.asItem(), BotaniaItems.flask.asItem(), BotaniaItems.incenseStick.asItem(), BotaniaItems.bloodPendant.asItem());

    private final BaseItemStackHandler inventory;

    private ItemStack currentOutput = ItemStack.EMPTY;

    public BlockEntityMechanicalBrewery(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, BotaniaRecipeTypes.BREW_TYPE, pos, state, LibXServerConfig.MaxManaCapacity.mechanicalBrewery, 1, 7);
        this.inventory = BaseItemStackHandler.builder(8)
                .validator(stack -> BREW_CONTAINER.contains(stack.getItem()) && !stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("brewKey"), 0)
                .validator(stack -> this.level != null && RecipeHelper.isItemValidInput(this.level.getRecipeManager(), BotaniaRecipeTypes.BREW_TYPE, stack), Range.closedOpen(1, 7))
                .output(7)
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
                this.currentOutput = this.recipe.getOutput(this.inventory.getStackInSlot(0));
                this.setChanged();
                this.setDispatchable();
            } else if (!this.currentOutput.isEmpty()) {
                this.currentOutput = ItemStack.EMPTY;
                this.setChanged();
                this.setDispatchable();
            }
        } else if (this.level != null) {
            if (this.getProgress() > 0 && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.mechanicalBrewery) {
                if (this.currentOutput.getItem() instanceof BrewItem item && this.level.random.nextFloat() < 0.5f) {
                    int segments = 3;
                    for (int i = 1; i <= 6; i++) {
                        if (!this.inventory.getStackInSlot(i).isEmpty()) {
                            segments += 1;
                        }
                    }
                    if (this.getProgress() < (segments - 1) * (this.getMaxProgress() / (double) segments) && this.getProgress() > (segments - 2) * (this.getMaxProgress() / (double) segments)) {
                        int targetColor = item.getBrew(this.currentOutput).getColor(this.currentOutput);
                        float red = (targetColor >> 16 & 255) / 255f;
                        float green = (targetColor >> 8 & 255) / 255f;
                        float blue = (targetColor & 255) / 255f;
                        WispParticleData data = WispParticleData.wisp(0.125f, red, green, blue, 0.5f);
                        double xPos = this.worldPosition.getX() + 0.25 + (this.level.random.nextDouble() / 2);
                        double zPos = this.worldPosition.getZ() + 0.25 + (this.level.random.nextDouble() / 2);
                        this.level.addParticle(data, xPos, this.worldPosition.getY() + 0.35, zPos, 0, 0.01 + (this.level.random.nextDouble() / 18), 0);
                    }
                }
            }
        }
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory) {
        return slot -> slot == 7;
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected boolean canMatchRecipes() {
        return !this.inventory.getStackInSlot(0).isEmpty() && BREW_CONTAINER.contains(this.inventory.getStackInSlot(0).getItem());
    }

    @Override
    protected List<ItemStack> resultItems(BotanicalBreweryRecipe recipe, List<ItemStack> stacks) {
        return List.of(recipe.getOutput(this.inventory.getStackInSlot(0)));
    }

    @Override
    protected void onCrafted(BotanicalBreweryRecipe recipe) {
        this.inventory.getUnrestricted().extractItem(0, 1, false);
    }

    @Override
    protected int getMaxProgress(BotanicalBreweryRecipe recipe) {
        return recipe.getManaUsage();
    }

    @Override
    public int getMaxManaPerTick() {
        return MAX_MANA_PER_TICK * LibXServerConfig.WorkingDurationMultiplier.mechanicalBrewery;
    }

    public ItemStack getCurrentOutput() {
        return this.currentOutput;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        CompoundTag tankTag = new CompoundTag();
        nbt.put(TileTags.FLUID, tankTag);
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.handleUpdateTag(nbt, registries);
        if (this.level != null && !this.level.isClientSide) return;
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        final CompoundTag tankTag = new CompoundTag();
        nbt.put(TileTags.FLUID, tankTag);
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
        return nbt;
    }
}
