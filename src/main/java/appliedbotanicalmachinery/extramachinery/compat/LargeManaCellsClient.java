package appliedbotanicalmachinery.extramachinery.compat;

import appliedbotanicalmachinery.extramachinery.init.InitAppbot;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;

/**
 * Whether JEI and EMI should hide the 1M to 256M mana cells. They start before the server's config reaches the
 * client, so they go by what the server sent on join (LargeManaCellsMessage).
 */
public final class LargeManaCellsClient {

    @Nullable
    private static Boolean serverReplaced = null;

    private LargeManaCellsClient() {
    }

    public static void setServerReplaced(boolean replaced) {
        serverReplaced = replaced;
    }

    public static boolean hidden() {
        if (!ModList.get().isLoaded("megacells")) {
            return false;
        }
        if (Minecraft.getInstance().getConnection() == null || serverReplaced == null) {
            return InitAppbot.replacedByMegaCells();
        }
        return serverReplaced;
    }
}
