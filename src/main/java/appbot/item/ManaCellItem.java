package appbot.item;

import net.minecraft.world.level.ItemLike;

/**
 * Applied Botanics' original mana cell class, for addons built against it (MEGA Cells). Hands off to ours.
 */
public class ManaCellItem extends appliedbotanicalmachinery.item.ManaCellItem {

    public ManaCellItem(Properties properties, ItemLike coreItem, int kilobytes, double idleDrain) {
        super(properties, coreItem, kilobytes, idleDrain);
    }
}
