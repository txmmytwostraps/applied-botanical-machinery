package appliedbotanicalmachinery.extramachinery.blocks.screens;

import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.ContainerJadedAmaranthus;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityJadedAmaranthus;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenJadedAmaranthus extends ExtraScreenBase<ContainerJadedAmaranthus> {
    BlockEntityJadedAmaranthus blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.OTHER);
    List<ItemStack> items = new ArrayList<>();
    Bars bars;
    SlotInfo slotInfo;


    public ScreenJadedAmaranthus(ContainerJadedAmaranthus menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerJadedAmaranthus.WIDTH_GUI;
        this.imageHeight = ContainerJadedAmaranthus.HEIGHT_GUI;

        blockEntity = this.menu.getBlockEntity();

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(33, 102);

        Map<Integer, int[]> slots = new HashMap<>();

        slots.put(0, new int[]{72, 23});
        slots.put(1, new int[]{108, 23});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] { "appliedbotanicalmachinery.tooltip.screen.upgrade_slot", "appliedbotanicalmachinery.tooltip.screen.upgrade_slot" });

        items.add(new ItemStack(ModItems.catalystManaInfinity));
        items.add(new ItemStack(ModItems.catalystPetal));
        items.add(new ItemStack(ModItems.catalystPetalBlock));
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.JADED_AMARANTHUS_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.jaded_amaranthus",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        for (int i = 0; i < 2; i++) {
            if (blockEntity.getInventory().getStackInSlot(i).isEmpty() && this.minecraft != null) {
                GhostItemRenderer.renderGhostItem(items, guiGraphics, this.leftPos + 72 + 36 * i, this.topPos + 23);
            }
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
    }
}
