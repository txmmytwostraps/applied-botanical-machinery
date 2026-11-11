package appliedbotanicalmachinery.extramachinery.blocks.pattern;

import appliedbotanicalmachinery.util.SimpleFluidTank;

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
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary.BlockEntityApothecaryAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary.BlockEntityApothecaryBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary.BlockEntityApothecaryUpgraded;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.base.tile.TickingBlock;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.recipe.CustomApothecaryColor;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.registry.BotaniaSounds;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class BlockEntityApothecaryPattern extends WorkingTile<RecipeInput, PetalApothecaryRecipe>
        implements TickingBlock, IInWorldGridNodeHost, IGridConnectedBlockEntity {
    public static final int WORKING_DURATION = LibXServerConfig.ApothecarySettings.workingDuration;
    public final int FLUID_CAPACITY;

    private final int SEEDS_SLOT;
    private final int FIRST_INPUT_SLOT;
    private final int LAST_INPUT_SLOT;
    private final int FIRST_OUTPUT_SLOT;
    private final int LAST_OUTPUT_SLOT;

    private int UPGRADE_SLOT_1 = -1;
    private int UPGRADE_SLOT_2 = -1;

    private final BaseItemStackHandler inventory;
    private final SimpleFluidTank fluidInventory;
    private ItemStack currentOutput;

    private int timeCheckOutputSlot = LibXServerConfig.tickOutputSlots;

    private final SettingPattern settingPattern;

    private boolean isInfinityWater = false;
    private int seedBuffer = 0;

    public BlockEntityApothecaryPattern(BlockEntityType<?> type, BlockPos pos, BlockState state, int[] slots, int countCraft, SettingPattern settingPattern) {
        super(type, BotaniaRecipeTypes.PETAL_TYPE, pos, state, 0, slots[1], slots[3], countCraft);
        this.currentOutput = ItemStack.EMPTY;

        this.SEEDS_SLOT = slots[0];
        this.FIRST_INPUT_SLOT = slots[1];
        this.LAST_INPUT_SLOT = slots[2];
        this.FIRST_OUTPUT_SLOT = slots[3];
        this.LAST_OUTPUT_SLOT = slots[4];

        if (slots.length >= 6){
            UPGRADE_SLOT_1 = slots[5];
        }

        if (slots.length >= 7){
            UPGRADE_SLOT_2 = slots[6];
        }

        this.settingPattern = settingPattern;


        this.FLUID_CAPACITY = settingPattern.getConfigInt("fluidStorage");
        fluidInventory = new SimpleFluidTank(FLUID_CAPACITY, (fluidStack) -> {
            return Fluids.WATER.isSame(fluidStack.getFluid());
        }, false, () -> {
            this.setChanged();
            this.setDispatchable();
            this.needsRecipeUpdate();
        });

        if (UPGRADE_SLOT_1 != -1 && UPGRADE_SLOT_2 == -1){
            this.inventory = BaseItemStackHandler.builder(this.LAST_OUTPUT_SLOT + 1)
                    .validator( (stack) -> { return stack.is(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);}, SEEDS_SLOT)
                    .validator( (stack) -> {return (stack.getItem() == ModItems.catalystSeedInfinity.asItem());}, UPGRADE_SLOT_1)
                    .validator( (stack) -> { return this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.PETAL_TYPE, stack);}, Range.closedOpen(FIRST_INPUT_SLOT, LAST_INPUT_SLOT + 1))
                    .slotLimit(1, UPGRADE_SLOT_1)
                    .output(Range.closedOpen(FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT + 1)).contentsChanged(() -> {this.setChanged();this.setDispatchable();this.needsRecipeUpdate();})
                    .build();
        } else if (UPGRADE_SLOT_1 != -1){
            this.inventory = BaseItemStackHandler.builder(this.LAST_OUTPUT_SLOT + 1)
                    .validator( (stack) -> { return stack.is(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);}, SEEDS_SLOT)
                    .validator( (stack) -> {return (stack.getItem() == ModItems.catalystSeedInfinity.asItem() || stack.getItem() == ModItems.catalystWaterInfinity.asItem());}, UPGRADE_SLOT_1, UPGRADE_SLOT_2)
                    .validator( (stack) -> { return this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.PETAL_TYPE, stack);}, Range.closedOpen(FIRST_INPUT_SLOT, LAST_INPUT_SLOT + 1))
                    .slotLimit(1, UPGRADE_SLOT_1, UPGRADE_SLOT_2)
                    .output(Range.closedOpen(FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT + 1)).contentsChanged(() -> {this.setChanged();this.setDispatchable();this.needsRecipeUpdate();})
                    .build();
        } else {
            this.inventory = BaseItemStackHandler.builder(this.LAST_OUTPUT_SLOT + 1)
                    .validator( (stack) -> { return stack.is(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);}, SEEDS_SLOT)
                    .validator( (stack) -> { return this.level != null && Recipes.isItemValidInput(this.level, BotaniaRecipeTypes.PETAL_TYPE, stack);}, Range.closedOpen(FIRST_INPUT_SLOT, LAST_INPUT_SLOT + 1))
                    .output(Range.closedOpen(FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT + 1)).contentsChanged(() -> {this.setChanged();this.setDispatchable();this.needsRecipeUpdate();})
                    .build();
        }

        this.setChangedQueued = false;
    }

    public void bufferCheck(){
        int old = seedBuffer;
        seedBuffer = seedBuffer > Inventories.get(this.inventory, 0).getCount() ? seedBuffer - Inventories.get(this.inventory, 0).getCount(): 0;
        Inventories.extract(this.inventory, 0, seedBuffer == 0 ? old : Inventories.get(this.inventory, 0).getCount(), false);
    }


    //region Base
    public void tick() {
        if (this.level != null && !this.level.isClientSide()){
            if (!this.getMainNode().isReady()){
                this.getMainNode().create(this.level, this.getBlockPos());
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

            if (!Inventories.get(this.inventory, 0).isEmpty() && seedBuffer != 0) bufferCheck();
            upgradedCheck();

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

        }
        else if (this.level != null && LibXClientConfig.RenderingVisualContent.all && settingPattern.getConfigBoolean("mechanicalApothecaryRender") && this.fluidInventory.getFluidAmount() > 0) {
            int slot;
            if (this.getMaxProgress() > 0 && this.getProgress() > this.getMaxProgress() - 5) {
                for(slot = 0; slot < 5; ++slot) {
                    SparkleParticleData data = SparkleParticleData.sparkle(this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), this.level.getRandom().nextFloat(), 10);
                    this.level.addParticle(data, (double)this.worldPosition.getX() + 0.3 + this.level.getRandom().nextDouble() * 0.4, (double)this.worldPosition.getY() + 0.6, (double)this.worldPosition.getZ() + 0.3 + this.level.getRandom().nextDouble() * 0.4, 0.0, 0.0, 0.0);
                }

                this.level.playLocalSound((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.5, BotaniaSounds.altarCraft, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            } else {
                for(slot = 0; slot < this.inventory.size(); ++slot) {
                    ItemStack stack = Inventories.get(this.inventory, slot);
                    if (!stack.isEmpty() && this.level.getRandom().nextFloat() >= 0.97F) {
                        Item item1 = stack.getItem();
                        int i;
                        if (item1 instanceof CustomApothecaryColor) {
                            CustomApothecaryColor item = (CustomApothecaryColor)item1;
                            i = item.getParticleColor(stack);
                        } else {
                            i = 8947848;
                        }

                        int color = i;
                        float red = (float)(color >> 16 & 255) / 255.0F;
                        float green = (float)(color >> 8 & 255) / 255.0F;
                        float blue = (float)(color & 255) / 255.0F;
                        if (Math.random() >= 0.75) {
                            this.level.playSound((Player)null, this.worldPosition, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 10.0F);
                        }

                        SparkleParticleData data = SparkleParticleData.sparkle(this.level.getRandom().nextFloat(), red, green, blue, 10);
                        this.level.addParticle(data, (double)this.worldPosition.getX() + 0.3 + this.level.getRandom().nextDouble() * 0.4, (double)this.worldPosition.getY() + 0.6, (double)this.worldPosition.getZ() + 0.3 + this.level.getRandom().nextDouble() * 0.4, 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }

    private boolean checkOutputSlots(){
        for (int i = FIRST_OUTPUT_SLOT; i <= LAST_OUTPUT_SLOT; i++){
            if (!Inventories.get(inventory, i).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getUpgrades(){
        List<ItemStack> res = new ArrayList<>();

        if (UPGRADE_SLOT_1 != -1){
            res.add(new ItemStack(ModItems.catalystSeedInfinity));
        }

        if (UPGRADE_SLOT_2 != -1){
            res.add(new ItemStack(ModItems.catalystWaterInfinity));
        }

        return res;
    }

    public void upgradedCheck(){
        if (UPGRADE_SLOT_1 != -1 && UPGRADE_SLOT_2 == -1){
            if (!Inventories.get(inventory, UPGRADE_SLOT_1).isEmpty() && Inventories.get(inventory, SEEDS_SLOT).isEmpty()){
                ItemStack seed = Items.WHEAT_SEEDS.getDefaultInstance();
                seed.setCount(64);

                Inventories.set(inventory, SEEDS_SLOT, seed);
            }
        } else if (UPGRADE_SLOT_1 != -1){

            if (((!Inventories.get(inventory, UPGRADE_SLOT_1).isEmpty() && Inventories.get(inventory, UPGRADE_SLOT_1).getItem().asItem() == ModItems.catalystSeedInfinity.asItem()) ||
                    (!Inventories.get(inventory, UPGRADE_SLOT_2).isEmpty() && Inventories.get(inventory, UPGRADE_SLOT_2).getItem().asItem() == ModItems.catalystSeedInfinity.asItem())) &&
                    Inventories.get(inventory, SEEDS_SLOT).isEmpty()){
                ItemStack seed = Items.WHEAT_SEEDS.getDefaultInstance();
                seed.setCount(64);

                Inventories.set(inventory, SEEDS_SLOT, seed);
            }

            isInfinityWater =
                    (!Inventories.get(inventory, UPGRADE_SLOT_1).isEmpty() && Inventories.get(inventory, UPGRADE_SLOT_1).getItem().asItem() == ModItems.catalystWaterInfinity.asItem()) ||
                    (!Inventories.get(inventory, UPGRADE_SLOT_2).isEmpty() && Inventories.get(inventory, UPGRADE_SLOT_2).getItem().asItem() == ModItems.catalystWaterInfinity.asItem());
        }
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

    @Nonnull
    public SimpleFluidTank getFluidInventory() {
        return this.fluidInventory;
    }

    public boolean actAsMana() {
        return false;
    }

    protected int getAndApplyProgressThisTick() {
        return 1;
    }

    protected boolean canMatchRecipes() {
        if (Inventories.get(this.inventory, 0).isEmpty()) {
            return false;
        } else {
            if (isInfinityWater) {
                return true;
            }
            FluidStack fluid = this.getFluidInventory().getFluid();
            return !fluid.isEmpty() && fluid.getFluid() == Fluids.WATER && fluid.getAmount() >= 1000;
        }
    }

    @Override
    protected void onCrafted(PetalApothecaryRecipe recipe, int countItemCraft) {
        seedBuffer = countItemCraft > Inventories.get(this.inventory, 0).getCount() ? countItemCraft - Inventories.get(this.inventory, 0).getCount(): 0;

        Inventories.extract(this.inventory, 0, seedBuffer == 0 ? countItemCraft : Inventories.get(this.inventory, 0).getCount(), false);
        FluidStack fluid = this.getFluidInventory().getFluid().copy();
        if (!isInfinityWater && fluid.getFluid() == Fluids.WATER) {
            int newAmount = Math.max(0, fluid.getAmount() - 1000);
            fluid.setAmount(newAmount);
            this.fluidInventory.setFluid(fluid);
        }
    }

    protected int getMaxProgress(PetalApothecaryRecipe recipe) {
        return WORKING_DURATION * settingPattern.getConfigInt("craftTime");
    }

    public int getMaxManaPerTick() {
        return 1;
    }

    public ItemStack getCurrentOutput() {
        return this.currentOutput;
    }

    protected void loadAdditional(@Nonnull ValueInput nbt) {
        super.loadAdditional(nbt);
        this.fluidInventory.setFluid(nbt.read("fluid", FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY));
        this.currentOutput = nbt.read("currentOutput", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.seedBuffer = nbt.getIntOr("seedBuffer", 0);
        this.getMainNode().deserialize(nbt);

        this.setChanged();
        this.setDispatchable();
    }

    protected void saveAdditional(@Nonnull ValueOutput nbt) {
        super.saveAdditional(nbt);
        nbt.store("fluid", FluidStack.OPTIONAL_CODEC, this.getFluidInventory().getFluid());
        nbt.store("currentOutput", ItemStack.OPTIONAL_CODEC, this.currentOutput);
        nbt.putInt("seedBuffer", this.seedBuffer);
        this.getMainNode().serialize(nbt);
    }




    public BlockEntity getBlockEntity() {
        return this;
    }

    public void setRemoved() {
        super.setRemoved();

        if (this.getMainNode() != null) {
            this.getMainNode().destroy();
        }
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
        if (blockEntity instanceof BlockEntityApothecaryBase){
            return ModBlocks.baseApothecary.asItem();
        }
        else if (blockEntity instanceof BlockEntityApothecaryUpgraded){
            return ModBlocks.upgradedApothecary.asItem();
        }
        else if (blockEntity instanceof BlockEntityApothecaryAdvanced){
            return ModBlocks.advancedApothecary.asItem();
        }
        else {
            return ModBlocks.ultimateApothecary.asItem();
        }
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

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
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
    //endregion
}
