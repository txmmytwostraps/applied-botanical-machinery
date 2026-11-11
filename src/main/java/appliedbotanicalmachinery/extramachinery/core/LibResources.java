package appliedbotanicalmachinery.extramachinery.core;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import net.minecraft.resources.Identifier;

public class LibResources {

    private static final String GUI_PREFIX = "textures/gui/";
    private static final String MODEL_GEO_PREFIX = "geo/";
    private static final String ANIMATION_GEO_PREFIX = "animations/";
    public static final Identifier MANA_BAR_CURRENT = loc(GUI_PREFIX + "misc/current_mana_horizontal");
    public static final Identifier INFINITY_MANA_BAR_CURRENT = loc(GUI_PREFIX + "misc/infinity_mana");
    public static final Identifier WATER_BAR_CURRENT = loc(GUI_PREFIX + "misc/current_water");
    public static final Identifier ENERGY_BAR_CURRENT = loc(GUI_PREFIX + "misc/current_energy");
    public static final Identifier HEAT_BAR_CURRENT = loc(GUI_PREFIX + "misc/current_heat");

    public static final Identifier BASE_INVENTORY = loc(GUI_PREFIX + "inventory_modules/base_inventory");
    public static final Identifier UPGRADE_INVENTORY = loc(GUI_PREFIX + "inventory_modules/upgrade_inventory");
    public static final Identifier ADVANCED_INVENTORY = loc(GUI_PREFIX + "inventory_modules/advanced_inventory");
    public static final Identifier ULTIMATE_INVENTORY = loc(GUI_PREFIX + "inventory_modules/ultimate_inventory");
    public static final Identifier OTHER_INVENTORY = loc(GUI_PREFIX + "inventory_modules/other_inventory");

    public static final Identifier BASE_SPARK = loc("base_spark");
    public static final Identifier MALACHITE_SPARK = loc("malachite_spark");
    public static final Identifier SAFFRON_SPARK = loc("saffron_spark");
    public static final Identifier SHADOW_SPARK = loc("shadow_spark");
    public static final Identifier CRIMSON_SPARK = loc("crimson_spark");
    public static final Identifier AUREATE_SPARK = loc("aureate_spark");
    public static final Identifier MAZARINE_SPARK = loc("mazarine_spark");

    public static final Identifier BASE_MECHANICAL_MANA_POOL_GUI = gui(LibNames.BASE_MECHANICAL_MANA_POOL);
    public static final Identifier UPGRADED_MECHANICAL_MANA_POOL_GUI = gui(LibNames.UPGRADED_MECHANICAL_MANA_POOL);
    public static final Identifier ADVANCED_MECHANICAL_MANA_POOL_GUI = gui(LibNames.ADVANCED_MECHANICAL_MANA_POOL);
    public static final Identifier ULTIMATE_MECHANICAL_MANA_POOL_GUI = gui(LibNames.ULTIMATE_MECHANICAL_MANA_POOL);

    public static final Identifier BASE_MECHANICAL_RUNIC_ALTAR_GUI = gui(LibNames.BASE_MECHANICAL_RUNIC_ALTAR);
    public static final Identifier UPGRADED_MECHANICAL_RUNIC_ALTAR_GUI = gui(LibNames.UPGRADED_MECHANICAL_RUNIC_ALTAR);
    public static final Identifier ADVANCED_MECHANICAL_RUNIC_ALTAR_GUI = gui(LibNames.ADVANCED_MECHANICAL_RUNIC_ALTAR);
    public static final Identifier ULTIMATE_MECHANICAL_RUNIC_ALTAR_GUI = gui(LibNames.ULTIMATE_MECHANICAL_RUNIC_ALTAR);

    public static final Identifier BASE_MECHANICAL_DAISY_GUI = gui(LibNames.BASE_MECHANICAL_DAISY);
    public static final Identifier UPGRADED_MECHANICAL_DAISY_GUI = gui(LibNames.UPGRADED_MECHANICAL_DAISY);
    public static final Identifier ADVANCED_MECHANICAL_DAISY_GUI = gui(LibNames.ADVANCED_MECHANICAL_DAISY);
    public static final Identifier ULTIMATE_MECHANICAL_DAISY_GUI = gui(LibNames.ULTIMATE_MECHANICAL_DAISY);

