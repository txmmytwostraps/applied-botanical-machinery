package appliedbotanicalmachinery.machinery.blocks.base;

import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.util.ARGB;

import com.mojang.blaze3d.systems.RenderSystem;
import appliedbotanicalmachinery.machinery.gui.ManaBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.moddingx.libx.menu.BlockEntityMenu;

import javax.annotation.Nonnull;
import java.awt.Color;

public abstract class ScreenBase<X extends BlockEntityMenu<?>> extends AbstractContainerScreen<X> {

    public final ManaBar manaBar;
    public int relX;
    public int relY;

    public ScreenBase(X menu, Inventory inventory, Component title) {
        this(menu, inventory, title, 176, 166);
    }

    public ScreenBase(X menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
        super(menu, inventory, title, imageWidth, imageHeight);
        if (this.menu.getBlockEntity() instanceof BotanicalTile botanicalTile) {
            this.manaBar = new ManaBar(this, botanicalTile.getMaxMana());
        } else {
            this.manaBar = new ManaBar(this, 0);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.relX = (this.width - this.imageWidth) / 2;
        this.relY = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void extractRenderState(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.manaBar.guiTop = this.topPos;
        this.manaBar.guiLeft = this.leftPos;
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        if (this.menu.getBlockEntity() instanceof BotanicalTile botanicalTile) {
            this.manaBar.renderHoveredToolTip(guiGraphics, mouseX, mouseY, botanicalTile.getCurrentMana());
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        String s = this.title.getString();
        guiGraphics.text(this.font, s, (int) (this.imageWidth / 2 - this.font.width(s) / 2), 6, ARGB.opaque(Color.DARK_GRAY.getRGB()), false);
        guiGraphics.text(this.font, this.playerInventoryTitle.getString(), 8, (int) (this.imageHeight - 96 + 2), ARGB.opaque(Color.DARK_GRAY.getRGB()), false);
    }

    public void drawDefaultGuiBackgroundLayer(GuiGraphicsExtractor guiGraphics, Identifier screenLocation) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, screenLocation, this.relX, this.relY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.getBlockEntity() instanceof BotanicalTile botanicalTile) {
            this.manaBar.draw(guiGraphics, botanicalTile.getCurrentMana());
        }
    }
}
