package appliedbotanicalmachinery.machinery.blocks.screens;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.client.FluidRender;

import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.util.ARGB;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalDaisy;
import appliedbotanicalmachinery.machinery.core.LibResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class ScreenMechanicalDaisy extends AbstractContainerScreen<ContainerMenuMechanicalDaisy> {

    private static final Identifier PURE_DAISY_TEXTURE = Identifier.fromNamespaceAndPath("botania", "block/pure_daisy");

    public ScreenMechanicalDaisy(ContainerMenuMechanicalDaisy menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LibResources.MECHANICAL_DAISY_GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        String s = this.title.getString();
        guiGraphics.text(this.font, s, (int) (this.imageWidth / 2 - this.font.width(s) / 2), 6, ARGB.opaque(Color.DARK_GRAY.getRGB()), false);
        guiGraphics.text(this.font, this.playerInventoryTitle.getString(), 8, (int) (this.imageHeight - 96 + 2), ARGB.opaque(Color.DARK_GRAY.getRGB()), false);

        TextureAtlasSprite sprite = velrondevs.botania.client.core.helper.RenderHelper.blockSprite(PURE_DAISY_TEXTURE);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 12, 16, 48, 48);
    }

    @Override
    protected void extractSlot(@Nonnull GuiGraphicsExtractor guiGraphics, @Nonnull Slot slot, int mouseX, int mouseY) {
        if (slot instanceof ContainerMenuMechanicalDaisy.ItemAndFluidSlot) {
            FluidStack stack = ((ContainerMenuMechanicalDaisy.ItemAndFluidSlot) slot).inventory.getFluidInTank(slot.index);
            if (!stack.isEmpty() && stack.getFluid() != null) {
                int maxAmount = ((ContainerMenuMechanicalDaisy.ItemAndFluidSlot) slot).inventory.getTankCapacity(slot.index);
                int yHeight = Math.round(stack.getAmount() / (float) maxAmount) * 16;
                int yPos = slot.y + 16 - yHeight;
                TextureAtlasSprite sprite = FluidRender.stillSprite(stack);
                int fluidColor = FluidRender.tint(stack);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, slot.x, yPos, 16, yHeight, fluidColor);
            }
        }

        super.extractSlot(guiGraphics, slot, mouseX, mouseY);
    }

    @Override
    protected void extractTooltip(@Nonnull GuiGraphicsExtractor guiGraphics, int x, int y) {
        //noinspection ConstantConditions
        if (this.minecraft.player.containerMenu.getCarried().isEmpty() && this.hoveredSlot != null) {
            if (this.hoveredSlot instanceof ContainerMenuMechanicalDaisy.ItemAndFluidSlot
                    && Inventories.get(((ContainerMenuMechanicalDaisy.ItemAndFluidSlot) this.hoveredSlot).inventory, this.hoveredSlot.index).isEmpty()
                    && !((ContainerMenuMechanicalDaisy.ItemAndFluidSlot) this.hoveredSlot).inventory.getFluidInTank(this.hoveredSlot.index).isEmpty()) {

                FluidStack stack = ((ContainerMenuMechanicalDaisy.ItemAndFluidSlot) this.hoveredSlot).inventory.getFluidInTank(this.hoveredSlot.index);
                List<Component> list = ImmutableList.of(
                        stack.getHoverName(),
                        Component.literal(stack.getAmount() + " / 1000")
                );

                guiGraphics.setComponentTooltipForNextFrame(this.font, list, x, y);
                return;
            }
        }

        super.extractTooltip(guiGraphics, x, y);
    }
}
