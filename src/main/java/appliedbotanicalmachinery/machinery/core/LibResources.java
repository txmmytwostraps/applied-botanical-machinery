package appliedbotanicalmachinery.machinery.core;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import net.minecraft.resources.Identifier;

public class LibResources {

    private static final String GUI_PREFIX = "textures/gui/";

    public static final Identifier MANA_BAR = loc(GUI_PREFIX + "misc/mana_bar");
    public static final Identifier MANA_BAR_CURRENT = loc(GUI_PREFIX + "misc/current_mana");

    public static final Identifier ALFHEIM_MARKET_GUI = gui(LibNames.ALFHEIM_MARKET);
    public static final Identifier INDUSTRIAL_AGGLOMERATION_FACTORY_GUI = gui(LibNames.INDUSTRIAL_AGGLOMERATION_FACTORY);
    public static final Identifier MECHANICAL_MANA_INFUSER_GUI = gui(LibNames.MECHANICAL_MANA_INFUSER);
    public static final Identifier MANA_BATTERY_GUI = gui(LibNames.MANA_BATTERY);
    public static final Identifier MECHANICAL_APOTHECARY_GUI = gui(LibNames.MECHANICAL_APOTHECARY);
    public static final Identifier MECHANICAL_BREWERY_GUI = gui(LibNames.MECHANICAL_BREWERY);
    public static final Identifier MECHANICAL_DAISY_GUI = gui(LibNames.MECHANICAL_DAISY);
    public static final Identifier MECHANICAL_MANA_POOL_GUI = gui(LibNames.MECHANICAL_MANA_POOL);
    public static final Identifier MECHANICAL_RUNIC_ALTAR_GUI = gui(LibNames.MECHANICAL_RUNIC_ALTAR);

    private static Identifier gui(String id) {
        return loc(GUI_PREFIX + id);
    }

    private static Identifier loc(String id) {
        return AppliedBotanicalMachinery.getInstance().id(id + ".png");
    }
}
