package appliedbotanicalmachinery.machinery.blocks.base;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;
import appliedbotanicalmachinery.machinery.core.TileTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.lwjgl.opengl.GL11;
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import org.moddingx.libx.inventory.FilterItemHandler;
import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.item.SparkEntity;
import velrondevs.botania.api.mana.KeyLocked;
import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.mana.spark.ManaSpark;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.common.block.block_entity.mana.ThrottledPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

@OnlyIn(value = Dist.CLIENT, _interface = WandHUD.class)
public abstract class BotanicalTile extends BlockEntityBase implements ManaPool, KeyLocked, SparkAttachable, ThrottledPacket, WandHUD, TickingBlock {

    private int mana;
    private final int manaCap;
    private String inputKey = "";
    private String outputKey = "";

    @Nullable
    private IItemHandler itemHandler;

    public BotanicalTile(BlockEntityType<?> blockEntityTypeIn, BlockPos pos, BlockState state, int manaCap) {
        super(blockEntityTypeIn, pos, state);
        this.manaCap = manaCap;
    }

    /**
     * The item handler exposed as capability. It is created on first use, as the inventory does not exist while
     * the constructors run.
     */
    public IItemHandler getItemHandler() {
        if (this.itemHandler == null) {
            this.itemHandler = this.createItemHandler(this::getInventory);
        }
        return this.itemHandler;
    }

    /**
     * This can be used to add canExtract or canInsert to the wrapper used as capability. You may call the supplier
     * inside the canExtract and canInsert lambda.
     */
    protected IItemHandler createItemHandler(Supplier<IItemHandlerModifiable> inventory) {
        return new FilterItemHandler(inventory.get(), this.getExtracts(inventory), this.getInserts(inventory));
    }

    protected abstract Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory);

    protected BiPredicate<Integer, ItemStack> getInserts(Supplier<IItemHandlerModifiable> inventory) {
        return null;
    }

    @Nonnull
    public abstract BaseItemStackHandler getInventory();

    public abstract int getComparatorOutput();

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
        }
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.getInventory().deserializeNBT(registries, tag.getCompound(TileTags.INVENTORY));
        this.mana = tag.getInt(TileTags.MANA);
        if (tag.contains(TileTags.INPUT_KEY)) this.inputKey = tag.getString(TileTags.INPUT_KEY);
        if (tag.contains(TileTags.OUTPUT_KEY)) this.outputKey = tag.getString(TileTags.OUTPUT_KEY);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(TileTags.INVENTORY, this.getInventory().serializeNBT(registries));
        nbt.putInt(TileTags.MANA, this.getCurrentMana());
        nbt.putString(TileTags.INPUT_KEY, this.inputKey);
        nbt.putString(TileTags.OUTPUT_KEY, this.outputKey);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider registries) {
        if (this.level != null && !this.level.isClientSide) return;
        this.getInventory().deserializeNBT(registries, tag.getCompound(TileTags.INVENTORY));
        this.mana = tag.getInt(TileTags.MANA);
        if (tag.contains(TileTags.INPUT_KEY)) this.inputKey = tag.getString(TileTags.INPUT_KEY);
        if (tag.contains(TileTags.OUTPUT_KEY)) this.outputKey = tag.getString(TileTags.OUTPUT_KEY);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        if (this.level != null && this.level.isClientSide) return super.getUpdateTag(registries);
        CompoundTag nbt = super.getUpdateTag(registries);
        nbt.put(TileTags.INVENTORY, this.getInventory().serializeNBT(registries));
        nbt.putInt(TileTags.MANA, this.getCurrentMana());
        nbt.putString(TileTags.INPUT_KEY, this.inputKey);
        nbt.putString(TileTags.OUTPUT_KEY, this.outputKey);
        return nbt;
    }

    @Override
    public void renderHUD(GuiGraphics guiGraphics, Minecraft minecraft) {
        ItemStack block = new ItemStack(this.getBlockState().getBlock());
        String name = block.getHoverName().getString();
        int centerX = minecraft.getWindow().getGuiScaledWidth() / 2;
        int centerY = minecraft.getWindow().getGuiScaledHeight() / 2;
        int width = Math.max(102, minecraft.font.width(name)) + 4;
        RenderHelper.renderHUDBox(guiGraphics, centerX - width / 2, centerY + 8, centerX + width / 2, centerY + 28);
        HUDHandler.drawSimpleManaHUD(guiGraphics, 0x4444FF, this.getCurrentMana(), this.getMaxMana(), name);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.disableBlend();
    }

    @Override
    public String getInputKey() {
        return this.inputKey;
    }

    @Override
    public String getOutputKey() {
        return this.outputKey;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return this.actAsMana();
    }

    @Override
    public int getAvailableSpaceForMana() {
        if (!this.actAsMana()) return 0;
        return Math.max(Math.max(0, this.getMaxMana() - this.getCurrentMana()), 0);
    }


    @Override
    public ManaSpark getAttachedSpark() {
        if (!this.actAsMana()) return null;
        //noinspection ConstantConditions
        List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above()), Predicates.instanceOf(SparkEntity.class));
        if (sparks.size() == 1) {
            Entity entity = sparks.get(0);
            return (ManaSpark) entity;
        }
        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return !this.actAsMana();
    }

    @Override
    public boolean isFull() {
        return this.getCurrentMana() >= this.getMaxMana();
    }

    @Override
    public void receiveMana(int i) {
        int old = this.getCurrentMana();
        this.mana = Math.max(0, Math.min(this.getCurrentMana() + i, this.getMaxMana()));
        if (old != this.getCurrentMana()) {
            this.setChanged();
            this.setDispatchable();
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        return this.mana;
    }

    @Override
    public int getMaxMana() {
        return this.manaCap;
    }

    @Override
    public boolean isOutputtingPower() {
        return false;
    }

    @Override
    public Optional<DyeColor> getColor() {
        return Optional.empty();
    }

    @Override
    public void setColor(Optional<DyeColor> color) {
        // unused
    }

    @Override
    public void markDispatchable() {
        this.setDispatchable();
    }

    @Override
    public Level getManaReceiverLevel() {
        return this.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    public boolean actAsMana() {
        return true;
    }
}
