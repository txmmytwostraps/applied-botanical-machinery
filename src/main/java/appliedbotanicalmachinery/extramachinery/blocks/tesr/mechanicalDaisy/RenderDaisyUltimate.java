package appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalDaisy;

import net.minecraft.client.renderer.SubmitNodeCollector;

import appliedbotanicalmachinery.client.LegacyBlockRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalDaisy.BlockEntityDaisyUltimate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderDaisyUltimate extends LegacyBlockRenderer<BlockEntityDaisyUltimate> {
    public RenderDaisyUltimate() {
    }

    private void renderState(@Nullable BlockState state, float translateX, float translateZ, @Nonnull PoseStack poseStack,
                             @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (state != null) {
            poseStack.pushPose();
            poseStack.translate(translateX, 0.0, translateZ);
            velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(state, poseStack, buffer, null, -1, light, overlay);
            poseStack.popPose();
        }

    }

    @Override
    protected void render(BlockEntityDaisyUltimate blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {}
}