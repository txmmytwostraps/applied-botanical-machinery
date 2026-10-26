package appliedbotanicalmachinery.extramachinery.init;

import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.AppliedBotanicsForge;
import appliedbotanicalmachinery.item.ManaCellItem;
import appeng.api.client.StorageCellModels;
import appeng.items.materials.StorageComponentItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitAppbot {
    private static final DeferredRegister.Items ITEMS;

    public static final DeferredItem<Item> cellComponent_1m;
    public static final DeferredItem<Item> cellComponent_4m;
    public static final DeferredItem<Item> cellComponent_16m;
    public static final DeferredItem<Item> cellComponent_64m;
    public static final DeferredItem<Item> cellComponent_256m;

    public static final DeferredItem<Item> manaStorageCell_1m;
    public static final DeferredItem<Item> manaStorageCell_4m;
    public static final DeferredItem<Item> manaStorageCell_16m;
    public static final DeferredItem<Item> manaStorageCell_64m;
    public static final DeferredItem<Item> manaStorageCell_256m;

    public static void initialize(IEventBus bus) {
        ITEMS.register(bus);

        bus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() ->{
            StorageCellModels.registerModel(manaStorageCell_1m.get(), AppliedBotanics.id("block/drive/cells/mana_storage_cell_1m"));
            StorageCellModels.registerModel(manaStorageCell_4m.get(), AppliedBotanics.id("block/drive/cells/mana_storage_cell_4m"));
            StorageCellModels.registerModel(manaStorageCell_16m.get(), AppliedBotanics.id("block/drive/cells/mana_storage_cell_16m"));
            StorageCellModels.registerModel(manaStorageCell_64m.get(), AppliedBotanics.id("block/drive/cells/mana_storage_cell_64m"));
            StorageCellModels.registerModel(manaStorageCell_256m.get(), AppliedBotanics.id("block/drive/cells/mana_storage_cell_256m"));
        }));
    }

    static {
        ITEMS = DeferredRegister.createItems(AppliedBotanics.MOD_ID);

        cellComponent_1m = ITEMS.register("cell_component_1m",
                () -> new StorageComponentItem(new Item.Properties(), 1024));
        cellComponent_4m = ITEMS.register("cell_component_4m",
                () -> new StorageComponentItem(new Item.Properties(), 4096));
        cellComponent_16m = ITEMS.register("cell_component_16m",
                () -> new StorageComponentItem(new Item.Properties(), 16384));
        cellComponent_64m = ITEMS.register("cell_component_64m",
                () -> new StorageComponentItem(new Item.Properties(), 65536));
        cellComponent_256m = ITEMS.register("cell_component_256m",
                () -> new StorageComponentItem(new Item.Properties(), 262144));

        manaStorageCell_1m = ITEMS.register("mana_storage_cell_1m",
                () -> new ManaCellItem(new Item.Properties().stacksTo(1).component(AppliedBotanicsForge.MANA, 0L), cellComponent_1m.get(), 1024, 3));
        manaStorageCell_4m = ITEMS.register("mana_storage_cell_4m",
                () -> new ManaCellItem(new Item.Properties().stacksTo(1).component(AppliedBotanicsForge.MANA, 0L), cellComponent_4m.get(), 4096, 3.5));
        manaStorageCell_16m = ITEMS.register("mana_storage_cell_16m",
                () -> new ManaCellItem(new Item.Properties().stacksTo(1).component(AppliedBotanicsForge.MANA, 0L), cellComponent_16m.get(), 16384, 4.0));
        manaStorageCell_64m = ITEMS.register("mana_storage_cell_64m",
                () -> new ManaCellItem(new Item.Properties().stacksTo(1).component(AppliedBotanicsForge.MANA, 0L), cellComponent_64m.get(), 65536, 4.5));
        manaStorageCell_256m = ITEMS.register("mana_storage_cell_256m",
                () -> new ManaCellItem(new Item.Properties().stacksTo(1).component(AppliedBotanicsForge.MANA, 0L), cellComponent_256m.get(), 262144, 5.0));
    }

}
