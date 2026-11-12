package appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalManaPool;

import net.minecraft.client.renderer.SubmitNodeCollector;

import appliedbotanicalmachinery.util.Inventories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolUpgraded;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import appliedbotanicalmachinery.client.LegacyRotatedRenderer;
import velrondevs.botania.api.mana.PoolOverlayProvider;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.client.core.helper.RenderHelper;

import javax.annotation.Nonnull;

public class RenderManaPoolUpgraded extends LegacyRotatedRenderer<BlockEntityManaPoolUpgraded> {
    public static final double INNER_POOL_HEIGHT = 0.28125;
    public static final double POOL_BOTTOM_HEIGHT = 0.071875;
    private static final TextureAtlasSprite MANA_WATER_SPRITE;

    public RenderManaPoolUpgraded() {
    }

    protected void doRender(@Nonnull BlockEntityManaPoolUpgraded tile, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, @Nonnull SubmitNodeCollector collector, int light, int overlay) {
        if (LibXClientConfig.RenderingVisualContent.all && LibXClientConfig.RenderingVisualContent.ManaPoolSettings.manaPoolUpgraded) {
            ItemStack catalystStack = Inventories.get(tile.getInventory(), 0);
            if (!catalystStack.isEmpty() && catalystStack.getItem() instanceof BlockItem) {
                Block var9 = ((BlockItem)catalystStack.getItem()).getBlock();
                if (var9 instanceof PoolOverlayProvider) {
                    PoolOverlayProvider catalyst = (PoolOverlayProvider)var9;
                    Identifier spriteId = catalyst.getIcon(tile.getLevel(), tile.getBlockPos());
                    TextureAtlasSprite sprite = velrondevs.botania.client.core.helper.RenderHelper.blockSprite(spriteId);
                    poseStack.pushPose();
                    poseStack.translate(0.125, POOL_BOTTOM_HEIGHT, 0.125);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    poseStack.scale(0.0625F, 0.0625F, 0.0625F);
                    float alpha = (float)((Math.sin((double)((float) ClientTickHandler.ticksInGame + partialTick) / 20.0) + 1.0) * 0.3 + 0.2);
                    VertexConsumer vertex = buffer.getBuffer(RenderHelper.ICON_OVERLAY);
                    RenderHelper.renderIconFullBright(poseStack, vertex, sprite, alpha);
                    poseStack.popPose();
                }
            }

            if (tile.getCurrentMana() > 0) {
                double amount = (double)tile.getCurrentMana() / (double)tile.getMaxMana();
                poseStack.pushPose();
                poseStack.translate(0.1875, POOL_BOTTOM_HEIGHT + amount * INNER_POOL_HEIGHT, 0.1875);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.scale(0.625F, 0.625F, 0.625F);
                VertexConsumer vertex = buffer.getBuffer(RenderHelper.MANA_POOL_WATER);
                RenderHelper.renderIconFullBright(poseStack, vertex, MANA_WATER_SPRITE, 1.0F);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.translate(0.3125, 0.4375, 0.5);

            for(int i = 7; i < 11; i++){
                ItemStack output = Inventories.get(tile.getInventory(), i);

                if (!output.isEmpty()) {
                    poseStack.pushPose();
                    float z = 0;

                    if (i > 8){
                        z = 0.1f;
                    }
                    poseStack.translate(0, 0.0, -0.2 + 0.1 * (i - 7) + z);

                    poseStack.scale(0.4375F, 0.4375F, 0.4375F);
                    poseStack.mulPose(Axis.YP.rotationDegrees((float)ClientTickHandler.ticksInGame + partialTick));
                    velrondevs.botania.client.core.helper.RenderHelper.renderItem(output, ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, tile.getLevel(), (int)tile.getBlockPos().asLong());
                    poseStack.popPose();
                }
            }

            poseStack.translate(0.375, 0.0, 0.0);

            int slot = 1;
            for(int i = 1; i < 3; i++){
                for (int j = 1; j < 4; j++){
                    ItemStack input = Inventories.get(tile.getInventory(), slot);

                    if (!input.isEmpty()) {
                        poseStack.pushPose();
                        poseStack.translate(-0.05 + 0.1 * (i - 1), 0.0, -0.2 + 0.2 * (j - 1));

                        poseStack.scale(0.4375F, 0.4375F, 0.4375F);
                        poseStack.mulPose(Axis.YP.rotationDegrees((float)(ClientTickHandler.ticksInGame % 360)));
                        velrondevs.botania.client.core.helper.RenderHelper.renderItem(input, ItemDisplayContext.GROUND, poseStack, collector, light, OverlayTexture.NO_OVERLAY, tile.getLevel(), (int)tile.getBlockPos().asLong());
                        poseStack.popPose();
                    }
                    slot++;
                }
            }

            poseStack.popPose();

        }
    }

    static {
        MANA_WATER_SPRITE = velrondevs.botania.client.core.helper.RenderHelper.blockSprite(Identifier.fromNamespaceAndPath("botania", "block/mana_water"));
    }
}