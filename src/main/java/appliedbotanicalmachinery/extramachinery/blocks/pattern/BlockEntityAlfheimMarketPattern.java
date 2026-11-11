package appliedbotanicalmachinery.extramachinery.blocks.pattern;

import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.level.storage.ValueInput;

import appliedbotanicalmachinery.util.Recipes;

import appliedbotanicalmachinery.util.Inventories;

import appeng.api.config.Actionable;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.util.AECableType;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import com.google.common.collect.Range;
import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityAlfheimMarketPattern extends WorkingTile<RecipeInput, ElvenTradeRecipe>
        implements IInWorldGridNodeHost, IGridConnectedBlockEntity {

    public static final int MAX_MANA_PER_TICK = LibXServerConfig.AlfheimMarketSettings.workingDuration;
    private BaseItemStackHandler inventory;
    private ItemStack currentInput;
    private ItemStack currentOutput;

    private int UPGRADE_SLOT = -1;
    private final int FIRST_INPUT_SLOT;
    private final int LAST_INPUT_SLOT;
    private final int FIRST_OUTPUT_SLOT;
    private final int LAST_OUTPUT_SLOT;

    private final SettingPattern settingPattern;
    private int timeCheckOutputSlot = LibXServerConfig.tickOutputSlots;

    private boolean isInfinityMana = false;


    public BlockEntityAlfheimMarketPattern(BlockEntityType<?> type, BlockPos pos, BlockState state, int manaCapacity, int countCraft, int[] slots, SettingPattern settingPattern) {
        super(type, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, pos, state, manaCapacity, slots[0], slots[2], countCraft);

        this.currentInput = ItemStack.EMPTY;
        this.currentOutput = ItemStack.EMPTY;

        FIRST_INPUT_SLOT = slots[0];
        LAST_INPUT_SLOT = slots[1];
        FIRST_OUTPUT_SLOT = slots[2];
        LAST_OUTPUT_SLOT = slots[3];

        if (slots.length >= 5){
            UPGRADE_SLOT = slots[4];
        }

        this.settingPattern = settingPattern;

        if (UPGRADE_SLOT != -1) {
            this.inventory = BaseItemStackHandler.builder(LAST_OUTPUT_SLOT + 1)
                    .validator( (stack) -> {return this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, stack);}, Range.closedOpen(FIRST_INPUT_SLOT, LAST_INPUT_SLOT + 1))
                    .validator( (stack) -> {return stack.getItem() == ModItems.catalystManaInfinity.asItem(); }, UPGRADE_SLOT)
                    .slotLimit(1, UPGRADE_SLOT)
                    .output(Range.closedOpen(FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT + 1))
                    .contentsChanged(() -> {this.setChanged(); this.setDispatchable(); this.needsRecipeUpdate();})
                    .build();
        } else {
            this.inventory = BaseItemStackHandler.builder(LAST_OUTPUT_SLOT + 1)
                    .validator( (stack) -> {return this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, stack);}, Range.closedOpen(FIRST_INPUT_SLOT, LAST_INPUT_SLOT + 1))
                    .output(Range.closedOpen(FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT + 1))
                    .contentsChanged(() -> {this.setChanged(); this.setDispatchable(); this.needsRecipeUpdate();})
                    .build();
        }

        this.setChangedQueued = false;

    }

    //region Base

    public void tick() {
        if (this.level != null && !this.level.isClientSide()) {
            if (!this.getMainNode().isReady()){
                this.getMainNode().create(this.level, this.getBlockPos());
            }

            isInfinityMana = UPGRADE_SLOT != -1 && Inventories.get(this.inventory, UPGRADE_SLOT).getItem().asItem() == ModItems.catalystManaInfinity;

            this.runRecipeTick(
                    () -> {this.currentInput = ItemStack.EMPTY;},
                    (stack, slotx) -> {this.currentInput = stack.copy();},
                    (stack, slotx) -> {}
            );

            if (this.recipe != null) {
                this.currentOutput = this.recipe.getOutputs().size() == 0 ? ItemStack.EMPTY : this.recipe.getOutputs().get(0).copy();
                this.setChanged();
                this.setDispatchable();
            } else if (!this.currentInput.isEmpty() || !this.currentOutput.isEmpty()) {
                this.currentInput = ItemStack.EMPTY;
                this.currentOutput = ItemStack.EMPTY;
                this.setChanged();
                this.setDispatchable();
            }

            if (getMainNode() != null && getMainNode().getNode() != null && getMainNode().isOnline()){
                if (timeCheckOutputSlot <= 0){
                    if (checkOutputSlots()){
                        this.exportResultsItemsME();
                    }

                    timeCheckOutputSlot = LibXServerConfig.tickOutputSlots;
                } else {
                    timeCheckOutputSlot--;
                }
            }


            if (this.getCurrentMana() > 0) {
                for(int slot = 0; slot < 4; ++slot) {
                    if (Inventories.get(this.inventory, slot).getItem() == Items.BREAD) {
                        this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
                        this.level.explode(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 3.0F, Level.ExplosionInteraction.BLOCK);
                        break;
                    }
                }
            }
        }

    }

    private boolean checkOutputSlots(){
        for (int i = FIRST_OUTPUT_SLOT; i < LAST_OUTPUT_SLOT + 1; i++){
            if (!Inventories.get(this.inventory, i).isEmpty()){
                return true;
            }
        }

        return false;
    }

    protected Predicate<Integer> getExtracts(Supplier<IAdvancedItemHandlerModifiable> inventory) {
        return (slot) -> {
            return slot >= FIRST_OUTPUT_SLOT && slot <= LAST_OUTPUT_SLOT;
        };
    }

    @Nonnull
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    protected List<ItemStack> resultItems(ElvenTradeRecipe recipe, List<ItemStack> stacks) {

        return recipe.getOutputs().stream().map(ItemStack::copy).toList();
    }

    public int getMaxProgress(ElvenTradeRecipe recipe) {
        return LibXServerConfig.AlfheimMarketSettings.recipeCost;
    }

    public int getMaxManaPerTick() {
        return MAX_MANA_PER_TICK * settingPattern.getConfigInt("craftTime");
    }

    @Override
    public boolean getUpgradeInfinityMana() {
        return this.isInfinityMana;
    }

    public ItemStack getCurrentInput() {
        return this.currentInput;
    }

    public ItemStack getCurrentOutput() {
        return this.currentOutput;
    }

    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        this.currentInput = nbt.read("currentInput", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.currentOutput = nbt.read("currentOutput", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.getMainNode().deserialize(nbt);

        this.setChanged();
        this.setDispatchable();
    }

    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.store("currentInput", ItemStack.OPTIONAL_CODEC, this.currentInput);
        nbt.store("currentOutput", ItemStack.OPTIONAL_CODEC, this.currentOutput);
        this.getMainNode().serialize(nbt);
    }



    public BlockEntity getBlockEntity(){
        return this;
    }
    //endregion

    //region AE INTEGRATION
    private boolean setChangedQueued;

    private final IManagedGridNode mainNode = this.createMainNode().setVisualRepresentation(this.getItemFromBlockEntity()).setInWorldNode(true).setTagName("proxy");


    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE);
    }

    protected Item getItemFromBlockEntity() {
        BlockEntity blockEntity = this.getBlockEntity();
        if (blockEntity instanceof BlockEntityAlfheimMarketBase){
            return ModBlocks.baseAlfheimMarket.asItem();
        }
        else if (blockEntity instanceof BlockEntityAlfheimMarketUpgraded){
            return ModBlocks.upgradedAlfheimMarket.asItem();
        }
        else if (blockEntity instanceof BlockEntityAlfheimMarketAdvanced){
            return ModBlocks.advancedAlfheimMarket.asItem();
        }
        else {
            return ModBlocks.ultimateAlfheimMarket.asItem();
        }
    }

    @Override
    public void setRemoved() {
        if (this.getMainNode() != null) {
            this.getMainNode().destroy();
        }

        super.setRemoved();
    }

    private Object setChangedAtEndOfTick(Level level) {
        this.setChanged();
        this.setChangedQueued = false;
        return null;
    }

    @Nullable
    @Override
    public IGridNode getGridNode(Direction direction) {
        return this.getMainNode().getNode();
    }

    @Override
    public IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    @Override
    public void saveChanges() {
        if (level != null) {
            if (level.isClientSide()) {
                this.setChanged();
            } else {
                this.level.blockEntityChanged(this.worldPosition);
                if (!this.setChangedQueued) {
                    TickHandler.instance().addCallable(null, this::setChangedAtEndOfTick);
                    this.setChangedQueued = true;
                }
            }
        }
    }

    private void exportResultsItemsME(){
        for (int slot = FIRST_OUTPUT_SLOT; slot <= LAST_OUTPUT_SLOT; slot++) {
            ItemStack stackInSlot = Inventories.get(this.inventory, slot);

            if (!stackInSlot.isEmpty()) {
                int getCountExport = Math.toIntExact(this.getMainNode().getGrid().getStorageService().getInventory().insert(AEItemKey.of(stackInSlot), stackInSlot.getCount(), Actionable.MODULATE, IActionSource.empty()));

                if (getCountExport > 0) {
                    stackInSlot.shrink(getCountExport);
                    Inventories.set(this.inventory, slot, stackInSlot);
                }
            }
        }
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }
    //endregion
}
