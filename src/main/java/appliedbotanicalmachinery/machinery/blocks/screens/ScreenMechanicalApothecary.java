package appliedbotanicalmachinery.machinery.blocks.screens;

import net.minecraft.core.HolderSet;

import net.neoforged.neoforge.fluids.FluidStack;

import appliedbotanicalmachinery.client.FluidRender;

import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.util.ARGB;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalApothecary;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalApothecary;
import appliedbotanicalmachinery.machinery.core.LibResources;
import appliedbotanicalmachinery.machinery.helper.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import org.moddingx.libx.render.RenderHelperFluid;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class ScreenMechanicalApothecary extends AbstractContainerScreen<ContainerMenuMechanicalApothecary> {

    private int relX;
    private int relY;
    private final BlockEntityMechanicalApothecary tile;

    public ScreenMechanicalApothecary(ContainerMenuMechanicalApothecary screenMenu, Inventory inventory, Component title) {
        super(screenMenu, inventory, title, 196, 195);
        this.relX = (this.width - this.imageWidth) / 2;
        this.relY = (this.height - this.imageHeight) / 2;
        this.tile = (BlockEntityMechanicalApothecary) this.menu.getLevel().getBlockEntity(this.menu.getPos());
    }

    @Override
    protected void init() {
        super.init();
        this.relX = (this.width - this.imageWidth) / 2;
        this.relY = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LibResources.MECHANICAL_APOTHECARY_GUI, this.relX, this.relY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if (Inventories.get(this.tile.getInventory(), 0).isEmpty()) {
            //noinspection DataFlowIssue,OptionalGetWithoutIsPresent
            List<ItemStack> items = this.minecraft.level.registryAccess().lookupOrThrow(Registries.ITEM).get(Tags.Items.SEEDS).stream().flatMap(HolderSet::stream).map(ItemStack::new).toList();
            GhostItemRenderer.renderGhostItem(items, guiGraphics, this.relX + 90, this.relY + 43);
        }

        if (this.tile.getProgress() > 0) {
            float pctProgress = Math.min(this.tile.getProgress() / (float) this.tile.getMaxProgress(), 1.0F);
            velrondevs.botania.client.core.helper.RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.MECHANICAL_APOTHECARY_GUI, this.relX + 87, this.relY + 64, this.imageWidth, 0, Math.round(22 * pctProgress), 16);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void extractLabels(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        String s = this.title.getString();
        guiGraphics.text(this.font, s, (int) (this.imageWidth / 2 - this.font.width(s) / 2), 6, ARGB.opaque(Color.DARK_GRAY.getRGB()), false);
        guiGraphics.text(this.font, this.playerInventoryTitle.getString(), 8, (int) (this.imageHeight - 96 + 2), ARGB.opaque(Color.DARK_GRAY.getRGB()), false);

        float pctFluid = Math.min((float) this.tile.getFluidInventory().getFluidAmount() / BlockEntityMechanicalApothecary.FLUID_CAPACITY, 1.0F);
        int xPos = 163;
        int ySize = Math.round(81 * pctFluid);
        int yPos = 16 + 81 - ySize;

        RenderHelperFluid.renderFluid(guiGraphics, FluidRender.tint(new FluidStack(Fluids.WATER, 1000)), xPos, yPos, 17, ySize);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LibResources.MECHANICAL_APOTHECARY_GUI, xPos, 16, this.imageWidth, 16, 17, 81, 256, 256);

    }

    @Override
    protected void extractTooltip(@Nonnull GuiGraphicsExtractor guiGraphics, int x, int y) {
        // x and y are screen coordinates. The tank area is relative to the GUI.
        if (x - this.leftPos >= 163 && x - this.leftPos <= 179 &&
                y - this.topPos >= 16 && y - this.topPos <= 96) {
            Component fluid = Component.translatable(this.tile.getFluidInventory().getFluidAmount() + " / " + this.tile.getFluidInventory().getCapacity() + " mB");
            guiGraphics.setTooltipForNextFrame(this.font, fluid, x, y);
        }
        super.extractTooltip(guiGraphics, x, y);
    }
}
