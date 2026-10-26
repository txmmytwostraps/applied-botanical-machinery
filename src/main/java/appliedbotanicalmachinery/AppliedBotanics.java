package appliedbotanicalmachinery;

import net.minecraft.resources.ResourceLocation;

public interface AppliedBotanics {

    String MOD_ID = "appliedbotanicalmachinery";

    static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
