package appliedbotanicalmachinery.extramachinery.data;


import appliedbotanicalmachinery.ABItems;
import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.init.InitAppbot;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.model.ItemModelProviderBase;

public class ItemModels extends ItemModelProviderBase {
    public ItemModels(DatagenContext context) {
        super(context);
        // Applied Botanics generates its own item models
        ABItems.getEntries().forEach(item -> this.manualModel(item.get()));

        this.manualModel(ModItems.catalystPetal);
        this.manualModel(ModItems.catalystPetalBlock);
        this.manualModel(ModBlocks.greenhouse.asItem());
        this.manualModel(ModBlocks.baseManaInfuser.asItem());
        this.manualModel(ModBlocks.upgradedManaInfuser.asItem());
        this.manualModel(ModBlocks.advancedManaInfuser.asItem());
        this.manualModel(ModBlocks.ultimateManaInfuser.asItem());
        this.manualModel(ModItems.aureateDragonstone);
        this.manualModel(ModItems.aureateIngot);
        this.manualModel(ModBlocks.aureateIngotBlock.asItem());
        this.manualModel(ModItems.aureateSpark);
        this.manualModel(ModItems.mazarineDragonstone);
        this.manualModel(ModItems.mazarineIngot);
        this.manualModel(ModBlocks.mazarineIngotBlock.asItem());
        this.manualModel(ModItems.mazarineSpark);

        this.manualModel(InitAppbot.manaStorageCell_1m.get());
        this.manualModel(InitAppbot.manaStorageCell_4m.get());
        this.manualModel(InitAppbot.manaStorageCell_16m.get());
        this.manualModel(InitAppbot.manaStorageCell_64m.get());
        this.manualModel(InitAppbot.manaStorageCell_256m.get());
    }

    @Override
    protected void setup() {
    }

}
