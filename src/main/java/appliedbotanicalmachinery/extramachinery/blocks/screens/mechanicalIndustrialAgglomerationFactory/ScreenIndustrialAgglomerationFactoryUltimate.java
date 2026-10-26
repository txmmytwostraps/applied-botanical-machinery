package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory;

import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalIndustrialAgglomerationFactory.ContainerIndustrialAgglomerationFactoryUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalIndustrialAgglomerationFactory.BlockEntityIndustrialAgglomerationFactoryUltimate;
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
import velrondevs.botania.client.core.helper.RenderHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenIndustrialAgglomerationFactoryUltimate extends ExtraScreenBase<ContainerIndustrialAgglomerationFactoryUltimate> {

    BlockEntityIndustrialAgglomerationFactoryUltimate blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.ULTIMATE);
    Bars bars;
    SlotInfo slotInfo;

    public ScreenIndustrialAgglomerationFactoryUltimate(ContainerIndustrialAgglomerationFactoryUltimate menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerIndustrialAgglomerationFactoryUltimate.WIDTH_GUI;
        this.imageHeight = ContainerIndustrialAgglomerationFactoryUltimate.HEIGHT_GUI;

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(43, 128);

        blockEntity = this.menu.getBlockEntity();

        Map<Integer, int[]> slots = new HashMap<>();
        slots.put(0, new int[] {17, 69});
        slots.put(1, new int[] {183, 69});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] { "appliedbotanicalmachinery.tooltip.screen.upgrade_slot", "appliedbotanicalmachinery.tooltip.screen.upgrade_slot"});
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.ULTIMATE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "text.appliedbotanicalmachinery.ultimate_industrial_agglomeration_factory_label_text_1",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 5);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "text.appliedbotanicalmachinery.ultimate_industrial_agglomeration_factory_label_text_2",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 13);

        for (int i = 0; i < 2; i++){
            if (blockEntity.getInventory().getStackInSlot(i).isEmpty() && this.minecraft != null){
                List<ItemStack> items = new ArrayList<>();
                items.add(new ItemStack(ModItems.catalystManaInfinity));
                items.add(new ItemStack(ModItems.catalystSpeed));

                GhostItemRenderer.renderGhostItem(items, guiGraphics, this.leftPos + 17 + (166 * i), this.topPos + 69);
            }
        }

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min((float)blockEntity.getProgress() / (float)blockEntity.getMaxProgress(), 1.0F);
            int height = Math.round(16.0F * pct);

            RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.ULTIMATE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI, this.leftPos + 88, this.topPos + 69, this.imageWidth, 0, 40, height);
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
    }
}
