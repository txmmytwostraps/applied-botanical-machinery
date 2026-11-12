package appliedbotanicalmachinery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Fluid sprites and colors from the fluid models (26.1 keeps them there instead of the fluid type extensions).
 */
public final class FluidRender {

    private FluidRender() {
    }

    private static FluidModel model(FluidStack stack) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet()
                .get(stack.getFluid().defaultFluidState());
    }

    public static TextureAtlasSprite stillSprite(FluidStack stack) {
        return model(stack).stillMaterial().sprite();
    }

    /**
     * The tint of the fluid as an ARGB color; -1 (white) for untinted fluids.
     */
    public static int tint(FluidStack stack) {
        var tintSource = model(stack).fluidTintSource();
        return tintSource != null ? tintSource.colorAsStack(stack) : -1;
    }
}
