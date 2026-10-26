package appliedbotanicalmachinery.extramachinery.data;

import appliedbotanicalmachinery.ABBlocks;
import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.machinery.core.TileTags;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;

public class LootTables extends BlockLootProviderBase {
    public LootTables(DatagenContext context) {
        super(context);
    }

    public static final String MANA = "mana";
    public static final String FLUID = "fluid";
    public static final String ENERGY = "energyStorage";

    protected void setup() {
        // Applied Botanics generates its own loot tables
        this.customLootTable(ABBlocks.FLUIX_MANA_POOL.get());

        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.alfheimMarket, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.industrialAgglomerationFactory, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalManaInfuser, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.manaBattery, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS, TileTags.SLOT_1_LOCKED, TileTags.SLOT_2_LOCKED));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.manaBatteryCreative, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS, TileTags.SLOT_1_LOCKED, TileTags.SLOT_2_LOCKED));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalApothecary, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS, TileTags.FLUID, TileTags.MAX_PROGRESS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalBrewery, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS, TileTags.MAX_PROGRESS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalDaisy, this.copyNBT(TileTags.INVENTORY, TileTags.WORKING_TICKS));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalManaPool, this.copyNBT(TileTags.INVENTORY, TileTags.MANA));
        this.drops(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalRunicAltar, this.copyNBT(TileTags.INVENTORY, TileTags.MANA, TileTags.PROGRESS, TileTags.MAX_PROGRESS));

        this.drops(ModBlocks.baseAlfheimMarket, this.copyNBT(MANA));
        this.drops(ModBlocks.upgradedAlfheimMarket, this.copyNBT(MANA));
        this.drops(ModBlocks.advancedAlfheimMarket, this.copyNBT(MANA));
        this.drops(ModBlocks.ultimateAlfheimMarket, this.copyNBT(MANA));

        this.drops(ModBlocks.baseIndustrialAgglomerationFactory, this.copyNBT(MANA));
        this.drops(ModBlocks.upgradedIndustrialAgglomerationFactory, this.copyNBT(MANA));
        this.drops(ModBlocks.advancedIndustrialAgglomerationFactory, this.copyNBT(MANA));
        this.drops(ModBlocks.ultimateIndustrialAgglomerationFactory, this.copyNBT(MANA));

        this.drops(ModBlocks.baseApothecary, this.copyNBT(FLUID));
        this.drops(ModBlocks.upgradedApothecary, this.copyNBT(FLUID));
        this.drops(ModBlocks.advancedApothecary, this.copyNBT(FLUID));
        this.drops(ModBlocks.ultimateApothecary, this.copyNBT(FLUID));

        this.drops(ModBlocks.baseDaisy, this.copyNBT());
        this.drops(ModBlocks.upgradedDaisy, this.copyNBT());
        this.drops(ModBlocks.advancedDaisy, this.copyNBT());
        this.drops(ModBlocks.ultimateDaisy, this.copyNBT());

        this.drops(ModBlocks.baseManaPool, this.copyNBT(MANA));
        this.drops(ModBlocks.upgradedManaPool, this.copyNBT(MANA));
        this.drops(ModBlocks.advancedManaPool, this.copyNBT(MANA));
        this.drops(ModBlocks.ultimateManaPool, this.copyNBT(MANA));

        this.drops(ModBlocks.baseRunicAltar, this.copyNBT(MANA));
        this.drops(ModBlocks.upgradedRunicAltar, this.copyNBT(MANA));
        this.drops(ModBlocks.advancedRunicAltar, this.copyNBT(MANA));
        this.drops(ModBlocks.ultimateRunicAltar, this.copyNBT(MANA));

        this.drops(ModBlocks.baseOrechid, this.copyNBT(MANA));
        this.drops(ModBlocks.upgradedOrechid, this.copyNBT(MANA));
        this.drops(ModBlocks.advancedOrechid, this.copyNBT(MANA));
        this.drops(ModBlocks.ultimateOrechid, this.copyNBT(MANA));

        this.drops(ModBlocks.jadedAmaranthus, this.copyNBT(MANA));
        this.drops(ModBlocks.greenhouse, this.copyNBT(MANA, ENERGY));
    }
}