    public static final Identifier BASE_MECHANICAL_APOTHECARY_GUI = gui(LibNames.BASE_MECHANICAL_APOTHECARY);
    public static final Identifier UPGRADED_MECHANICAL_APOTHECARY_GUI = gui(LibNames.UPGRADED_MECHANICAL_APOTHECARY);
    public static final Identifier ADVANCED_MECHANICAL_APOTHECARY_GUI = gui(LibNames.ADVANCED_MECHANICAL_APOTHECARY);
    public static final Identifier ULTIMATE_MECHANICAL_APOTHECARY_GUI = gui(LibNames.ULTIMATE_MECHANICAL_APOTHECARY);


    public static final Identifier BASE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI = gui(LibNames.BASE_INDUSTRIAL_AGGLOMERATION_FACTORY);
    public static final Identifier UPGRADED_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI = gui(LibNames.UPGRADED_INDUSTRIAL_AGGLOMERATION_FACTORY);
    public static final Identifier ADVANCED_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI = gui(LibNames.ADVANCED_INDUSTRIAL_AGGLOMERATION_FACTORY);
    public static final Identifier ULTIMATE_INDUSTRIAL_AGGLOMERATION_FACTORY_GUI = gui(LibNames.ULTIMATE_INDUSTRIAL_AGGLOMERATION_FACTORY);

    public static final Identifier BASE_MANA_INFUSER_GUI = gui(LibNames.BASE_MANA_INFUSER);
    public static final Identifier UPGRADED_MANA_INFUSER_GUI = gui(LibNames.UPGRADED_MANA_INFUSER);
    public static final Identifier ADVANCED_MANA_INFUSER_GUI = gui(LibNames.ADVANCED_MANA_INFUSER);
    public static final Identifier ULTIMATE_MANA_INFUSER_GUI = gui(LibNames.ULTIMATE_MANA_INFUSER);

    public static final Identifier BASE_ALFHEIM_MARKET_GUI = gui(LibNames.BASE_ALFHEIM_MARKET);
    public static final Identifier UPGRADED_ALFHEIM_MARKET_GUI = gui(LibNames.UPGRADED_ALFHEIM_MARKET);
    public static final Identifier ADVANCED_ALFHEIM_MARKET_GUI = gui(LibNames.ADVANCED_ALFHEIM_MARKET);
    public static final Identifier ULTIMATE_ALFHEIM_MARKET_GUI = gui(LibNames.ULTIMATE_ALFHEIM_MARKET);

    public static final Identifier BASE_ORECHID_GUI = gui(LibNames.BASE_ORECHID);
    public static final Identifier UPGRADED_ORECHID_GUI = gui(LibNames.UPGRADED_ORECHID);
    public static final Identifier ADVANCED_ORECHID_GUI = gui(LibNames.ADVANCED_ORECHID);
    public static final Identifier ULTIMATE_ORECHID_GUI = gui(LibNames.ULTIMATE_ORECHID);
    public static final Identifier JADED_AMARANTHUS_GUI = gui(LibNames.JADED_AMARANTHUS);
    public static final Identifier GREENHOUSE_GUI = gui(LibNames.GREENHOUSE);


    public static final Identifier GREENHOUSE_TEXTURE = loc("textures/block/" + LibNames.GREENHOUSE);
    public static final Identifier GREENHOUSE_GEO_MODEL = model(MODEL_GEO_PREFIX + LibNames.GREENHOUSE);
    public static final Identifier GREENHOUSE_GEO_ANIMATION = animation(ANIMATION_GEO_PREFIX + LibNames.GREENHOUSE);
    public LibResources() {
    }

    private static Identifier gui(String id) {
        return loc(GUI_PREFIX + id);
    }

    private static Identifier loc(String id) {
        return Identifier.fromNamespaceAndPath(AppliedBotanicalMachinery.getInstance().modid, id + ".png");
    }
    private static Identifier model(String id) {
        return Identifier.fromNamespaceAndPath(AppliedBotanicalMachinery.getInstance().modid, id + ".geo.json");
    }
    private static Identifier animation(String id) {
        return Identifier.fromNamespaceAndPath(AppliedBotanicalMachinery.getInstance().modid, id + ".animation.json");
    }
}
