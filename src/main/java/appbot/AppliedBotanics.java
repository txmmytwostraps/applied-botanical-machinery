package appbot;

import net.minecraft.resources.ResourceLocation;

/**
 * Applied Botanics' original entry point, for addons built against it (MEGA Cells). Hands off to ours.
 */
public interface AppliedBotanics {

    static ResourceLocation id(String path) {
        return appliedbotanicalmachinery.AppliedBotanics.id(path);
    }
}
