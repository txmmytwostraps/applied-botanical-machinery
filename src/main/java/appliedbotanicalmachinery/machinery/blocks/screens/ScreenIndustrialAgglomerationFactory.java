package appliedbotanicalmachinery.machinery.blocks.screens;

import appliedbotanicalmachinery.machinery.blocks.base.ScreenBase;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.core.LibResources;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class ScreenIndustrialAgglomerationFactory extends ScreenBase<ContainerMenuIndustrialAgglomerationFactory> {

    public ScreenIndustrialAgglomerationFactory(ContainerMenuIndustrialAgglomerationFactory menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 195;
        this.manaBar.x -= 5;
        this.manaBar.y += 23;
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.INDUSTRIAL_AGGLOMERATION_FACTORY_GUI);
        BlockEntityIndustrialAgglomerationFactory blockEntity = this.menu.getBlockEntity();

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min(blockEntity.getProgress() / (float) blockEntity.getMaxProgress(), 1.0F);
            int height = Math.round(25 * pct);
            velrondevs.botania.client.core.helper.RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.INDUSTRIAL_AGGLOMERATION_FACTORY_GUI, this.relX + 73, this.relY + 76 - height, 176, 25 - height, 30, height);
        }
    }
}
