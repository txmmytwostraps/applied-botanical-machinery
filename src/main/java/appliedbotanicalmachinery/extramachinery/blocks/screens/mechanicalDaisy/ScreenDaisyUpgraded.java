package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalDaisy.ContainerDaisyUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ScreenDaisyUpgraded extends ExtraScreenBase<ContainerDaisyUpgraded> {
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.UPGRADE);
    public ScreenDaisyUpgraded(ContainerDaisyUpgraded menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = ContainerDaisyUpgraded.WIDTH_GUI;
        this.imageHeight = ContainerDaisyUpgraded.HEIGHT_GUI;
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.UPGRADED_MECHANICAL_DAISY_GUI, screenAddInventory,
                new int[] {}, new int[] {}, null, null);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.upgraded_daisy",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 5);
    }
}
