package appliedbotanicalmachinery.extramachinery.blocks.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.ContainerGreenhouse;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityGreenhouse;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenGreenhouse extends ExtraScreenBase<ContainerGreenhouse> {

    BlockEntityGreenhouse blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.ULTIMATE);
    Bars bars;
    SlotInfo slotInfo;


    public ScreenGreenhouse(ContainerGreenhouse menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerGreenhouse.WIDTH_GUI;
        this.imageHeight = ContainerGreenhouse.HEIGHT_GUI;

        Map<Integer, int[]> slots = new HashMap<>();
        String[] translates = new String[8];

        for (int i = 0; i < 4; i++){
            slots.put(i, new int[]{22, 34 + i * 20});
            slots.put(i + 4, new int[]{178, 34 + i * 20});
            translates[i] = "appliedbotanicalmachinery.tooltip.screen.upgrade_slot";
            translates[i + 4] = "appliedbotanicalmachinery.tooltip.screen.upgrade_slot";
        }

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(translates);

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(43, 104);

        bars.setBar(AllBars.ENERGY);
        bars.setDrawCoord(43, 114);

        bars.setBar(AllBars.HEAT);
        bars.setBar(AllBars.HEAT);
        bars.setDrawCoord(10, 60);
        bars.setDrawCoord(202, 60);

        blockEntity = this.menu.getBlockEntity();
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.GREENHOUSE_GUI, screenAddInventory,
                new int[] { blockEntity.getCurrentMana(), blockEntity.getEnergyStored(), blockEntity.getCountHeat(), blockEntity.getCountHeat() },
                new int[] { blockEntity.getMaxMana(), blockEntity.getMaxEnergyStored(), blockEntity.getMaxCountHeat(), blockEntity.getMaxCountHeat() },
                bars, slotInfo
        );

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.greenhouse",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        for (int i = 0; i < blockEntity.getUpgradeInventory().getSlots(); i++){
            if (this.minecraft != null && blockEntity.getUpgradeInventory().getStackInSlot(i).isEmpty()) {
                int x;
                int y;

                if (i < 4){
                    x = 22;
                    y = 34 + 20 * i;
                } else {
                    x = 178;
                    y = 34 + 20 * (i - 4);
                }

                GhostItemRenderer.renderGhostItem(blockEntity.getUpgradeSlot(i).stream().map(ItemStack::new).toList(), guiGraphics, this.leftPos + x, this.topPos + y);
            }
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getUpgradeInventory());

        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getEnergyStored(), blockEntity.getMaxEnergyStored(), 1);
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCountHeat(), blockEntity.getMaxCountHeat(), 2);
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCountHeat(), blockEntity.getMaxCountHeat(), 3);
    }
}
