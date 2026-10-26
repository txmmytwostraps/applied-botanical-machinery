package appliedbotanicalmachinery.extramachinery.data;


import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.init.InitAppbot;
import net.minecraftforge.fml.ModList;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.model.ItemModelProviderBase;

public class ItemModels extends ItemModelProviderBase {
    public ItemModels(DatagenContext context) {
        super(context);
        this.manualModel(ModItems.catalystPetal);
        this.manualModel(ModItems.catalystPetalBlock);
        this.manualModel(ModBlocks.greenhouse.asItem());

        if (ModList.get().isLoaded("appliedbotanicalmachinery")){
            this.manualModel(InitAppbot.manaStorageCell_1m.get());
            this.manualModel(InitAppbot.manaStorageCell_4m.get());
            this.manualModel(InitAppbot.manaStorageCell_16m.get());
            this.manualModel(InitAppbot.manaStorageCell_64m.get());
            this.manualModel(InitAppbot.manaStorageCell_256m.get());
        }
    }

    @Override
    protected void setup() {
    }

}