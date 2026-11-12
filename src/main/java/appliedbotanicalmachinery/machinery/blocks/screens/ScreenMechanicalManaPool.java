package appliedbotanicalmachinery.machinery.blocks.screens;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.machinery.blocks.base.ScreenBase;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalManaPool;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaPool;
import appliedbotanicalmachinery.machinery.core.LibResources;
import appliedbotanicalmachinery.machinery.helper.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ScreenMechanicalManaPool extends ScreenBase<ContainerMenuMechanicalManaPool> {

    public ScreenMechanicalManaPool(ContainerMenuMechanicalManaPool menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.MECHANICAL_MANA_POOL_GUI);

        BlockEntityMechanicalManaPool blockEntity = this.menu.getBlockEntity();
        if (Inventories.get(blockEntity.getInventory(), 0).isEmpty() && this.minecraft != null) {
            GhostItemRenderer.renderGhostItem(blockEntity.getCatalysts().stream().map(ItemStack::new).toList(), guiGraphics, this.relX + 53, this.relY + 47);
        }
    }
}
