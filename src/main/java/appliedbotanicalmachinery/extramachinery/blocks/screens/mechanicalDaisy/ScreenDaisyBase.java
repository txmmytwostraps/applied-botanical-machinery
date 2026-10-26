package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalAlfheimMarket.ContainerAlfheimMarketBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalDaisy.ContainerDaisyBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ScreenDaisyBase extends ExtraScreenBase<ContainerDaisyBase> {
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.BASE);

    public ScreenDaisyBase(ContainerDaisyBase menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = ContainerAlfheimMarketBase.WIDTH_GUI;
        this.imageHeight = ContainerAlfheimMarketBase.HEIGHT_GUI;
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.BASE_MECHANICAL_DAISY_GUI, screenAddInventory,
                new int[] {}, new int[] {}, null, null);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.base_daisy",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 5);
    }
}
