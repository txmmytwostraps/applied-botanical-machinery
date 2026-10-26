package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalAlfheimMarket.ContainerAlfheimMarketBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalOrechid.ContainerOrechidBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalOrechid.BlockEntityOrechidBase;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class ScreenOrechidBase extends ExtraScreenBase<ContainerOrechidBase> {

    BlockEntityOrechidBase blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.BASE);
    Bars bars;
    SlotInfo slotInfo;

    public ScreenOrechidBase(ContainerOrechidBase menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerAlfheimMarketBase.WIDTH_GUI;
        this.imageHeight = ContainerAlfheimMarketBase.HEIGHT_GUI;

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(33, 104);

        blockEntity = this.menu.getBlockEntity();

        Map<Integer, int[]> slots = new HashMap<>();
        String[] infoTranslate = new String[9];

        for (int i = 0; i < 3; i++){
            slots.put(i, new int[] {72 + 18 * i, 22});
            infoTranslate[i] = ("appliedbotanicalmachinery.tooltip.screen.ore_slot");
        }

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(infoTranslate);
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.BASE_ORECHID_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.base_orechid",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);

    }
}
