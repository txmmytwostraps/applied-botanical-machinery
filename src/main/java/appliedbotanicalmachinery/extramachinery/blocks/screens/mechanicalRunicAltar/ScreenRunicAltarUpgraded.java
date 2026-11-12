package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar;

import net.minecraft.client.renderer.RenderPipelines;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalRunicAltar.ContainerRunicAltarUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalRunicAltar.BlockEntityRunicAltarUpgraded;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import velrondevs.botania.registry.BotaniaBlocks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ScreenRunicAltarUpgraded extends ExtraScreenBase<ContainerRunicAltarUpgraded> {
    BlockEntityRunicAltarUpgraded blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.UPGRADE);
    Bars bars;
    SlotInfo slotInfo;

    public ScreenRunicAltarUpgraded(ContainerRunicAltarUpgraded menu, Inventory inventory, Component title) {
        super(menu, inventory, title, ContainerRunicAltarUpgraded.WIDTH_GUI, ContainerRunicAltarUpgraded.HEIGHT_GUI);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(43, 121);

        blockEntity = this.menu.getBlockEntity();

        Map<Integer, int[]> slots = new HashMap<>();
        slots.put(0, new int[] {81, 100});
        slots.put(1, new int[] {100, 100});
        slots.put(2, new int[] {119, 100});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] {
                "appliedbotanicalmachinery.tooltip.screen.livingrock_slot",
                "appliedbotanicalmachinery.tooltip.screen.livingrock_slot",
                "appliedbotanicalmachinery.tooltip.screen.livingrock_slot"
        });
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    public void extractBackground(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.UPGRADED_MECHANICAL_RUNIC_ALTAR_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.upgraded_runic_altar",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        for (int i = 0; i < 3; i++){
            if (Inventories.get(blockEntity.getInventory(), i).isEmpty() && this.minecraft != null) {
                GhostItemRenderer.renderGhostItem(new ItemStack(BotaniaBlocks.livingrock), guiGraphics, this.leftPos + 81 + i * 18 + i, this.topPos + 100);
            }
        }

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min((float)blockEntity.getProgress() / (float)blockEntity.getMaxProgress(), 1.0F);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LibResources.UPGRADED_MECHANICAL_RUNIC_ALTAR_GUI, this.leftPos + 102, this.topPos + 41, this.imageWidth, 0, Math.round(11.0F * pct), 37, 256, 256);
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);

    }
}
