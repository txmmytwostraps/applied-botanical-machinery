package appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalAlfheimMarket;

import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import appliedbotanicalmachinery.client.LegacyRotatedRenderer;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nonnull;
import java.util.Objects;

public class RenderAlpheimMarketUpgraded extends LegacyRotatedRenderer<BlockEntityAlfheimMarketUpgraded> {
    private final TextureAtlasSprite portalSprite;
    public RenderAlpheimMarketUpgraded() {
        this.portalSprite = velrondevs.botania.client.core.helper.RenderHelper.blockSprite(ResourceLocationHelper.prefix("block/alfheim_portal_swirl"));
    }

    public void doRender(@Nonnull BlockEntityAlfheimMarketUpgraded tile, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        if (LibXClientConfig.RenderingVisualContent.all && LibXClientConfig.RenderingVisualContent.AlfheimMarketSettings.alfheimMarketUpgraded) {
            poseStack.pushPose();
            poseStack.scale(0.0625F, 0.0625F, 0.0625F);
            poseStack.translate(3.2, 2.0, 3.6);
            poseStack.scale(3.6F, 3.6F, 3.6F);
            velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(BotaniaBlocks.naturaPylon.defaultBlockState(), poseStack, buffer, null, -1, light, overlay);
            poseStack.translate(1.5555555555555556, 0.0, 0.0);
            velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(BotaniaBlocks.naturaPylon.defaultBlockState(), poseStack, buffer, null, -1, light, overlay);
            poseStack.popPose();
            if (tile.getCurrentMana() > 0) {
                poseStack.pushPose();
                poseStack.scale(0.0625F, 0.0625F, 0.0625F);
                poseStack.translate(6.8, 1.0, 8.8);
                poseStack.scale(2.4F, 2.4F, 2.4F);
                poseStack.translate(-1.0, 1.0, 0.25);
                float alpha = (float)Math.min(1.0, (Math.sin((double)((float) ClientTickHandler.ticksInGame + partialTick) / 8.0) + 1.0) / 7.0 + 0.7);
                this.renderPortal(poseStack, buffer, this.portalSprite, 0, 0, 3, 3, alpha, overlay);
                poseStack.translate(3.0, 0.0, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                this.renderPortal(poseStack, buffer, this.portalSprite, 0, 0, 3, 3, alpha, overlay);
                poseStack.popPose();
            }

            if (tile.getProgress() > 0) {
                double progress = ((double)tile.getProgress() - (double)tile.getMaxProgress() / 2.0) / ((double)tile.getMaxProgress() / 2.0);
                ItemStack stack = progress < 0.0 ? tile.getCurrentInput() : tile.getCurrentOutput();
                if (!stack.isEmpty()) {
                    double yPos = Math.pow(progress, 2.0);
                    double zPos = -(progress * 0.75);
                    poseStack.pushPose();
                    poseStack.scale(0.0625F, 0.0625F, 0.0625F);
                    poseStack.translate(8.0, 4.6, 8.8);
                    poseStack.scale(5.4F, 5.4F, 5.4F);
                    poseStack.translate(0.0, yPos, zPos);
                    poseStack.scale(-1.0F, 1.0F, -1.0F);
                    poseStack.mulPose(this.camera.orientation);
                    velrondevs.botania.client.core.helper.RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, tile.getLevel(), (int)tile.getBlockPos().asLong());
                    poseStack.popPose();
                }
            }

        }
    }

    private void renderPortal(PoseStack poseStack, MultiBufferSource buffer, TextureAtlasSprite sprite, int x, int y, int width, int height, float alpha, int overlay) {
        VertexConsumer vertex = buffer.getBuffer(Sheets.translucentBlockSheet());
        PoseStack.Pose pose = poseStack.last();
        vertex.addVertex(pose, (float)x, (float)(y + height), 0.0F).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(sprite.getU0(), sprite.getV1()).setOverlay(overlay).setLight(LightCoordsUtil.pack(15, 15)).setNormal(pose, 1.0F, 0.0F, 0.0F);
        vertex.addVertex(pose, (float)(x + width), (float)(y + height), 0.0F).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(sprite.getU1(), sprite.getV1()).setOverlay(overlay).setLight(LightCoordsUtil.pack(15, 15)).setNormal(pose, 1.0F, 0.0F, 0.0F);
        vertex.addVertex(pose, (float)(x + width), (float)y, 0.0F).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(sprite.getU1(), sprite.getV0()).setOverlay(overlay).setLight(LightCoordsUtil.pack(15, 15)).setNormal(pose, 1.0F, 0.0F, 0.0F);
        vertex.addVertex(pose, (float)x, (float)y, 0.0F).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(sprite.getU0(), sprite.getV0()).setOverlay(overlay).setLight(LightCoordsUtil.pack(15, 15)).setNormal(pose, 1.0F, 0.0F, 0.0F);
    }
}
