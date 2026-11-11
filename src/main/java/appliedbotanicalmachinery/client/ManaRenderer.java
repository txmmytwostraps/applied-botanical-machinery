package appliedbotanicalmachinery.client;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import appliedbotanicalmachinery.ae2.ManaKey;
import appliedbotanicalmachinery.ae2.ManaKeyType;

import appeng.client.api.AEKeyRenderer;
import appeng.client.api.AEKeyRendering;
import appeng.client.gui.style.Blitter;

public class ManaRenderer implements AEKeyRenderer<ManaKey, ManaRenderer.RenderState> {

    public static void initialize(IEventBus bus) {
        bus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> {
            AEKeyRendering.register(ManaKeyType.TYPE, ManaKey.class, new ManaRenderer());
        }));
    }

    private static TextureAtlasSprite waterSprite() {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS)
                .getSprite(prefix("block/mana_water"));
    }

    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphicsExtractor guiGraphics, int x, int y, ManaKey stack) {
        Blitter.sprite(waterSprite())
                // Most fluid texture have transparency, but we want an opaque slot
                .blending(false)
                .dest(x, y, 16, 16)
                .blit(guiGraphics);
    }

    @Override
    public Class<RenderState> stateClass() {
        return RenderState.class;
    }

    @Override
    public RenderState createState() {
        return new RenderState();
    }

    @Override
    public void extract(RenderState state, ManaKey what, @Nullable Level level, int seed) {
        state.sprite = waterSprite();
    }

    @Override
    public void submit(PoseStack poseStack, RenderState state, SubmitNodeCollector nodes, int lightCoords) {
        poseStack.pushPose();
        // Push it out of the block face a bit to avoid z-fighting
        poseStack.translate(0, 0, 0.01f);

        // In comparison to items, make it _slightly_ smaller because item icons
        // usually don't extend to the full size.
        var scale = 1 - 0.05f;

        // y is flipped here
        var x0 = -scale / 2;
        var y0 = scale / 2;
        var x1 = scale / 2;
        var y1 = -scale / 2;

        var sprite = state.sprite;
        nodes.submitCustomGeometry(poseStack, RenderTypes.entitySolid(sprite.atlasLocation()), (pose, buffer) -> {
            buffer.addVertex(pose, x0, y1, 0)
                    .setColor(-1)
                    .setUv(sprite.getU0(), sprite.getV1())
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(lightCoords)
                    .setNormal(0, 0, 1);
            buffer.addVertex(pose, x1, y1, 0)
                    .setColor(-1)
                    .setUv(sprite.getU1(), sprite.getV1())
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(lightCoords)
                    .setNormal(0, 0, 1);
            buffer.addVertex(pose, x1, y0, 0)
                    .setColor(-1)
                    .setUv(sprite.getU1(), sprite.getV0())
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(lightCoords)
                    .setNormal(0, 0, 1);
            buffer.addVertex(pose, x0, y0, 0)
                    .setColor(-1)
                    .setUv(sprite.getU0(), sprite.getV0())
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(lightCoords)
                    .setNormal(0, 0, 1);
        });

        poseStack.popPose();
    }

    @Override
    public List<Component> getTooltip(ManaKey stack) {
        return List.of(ManaKeyType.MANA);
    }

    public static final class RenderState {
        public TextureAtlasSprite sprite;
    }
}
