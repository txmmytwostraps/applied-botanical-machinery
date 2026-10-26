package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalIndustrialAgglomerationFactory.ContainerIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalIndustrialAgglomerationFactory.BlockEntityIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.core.helper.RenderHelper;

import javax.annotation.Nonnull;

public class ScreenIndustrialAgglomerationFactoryBase extends ExtraScreenBase<ContainerIndustrialAgglomerationFactoryBase> {

    BlockEntityIndustrialAgglomerationFactoryBase blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.BASE);
    Bars bars;

    public ScreenIndustrialAgglomerationFactoryBase(ContainerIndustrialAgglomerationFactoryBase menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);

        this.imageWidth = ContainerIndustrialAgglomerationFactoryBase.WIDTH_GUI;
        this.imageHeight = ContainerIndustrialAgglomerationFactoryBase.HEIGHT_GUI;

        bars.setBar(AllBars.MANA);
        bars.setDrawCoord(33, 109);

        blockEntity = this.menu.getBlockEntity();
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.BASE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, null);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "text.appliedbotanicalmachinery.base_industrial_agglomeration_factory_label_text_1",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 5);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "text.appliedbotanicalmachinery.base_industrial_agglomeration_factory_label_text_2",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 13);

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min((float)blockEntity.getProgress() / (float)blockEntity.getMaxProgress(), 1.0F);
            int height = Math.round(16.0F * pct);

            RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.BASE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI, this.leftPos + 78, this.topPos + 57, this.imageWidth, 0, 40, height);
        }

        this.bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
    }
}
