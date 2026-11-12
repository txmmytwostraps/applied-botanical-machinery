package appliedbotanicalmachinery.extramachinery.blocks.tesr;

import java.util.List;

import appliedbotanicalmachinery.extramachinery.events.ClientModEventSubscriber;

import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;

import net.minecraft.client.renderer.SubmitNodeCollector;

import appliedbotanicalmachinery.client.LegacyBlockRenderer;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityGreenhouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderGreenhouse extends LegacyBlockRenderer<BlockEntityGreenhouse> {

    private static final StandaloneModelKey<BlockStateModelPart>[] CIRCLES = List.of(
            ClientModEventSubscriber.GREENHOUSE_CIRCLE_3,
            ClientModEventSubscriber.GREENHOUSE_CIRCLE_2,
            ClientModEventSubscriber.GREENHOUSE_CIRCLE_1).toArray(StandaloneModelKey[]::new);

    private static BlockStateModelPart model(StandaloneModelKey<BlockStateModelPart> key) {
        return Minecraft.getInstance().getModelManager().getStandaloneModel(key);
    }

    public RenderGreenhouse() {
    }

    private void renderState(@Nullable BlockState state, float translateX, float translateZ, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (state != null) {
            poseStack.pushPose();
            poseStack.translate(translateX, 0.0, translateZ);
            velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(state, poseStack, buffer, null, -1, light, overlay);
            poseStack.popPose();
        }

    }

    @Override
    protected void render(BlockEntityGreenhouse blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        float angle = (blockEntity.getLevel().getGameTime() + partialTick) % 360;
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.translate(-0.5, -0.5, -0.5);

        velrondevs.botania.client.core.helper.RenderHelper.renderQuads(poseStack.last(), buffer.getBuffer(Sheets.translucentBlockSheet()), velrondevs.botania.client.core.helper.RenderHelper.allQuads(model(ClientModEventSubscriber.GREENHOUSE_PYLONS)), -1, light, overlay);
        poseStack.popPose();

        float[] y_offset = new float[] {0.017f, 0.025f, 0.06f};

        for (int i = 0; i < 3; i++) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.1 - i * 0.01, 0.5);

            angle = (blockEntity.getLevel().getGameTime() + partialTick) % 360;
            poseStack.mulPose(Axis.YP.rotationDegrees(i == 1 ? -angle: angle));

            double phaseShift = i * (Math.PI / 2);
            double bounce = Math.sin((blockEntity.getLevel().getGameTime() + partialTick) * 0.05 + phaseShift) * 0.083f - y_offset[i];

            poseStack.translate(-0.5, bounce, -0.5);

            velrondevs.botania.client.core.helper.RenderHelper.renderQuads(poseStack.last(), buffer.getBuffer(Sheets.cutoutBlockSheet()), velrondevs.botania.client.core.helper.RenderHelper.allQuads(model(CIRCLES[i])), -1, light, overlay);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.05, 0.5);

        angle = (blockEntity.getLevel().getGameTime() + partialTick) % 360;
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));

        double bounce = Math.sin((blockEntity.getLevel().getGameTime() + partialTick) * 0.1) * 0.02;
        poseStack.translate(-0.5, bounce, -0.5);

        velrondevs.botania.client.core.helper.RenderHelper.renderQuads(poseStack.last(), buffer.getBuffer(Sheets.translucentBlockSheet()), velrondevs.botania.client.core.helper.RenderHelper.allQuads(model(ClientModEventSubscriber.GREENHOUSE_GARDEN_BED)), -1, light, overlay);
        poseStack.popPose();


        for (int i = 0; i < blockEntity.getFlowerInventory().size(); i++) {
            if (!Inventories.get(blockEntity.getFlowerInventory(), i).isEmpty()) {
                ItemStack flowerStack = Inventories.get(blockEntity.getFlowerInventory(), i);

                if (flowerStack.getItem() instanceof BlockItem blockItem) {
                    Block flowerBlock = blockItem.getBlock();

                    poseStack.pushPose();

                    poseStack.translate(0.4, 0.485 + bounce, 0.4);
                    poseStack.scale(0.2f, 0.2f, 0.2f);

                    double flowerOffsetX = Math.cos(i * Math.PI * 2 / blockEntity.getFlowerInventory().size()) * 0.7;
                    double flowerOffsetZ = Math.sin(i * Math.PI * 2 / blockEntity.getFlowerInventory().size()) * 0.7;

                    poseStack.translate(flowerOffsetX , 0, flowerOffsetZ);

                    // Рендерим модель блока
                    velrondevs.botania.client.core.helper.RenderHelper.renderBlockState(flowerBlock.defaultBlockState(), poseStack, buffer, null, -1, light, overlay);

                    poseStack.popPose();
                }
            }
        }
    }
}
