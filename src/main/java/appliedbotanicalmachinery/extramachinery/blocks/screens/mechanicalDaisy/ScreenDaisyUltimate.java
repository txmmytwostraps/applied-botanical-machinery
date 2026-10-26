package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalDaisy.ContainerDaisyUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalDaisy.BlockEntityDaisyUltimate;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ScreenDaisyUltimate extends ExtraScreenBase<ContainerDaisyUltimate> {
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.ULTIMATE);
    BlockEntityDaisyUltimate blockEntity;
    SlotInfo slotInfo;

    public ScreenDaisyUltimate(ContainerDaisyUltimate menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerDaisyUltimate.WIDTH_GUI;
        this.imageHeight = ContainerDaisyUltimate.HEIGHT_GUI;

        Map<Integer, int[]> slots = new HashMap<>();
        slots.put(0, new int[] {166, 105});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] { "appliedbotanicalmachinery.tooltip.screen.upgrade_slot"});

        blockEntity = this.menu.getBlockEntity();
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.ULTIMATE_MECHANICAL_DAISY_GUI, screenAddInventory,
                new int[] {}, new int[] {}, null, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.ultimate_daisy",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 3);

        if (blockEntity.getInventoryUpgrade() != null && blockEntity.getInventoryUpgrade().getStackInSlot(0).isEmpty() && this.minecraft != null) {
            GhostItemRenderer.renderGhostItem(blockEntity.getUpgrades(), guiGraphics, this.leftPos + 166, this.topPos + 105);
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventoryUpgrade());
    }
}
