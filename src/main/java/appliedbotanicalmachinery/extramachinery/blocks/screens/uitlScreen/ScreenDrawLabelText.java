package appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen;

import net.minecraft.util.ARGB;

import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class ScreenDrawLabelText {

    public static void drawLabelText(GuiGraphicsExtractor guiGraphics, Font font, String idTextTranslatable, int[] pos, int[] image, int offsetY) {
        if (!LibXServerConfig.nameMechanism || !LibXClientConfig.nameMechanism) return;

        Component titleText = Component.translatable(idTextTranslatable);
        float scale = calculateOptimalScale(font, titleText, image[0] - 20);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.text(font, titleText, (int) ((pos[0] + image[0] / 2 - font.width(titleText) * scale / 2) / scale), (int) ((pos[1] + offsetY) / scale), ARGB.opaque(0x00), false);
        guiGraphics.pose().popMatrix();
    }

    private static float calculateOptimalScale(Font font, Component text, int maxWidth) {
        int textWidth = font.width(text);
        if (textWidth <= maxWidth) {
            return 1.0f;
        }
        return (float) maxWidth / textWidth;
    }
}
