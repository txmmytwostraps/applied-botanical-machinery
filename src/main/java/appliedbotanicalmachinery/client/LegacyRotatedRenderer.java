package appliedbotanicalmachinery.client;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * A {@link LegacyBlockRenderer} that turns the pose stack to the block's horizontal facing first. The block entity is
 * drawn as if it faced north.
 */
public abstract class LegacyRotatedRenderer<T extends BlockEntity> extends LegacyBlockRenderer<T> {

    @Override
    protected final void render(@Nonnull T blockEntity, float partialTick, @Nonnull PoseStack poseStack,
            @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        BlockState state = blockEntity.getBlockState();
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            float rot = state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180;
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-rot));
            poseStack.translate(-0.5D, -0.5D, -0.5D);
        }
        this.doRender(blockEntity, partialTick, poseStack, buffer, collector, light, overlay);
    }

    protected abstract void doRender(@Nonnull T blockEntity, float partialTick, @Nonnull PoseStack poseStack,
            @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay);
}
