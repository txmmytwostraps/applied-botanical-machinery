package appliedbotanicalmachinery.machinery.compat;

import net.neoforged.fml.ModList;

public class MasterfulBotanyCompat {

    public static final String MOD_ID = "masterfulbotany";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }
}
