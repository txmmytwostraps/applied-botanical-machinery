package appliedbotanicalmachinery.extramachinery.compat;

import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.extramachinery.init.InitAppbot;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

/**
 * Whether JEI and EMI should hide the 1M to 256M mana cells. They start before the server's config reaches the
 * client, so they go by the recipes the server sent: the cell recipes only load when the cells are not replaced.
 */
public final class LargeManaCellsClient {

    private static final ResourceLocation CELL_RECIPE = AppliedBotanics.id("cells/mana_storage_cell_1m_storage");

    private LargeManaCellsClient() {
    }

    public static boolean hidden() {
        if (!ModList.get().isLoaded("megacells")) {
            return false;
        }
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return InitAppbot.replacedByMegaCells();
        }
        return connection.getRecipeManager().byKey(CELL_RECIPE).isEmpty();
    }
}
