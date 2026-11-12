package appliedbotanicalmachinery.machinery.blocks.tesr;

import net.minecraft.client.renderer.SubmitNodeCollector;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import appliedbotanicalmachinery.client.LegacyRotatedRenderer;
import velrondevs.botania.client.core.handler.ClientTickHandler;

import javax.annotation.Nonnull;


public class MechanicalManaInfuserRenderer extends LegacyRotatedRenderer<BlockEntityMechanicalManaInfuser> {

    public MechanicalManaInfuserRenderer() {}

    @Override
    protected void doRender(@Nonnull BlockEntityMechanicalManaInfuser tile, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        if (!LibXClientConfig.AdvancedRendering.all || !LibXClientConfig.AdvancedRendering.mechanicalManaInfuser)
            return;

        double progressLeft = 1 - (tile.getProgress() / (double) tile.getMaxProgress());

        this.renderStack(Inventories.get(tile.getInventory(), 0), poseStack, buffer, collector, partialTick, progressLeft, 0, light);
        this.renderStack(Inventories.get(tile.getInventory(), 1), poseStack, buffer, collector, partialTick, progressLeft, 120, light);
        this.renderStack(Inventories.get(tile.getInventory(), 2), poseStack, buffer, collector, partialTick, progressLeft, 240, light);

        if (!Inventories.get(tile.getInventory(), 3).isEmpty()) {
            float time = ClientTickHandler.ticksInGame + partialTick;

            poseStack.pushPose();
            poseStack.translate(0.5, 11.2 / 16d, 0.5);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-time));
            poseStack.translate(0, 0.075 * Math.sin(time / 5d), 0);

            velrondevs.botania.client.core.helper.RenderHelper.renderItem(Inventories.get(tile.getInventory(), 3), ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, tile.getLevel(), (int) tile.getBlockPos().asLong());

            poseStack.popPose();
        }

        if (tile.getProgress() > 0) {
            poseStack.pushPose();
            poseStack.translate(6.2 / 16, 4.7 / 16, 6.2 / 16);
            poseStack.scale(3.6f / 16, 3.6f / 16, 3.6f / 16);

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.translate(0, 0, -0.18850000202655792);

            poseStack.popPose();
        }
    }

    private void renderStack(ItemStack stack, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, float partialTick, double progressLeft, float angle, int light) {
        if (!stack.isEmpty()) {
            double progressLeftScaled = progressLeft * 0.95;
            float time = ClientTickHandler.ticksInGame + partialTick;

            poseStack.pushPose();
            poseStack.translate(0.5, (10.4 - (4 * progressLeft)) / 16d, 0.5);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle + time));
            poseStack.translate(progressLeftScaled * 1.125, 0, progressLeftScaled * 0.25);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            poseStack.translate(0, 0.075 * Math.sin((time + angle) / 5d), 0);

            velrondevs.botania.client.core.helper.RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().level, 0);

            poseStack.popPose();
        }
    }
}
