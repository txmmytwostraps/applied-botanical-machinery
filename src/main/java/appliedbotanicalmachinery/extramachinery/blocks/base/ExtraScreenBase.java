package appliedbotanicalmachinery.extramachinery.blocks.base;

import net.minecraft.client.renderer.RenderPipelines;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nonnull;

import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityGreenhouse;
import appliedbotanicalmachinery.extramachinery.gui.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.moddingx.libx.menu.BlockEntityMenu;


public abstract class ExtraScreenBase<X extends BlockEntityMenu<?>> extends AbstractContainerScreen<X> {
    public ExtraScreenBase(X menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    public ExtraScreenBase(X menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
        super(menu, inventory, title, imageWidth, imageHeight);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (width - this.imageWidth) / 2;
        // Center the entire GUI (machine + appended player inventory) once during init.
        // This avoids changing topPos every frame, which can confuse overlay mods like EMI.
        int effectiveHeight = this.imageHeight;
        ScreenAddInventory addInv = getAddInventory();
        if (addInv != null) {
            effectiveHeight = addInv.getHeightGuiMax(this.imageHeight);
        }
        this.topPos = (height - effectiveHeight) / 2;

        this.inventoryLabelY = -9999;
        this.titleLabelY = -9999;
    }

    public void extractRenderState(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void drawDefaultGuiBackgroundLayer(GuiGraphicsExtractor guiGraphics, Identifier screenLocation,
                                              ScreenAddInventory addInventory, int[] storageAll, int[] maxStorageAll,
                                              Bars bars, SlotInfo slotInfo) {
        if (bars != null){
            bars.setGuiCoord(this.leftPos, this.topPos);
        }
        if (slotInfo != null){
            slotInfo.setGuiCoord(this.leftPos, this.topPos);
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, screenLocation, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, addInventory.getInventoryScreen().getResourceLocation(), addInventory.getXBlitScreen(this.leftPos, addInventory.getWidthGuiMax(this.imageWidth)), addInventory.getYBlitScreen(this.topPos, addInventory.getHeightGuiMax(this.imageHeight)), 0, 0, addInventory.getInventoryScreen().getWidth(), addInventory.getInventoryScreen().getHeight(), 256, 256);

        if (bars != null && storageAll.length != 0 && maxStorageAll.length != 0){
            bars.draw(guiGraphics, storageAll, maxStorageAll, checkUpgradeInfinityMana(slotInfo));
        }
    }

    // Subclasses can override to provide their ScreenAddInventory so the
    // base class can center the full GUI correctly during init.
    protected ScreenAddInventory getAddInventory() {
        return null;
    }

    public boolean checkUpgradeInfinityMana(SlotInfo slotInfo){
        if (slotInfo == null) return false;
        BlockEntity blockEntity = ((BlockEntityMenu<?>)this.menu).getBlockEntity();

        assert blockEntity != null;
        if (blockEntity instanceof ExtraBotanicalTile && !(blockEntity instanceof BlockEntityGreenhouse)) {
            for (Integer slot: slotInfo.getSlotUpgrade()){
                if (Inventories.get(((ExtraBotanicalTile) blockEntity).getInventory(), slot).getItem() == ModItems.catalystManaInfinity ||
                        Inventories.get(((ExtraBotanicalTile) blockEntity).getInventory(), slot).getItem() == ModItems.catalystWaterInfinity){
                    return true;
                }
            }
        }
        return false;
    }
}
