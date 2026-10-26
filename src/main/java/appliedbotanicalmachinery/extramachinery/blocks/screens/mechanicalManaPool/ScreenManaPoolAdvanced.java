package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool;

import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalManaPool.ContainerManaPoolAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolAdvanced;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ScreenManaPoolAdvanced extends ExtraScreenBase<ContainerManaPoolAdvanced> {

    BlockEntityManaPoolAdvanced blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.ADVANCED);
    Bars bars;
    SlotInfo slotInfo;

    public ScreenManaPoolAdvanced(ContainerManaPoolAdvanced menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerManaPoolAdvanced.WIDTH_GUI;
        this.imageHeight = ContainerManaPoolAdvanced.HEIGHT_GUI;

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(33, 102);

        blockEntity = this.menu.getBlockEntity();

        Map<Integer, int[]> slots = new HashMap<>();
        slots.put(0, new int[] {98, 72});
        slots.put(1, new int[] {98, 36});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] { "appliedbotanicalmachinery.tooltip.screen.catalyst_slot", "appliedbotanicalmachinery.tooltip.screen.upgrade_slot"});
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.ADVANCED_MECHANICAL_MANA_POOL_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.advanced_mana_pool",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        if (blockEntity.getInventory().getStackInSlot(0).isEmpty() && this.minecraft != null) {
            GhostItemRenderer.renderGhostItem(blockEntity.getCatalysts().stream().map(ItemStack::new).toList(), guiGraphics, this.leftPos + 98, this.topPos + 72);
        }

        if (blockEntity.getInventory().getStackInSlot(1).isEmpty() && this.minecraft != null){
            GhostItemRenderer.renderGhostItem(new ItemStack(ModItems.catalystManaInfinity), guiGraphics, this.leftPos + 98, this.topPos + 36);
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
    }
}
