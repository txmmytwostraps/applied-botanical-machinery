package appliedbotanicalmachinery;

import net.minecraft.resources.Identifier;

public interface AppliedBotanics {

    String MOD_ID = "appliedbotanicalmachinery";

    static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
