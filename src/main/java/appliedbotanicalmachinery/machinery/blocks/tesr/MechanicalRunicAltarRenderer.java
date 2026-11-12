package appliedbotanicalmachinery.machinery.blocks.tesr;

import net.minecraft.client.renderer.SubmitNodeCollector;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import appliedbotanicalmachinery.client.LegacyRotatedRenderer;
import velrondevs.botania.client.core.handler.ClientTickHandler;

import javax.annotation.Nonnull;

public class MechanicalRunicAltarRenderer extends LegacyRotatedRenderer<BlockEntityMechanicalRunicAltar> {
    
    @Override
    protected void doRender(@Nonnull BlockEntityMechanicalRunicAltar tile, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        if (!LibXClientConfig.AdvancedRendering.all || !LibXClientConfig.AdvancedRendering.mechanicalRunicAltar)
            return;

        ItemStack livingRockStack = Inventories.get(tile.getInventory(), 0);
        if (!livingRockStack.isEmpty() && livingRockStack.getItem() instanceof BlockItem) {
            BlockState state = ((BlockItem) livingRockStack.getItem()).getBlock().defaultBlockState();

            poseStack.pushPose();
            poseStack.scale(1 / 16f, 1 / 16f, 1 / 16f);
            poseStack.translate(6.5, 10, 6.5);
            poseStack.scale(3, 3, 3);

            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-(ClientTickHandler.ticksInGame + partialTicks)));
            poseStack.translate(-0.5, 0, -0.5);

            //noinspection deprecation
            velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(state, poseStack, buffer, null, -1, 200, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        }

        double progressLeft = 1 - ((tile.getProgress() / (double) tile.getMaxProgress()) * 0.9);

        int items = 0;
        for (int slot = 1; slot < 17; slot++) {
            if (!Inventories.get(tile.getInventory(), slot).isEmpty())
                items += 1;
        }

        float[] angles = new float[items];
        float anglePer = 360f / items;
        float totalAngle = 0;
        for (int i = 0; i < angles.length; i++) {
            angles[i] = totalAngle += anglePer;
        }

        float time = ClientTickHandler.ticksInGame + partialTicks;

        int nextAngleIdx = 0;
        for (int slot = 1; slot < 17; slot++) {
            if (!Inventories.get(tile.getInventory(), slot).isEmpty()) {
                double travelCenter = 1;
                boolean shrink = false;
                if (tile.isSlotUsedCurrently(slot)) {
                    travelCenter = progressLeft;
                } else if (tile.getProgress() > 0) {
                    shrink = true;
                }

                int angleIdx = nextAngleIdx++;
                if (angleIdx >= angles.length)
                    break;

                poseStack.pushPose();
                poseStack.translate(0.5, 10.8 / 16d, 0.5);
                poseStack.scale(0.3f, 0.3f, 0.3f);
                poseStack.mulPose(Axis.YP.rotationDegrees(angles[angleIdx] + time));
                poseStack.translate(travelCenter * 1.125, 0, travelCenter * 0.25);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
                poseStack.translate(0, 0.075 * Math.sin((time + (angleIdx * 10)) / 5d), 0);
                if (shrink) {
                    poseStack.scale(0.3f, 0.3f, 0.3f);
                }

                ItemStack stack = Inventories.get(tile.getInventory(), slot);
                velrondevs.botania.client.core.helper.RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, tile.getLevel(), (int) tile.getBlockPos().asLong() + slot);

                poseStack.popPose();
            }
        }
    }
}
