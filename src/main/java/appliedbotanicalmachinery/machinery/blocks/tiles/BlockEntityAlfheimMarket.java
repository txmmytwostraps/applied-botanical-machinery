package appliedbotanicalmachinery.machinery.blocks.tiles;

import appliedbotanicalmachinery.machinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.moddingx.libx.crafting.RecipeHelper;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityAlfheimMarket extends WorkingTile<RecipeInput, ElvenTradeRecipe> {

    public static final int MAX_MANA_PER_TICK = 25;

    private final BaseItemStackHandler inventory;

    private ItemStack currentInput = ItemStack.EMPTY;
    private ItemStack currentOutput = ItemStack.EMPTY;

    public BlockEntityAlfheimMarket(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, pos, state, LibXServerConfig.MaxManaCapacity.alfheimMarket, 0, 4);
        this.inventory = BaseItemStackHandler.builder(5)
                .validator(stack -> this.level != null && RecipeHelper.isItemValidInput(this.level.getRecipeManager(), BotaniaRecipeTypes.ELVEN_TRADE_TYPE, stack), 0, 1, 2, 3)
                .output(4)
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
            this.runRecipeTick(() -> this.currentInput = ItemStack.EMPTY, (stack, slot) -> this.currentInput = stack.copy(), (stack, slot) -> {
            });
            if (this.recipe != null) {
                this.currentOutput = this.recipe.getOutputs().isEmpty() ? ItemStack.EMPTY : this.recipe.getOutputs().get(0).copy();
                this.setChanged();
                this.setDispatchable();
            } else if (!this.currentInput.isEmpty() || !this.currentOutput.isEmpty()) {
                this.currentInput = ItemStack.EMPTY;
                this.currentOutput = ItemStack.EMPTY;
                this.setChanged();
                this.setDispatchable();
            }
            if (this.getCurrentMana() > 0) {
                for (int slot = 0; slot < 4; slot++) {
                    if (this.inventory.getStackInSlot(slot).getItem() == Items.BREAD) {
                        this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
                        this.level.explode(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 3, Level.ExplosionInteraction.BLOCK);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory) {
        return slot -> slot == 4;
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected List<ItemStack> resultItems(ElvenTradeRecipe recipe, List<ItemStack> stacks) {
        return recipe.getOutputs().stream().map(ItemStack::copy).toList();
    }

    @Override
    public int getMaxProgress(ElvenTradeRecipe recipe) {
        return LibXServerConfig.AlfheimMarket.recipeCost;
    }

    @Override
    public int getMaxManaPerTick() {
        return MAX_MANA_PER_TICK * LibXServerConfig.WorkingDurationMultiplier.alfheimMarket;
    }

    public ItemStack getCurrentInput() {
        return this.currentInput;
    }

    public ItemStack getCurrentOutput() {
        return this.currentOutput;
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.currentInput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_INPUT));
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(TileTags.CURRENT_INPUT, this.currentInput.saveOptional(registries));
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        if (this.level != null && !this.level.isClientSide) return;
        super.handleUpdateTag(nbt, registries);
        this.currentInput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_INPUT));
        this.currentOutput = ItemStack.parseOptional(registries, nbt.getCompound(TileTags.CURRENT_OUTPUT));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.put(TileTags.CURRENT_INPUT, this.currentInput.saveOptional(registries));
        nbt.put(TileTags.CURRENT_OUTPUT, this.currentOutput.saveOptional(registries));
        return nbt;
    }
}
