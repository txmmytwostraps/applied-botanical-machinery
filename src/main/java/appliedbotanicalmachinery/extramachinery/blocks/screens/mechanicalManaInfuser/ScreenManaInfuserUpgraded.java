package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaInfuser;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalManaInfuser.ContainerManaInfuserUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaInfuser.BlockEntityManaInfuserUpgraded;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.core.helper.RenderHelper;

import javax.annotation.Nonnull;

public class ScreenManaInfuserUpgraded extends ExtraScreenBase<ContainerManaInfuserUpgraded> {

    BlockEntityManaInfuserUpgraded blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.UPGRADE);
    Bars bars;

    public ScreenManaInfuserUpgraded(ContainerManaInfuserUpgraded menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        bars = new Bars(this);

        this.imageWidth = ContainerManaInfuserUpgraded.WIDTH_GUI;
        this.imageHeight = ContainerManaInfuserUpgraded.HEIGHT_GUI;

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
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.UPGRADED_MANA_INFUSER_GUI, screenAddInventory,
                new int[] {blockEntity.getCurrentMana()}, new int[] {blockEntity.getMaxMana()}, bars, null);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.upgraded_mana_infuser",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 10);

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min((float)blockEntity.getProgress() / (float)blockEntity.getMaxProgress(), 1.0F);
            int height = Math.round(16.0F * pct);

            RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.UPGRADED_MANA_INFUSER_GUI, this.leftPos + 78, this.topPos + 57, this.imageWidth, 0, 40, height);
        }

        this.bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getCurrentMana(), blockEntity.getMaxMana(), 0);
    }
}
