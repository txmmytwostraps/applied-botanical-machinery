package appliedbotanicalmachinery.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.core.helper.DeferredBufferSource;

/**
 * A block entity renderer that draws straight from the block entity. The buffer source records vertices and submits
 * them to the frame's node collector (Botania Revived's DeferredBufferSource).
 */
public abstract class LegacyBlockRenderer<T extends BlockEntity>
        implements BlockEntityRenderer<T, LegacyBlockRenderer.State<T>> {

    /**
     * The camera of the frame being drawn. Set before {@link #render} runs.
     */
    @Nullable
    protected CameraRenderState camera;

    @Override
    public State<T> createRenderState() {
        return new State<>();
    }

    @Override
    public void extractRenderState(@Nonnull T blockEntity, @Nonnull State<T> state, float partialTicks,
            @Nonnull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(@Nonnull State<T> state, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector collector,
            @Nonnull CameraRenderState camera) {
        T blockEntity = state.blockEntity;
        if (blockEntity == null || blockEntity.isRemoved()) {
            return;
        }
        this.camera = camera;
        poseStack.pushPose();
        try {
            this.render(blockEntity, state.partialTicks, poseStack, new DeferredBufferSource(collector), collector,
                    state.lightCoords, OverlayTexture.NO_OVERLAY);
        } finally {
            poseStack.popPose();
        }
    }

    protected abstract void render(@Nonnull T blockEntity, float partialTick, @Nonnull PoseStack poseStack,
            @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay);

    public static class State<T extends BlockEntity> extends BlockEntityRenderState {
        @Nullable
        public T blockEntity;
        public float partialTicks;
    }
}
