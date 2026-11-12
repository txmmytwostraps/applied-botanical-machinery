package appliedbotanicalmachinery.machinery.blocks.screens;

import appliedbotanicalmachinery.machinery.blocks.base.ScreenBase;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.core.LibResources;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class ScreenMechanicalManaInfuser extends ScreenBase<ContainerMenuMechanicalManaInfuser> {

    public ScreenMechanicalManaInfuser(ContainerMenuMechanicalManaInfuser menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 195);
        this.manaBar.x -= 5;
        this.manaBar.y += 23;
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.MECHANICAL_MANA_INFUSER_GUI);
        BlockEntityMechanicalManaInfuser blockEntity = this.menu.getBlockEntity();

        if (blockEntity.getProgress() > 0) {
            float pct = Math.min(blockEntity.getProgress() / (float) blockEntity.getMaxProgress(), 1.0F);
            int height = Math.round(25 * pct);
            velrondevs.botania.client.core.helper.RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.MECHANICAL_MANA_INFUSER_GUI, this.relX + 73, this.relY + 76 - height, 176, 25 - height, 30, height);
        }
    }
}
