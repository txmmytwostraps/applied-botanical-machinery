package appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalManaInfuser;

import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaInfuser.BlockEntityManaInfuserBase;
import net.minecraft.client.renderer.MultiBufferSource;
import appliedbotanicalmachinery.client.LegacyRotatedRenderer;

import javax.annotation.Nonnull;

public class RenderManaInfuserBase extends LegacyRotatedRenderer<BlockEntityManaInfuserBase> {
    public RenderManaInfuserBase() {
    }

    protected void doRender(@Nonnull BlockEntityManaInfuserBase tile, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {

    }

}
