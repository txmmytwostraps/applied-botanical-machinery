package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalDaisy.ContainerDaisyAdvanced;
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

public class ScreenDaisyAdvanced extends ExtraScreenBase<ContainerDaisyAdvanced> {
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.ADVANCED);
    public ScreenDaisyAdvanced(ContainerDaisyAdvanced menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = ContainerDaisyAdvanced.WIDTH_GUI;
        this.imageHeight = ContainerDaisyAdvanced.HEIGHT_GUI;
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.ADVANCED_MECHANICAL_DAISY_GUI, screenAddInventory,
                new int[] {}, new int[] {}, null, null);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.advanced_daisy",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 5);
    }
}
