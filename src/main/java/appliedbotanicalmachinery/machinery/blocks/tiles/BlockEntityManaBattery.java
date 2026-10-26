package appliedbotanicalmachinery.machinery.blocks.tiles;

import appliedbotanicalmachinery.machinery.blocks.BlockManaBattery;
import appliedbotanicalmachinery.machinery.blocks.base.BotanicalTile;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.common.item.BlackLotusItem;
import velrondevs.botania.registry.BotaniaItems;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityManaBattery extends BotanicalTile {

    private static final int MANA_TRANSFER_RATE = 5000;
    private boolean slot1Locked;
    private boolean slot2Locked;

    private final BaseItemStackHandler inventory;

    public BlockEntityManaBattery(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, LibXServerConfig.MaxManaCapacity.manaBattery);
        this.inventory = BaseItemStackHandler.builder(2)
                .validator(stack -> !this.slot1Locked && Optional.ofNullable(stack.getCapability(BotaniaCapabilities.MANA_ITEM)).map(manaItem -> manaItem.getMana() < manaItem.getMaxMana() && manaItem.canReceiveManaFromPool(this)).orElse(false), 0)
                .validator(stack -> !this.slot2Locked && Optional.ofNullable(stack.getCapability(BotaniaCapabilities.MANA_ITEM)).map(manaItem -> manaItem.getMana() > 0 && manaItem.canExportManaToPool(this))
                        .orElse(stack.getItem() instanceof BlackLotusItem), 1)
                .contentsChanged(() -> {
                    this.setChanged();
                    this.setDispatchable();
                })
                .build();
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public int getCurrentMana() {
        return ((BlockManaBattery) this.getBlockState().getBlock()).variant == BlockManaBattery.Variant.CREATIVE ? this.getMaxMana() / 2 : super.getCurrentMana();
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            ItemStack minus = this.inventory.getStackInSlot(0);
            ItemStack plus = this.inventory.getStackInSlot(1);
            if (!minus.isEmpty()) {
                Optional.ofNullable(minus.getCapability(BotaniaCapabilities.MANA_ITEM)).ifPresent(manaItem -> {
                    if (manaItem.canReceiveManaFromPool(this)) {
                        int maxManaValue = ((BlockManaBattery) this.getBlockState().getBlock()).variant == BlockManaBattery.Variant.NORMAL ? MANA_TRANSFER_RATE : Integer.MAX_VALUE;
                        int manaValue = Math.min(maxManaValue, Math.min(this.getCurrentMana(), manaItem.getMaxMana() - manaItem.getMana()));
                        manaItem.addMana(manaValue);
                        this.receiveMana(-manaValue);
                        this.setChanged();
                        this.setDispatchable();
                    }
                });
            }
            if (!plus.isEmpty()) {
                Optional.ofNullable(plus.getCapability(BotaniaCapabilities.MANA_ITEM)).ifPresent(manaItem -> {
                    if (manaItem.canExportManaToPool(this)) {
                        int maxManaValue = ((BlockManaBattery) this.getBlockState().getBlock()).variant == BlockManaBattery.Variant.NORMAL ? MANA_TRANSFER_RATE : Integer.MAX_VALUE;
                        int manaValue = Math.min(maxManaValue, Math.min(this.getMaxMana() - this.getCurrentMana(), manaItem.getMana()));
                        manaItem.addMana(-manaValue);
                        this.receiveMana(manaValue);
                        this.setChanged();
                        this.setDispatchable();
                    }
                });
                if (plus.getItem() instanceof BlackLotusItem) {
                    int manaToTransfer = plus.getItem() == BotaniaItems.blackerLotus ? 100000 : 8000;
                    if (this.getAvailableSpaceForMana() >= manaToTransfer) {
                        this.receiveMana(manaToTransfer);
                        ItemStack stack = this.inventory.getStackInSlot(1).copy();
                        stack.shrink(1);
                        this.inventory.setStackInSlot(1, stack);
                        this.setChanged();
                        this.setDispatchable();
                    }
                }
            }
            for (Direction direction : Direction.values()) {
                BlockEntity tile = this.level.getBlockEntity(this.getBlockPos().relative(direction));
                if (tile instanceof BotanicalTile offsetTile && offsetTile.actAsMana() && (((BlockManaBattery) this.getBlockState().getBlock()).variant == BlockManaBattery.Variant.CREATIVE || !(tile instanceof BlockEntityManaBattery))) {
                    if (!offsetTile.isFull()) {
                        int maxManaValue = ((BlockManaBattery) this.getBlockState().getBlock()).variant == BlockManaBattery.Variant.NORMAL ? MANA_TRANSFER_RATE : Integer.MAX_VALUE;
                        int manaValue = Math.min(maxManaValue, Math.min(this.getCurrentMana(), offsetTile.getMaxMana() - offsetTile.getCurrentMana()));
                        if (manaValue <= 0 && offsetTile instanceof BlockEntityMechanicalManaPool)
                            manaValue = Math.min(this.getCurrentMana(), MANA_TRANSFER_RATE);
                        this.receiveMana(-manaValue);
                        offsetTile.receiveMana(manaValue);
                        this.setChanged();
                        this.setDispatchable();
                    }
                }
            }
        }
    }

    public boolean isSlot1Locked() {
        return this.slot1Locked;
    }

    public boolean isSlot2Locked() {
        return this.slot2Locked;
    }

    public void setSlot1Locked(boolean slot1Locked) {
        // Do not remove the condition! (Because of packets)
        if (slot1Locked != this.slot1Locked) {
            this.slot1Locked = slot1Locked;
            this.setChanged();
        }
    }

    public void setSlot2Locked(boolean slot2Locked) {
        // Do not remove the condition! (Because of packets)
        if (slot2Locked != this.slot2Locked) {
            this.slot2Locked = slot2Locked;
            this.setChanged();
        }
    }

    @Override
    public int getComparatorOutput() {
        return (int) Math.round(this.getCurrentMana() / (double) this.getMaxMana() * 15d);
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory) {
        return slot -> {
            ItemStack minus = inventory.get().getStackInSlot(0);
            ItemStack plus = inventory.get().getStackInSlot(1);
            if (slot == 0) {
                Optional<ManaItem> manaItem = Optional.ofNullable(minus.getCapability(BotaniaCapabilities.MANA_ITEM));
                return manaItem.map(item -> item.getMana() >= item.getMaxMana()).orElse(true);
            } else if (slot == 1 && plus.getItem() instanceof ManaItem) {
                Optional<ManaItem> manaItem = Optional.ofNullable(plus.getCapability(BotaniaCapabilities.MANA_ITEM));
                return manaItem.map(item -> item.getMana() <= 0).orElse(true);
            }

            return true;
        };
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.slot1Locked = nbt.getBoolean(TileTags.SLOT_1_LOCKED);
        this.slot2Locked = nbt.getBoolean(TileTags.SLOT_2_LOCKED);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putBoolean(TileTags.SLOT_1_LOCKED, this.slot1Locked);
        nbt.putBoolean(TileTags.SLOT_2_LOCKED, this.slot2Locked);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.handleUpdateTag(nbt, registries);
        if (this.level != null && !this.level.isClientSide) return;
        this.slot1Locked = nbt.getBoolean(TileTags.SLOT_1_LOCKED);
        this.slot2Locked = nbt.getBoolean(TileTags.SLOT_2_LOCKED);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.putBoolean(TileTags.SLOT_1_LOCKED, this.slot1Locked);
        nbt.putBoolean(TileTags.SLOT_2_LOCKED, this.slot2Locked);
        return nbt;
    }
}
