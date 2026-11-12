package appliedbotanicalmachinery.client;

import java.util.List;
import java.util.Locale;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InitializeClientRegistriesEvent;

import appliedbotanicalmachinery.ABItems;
import appliedbotanicalmachinery.AppliedBotanics;

import appeng.api.client.StorageCellModels;

@Mod(value = AppliedBotanics.MOD_ID, dist = Dist.CLIENT)
public class AppliedBotanicsClient {

    public AppliedBotanicsClient(IEventBus bus) {
        ManaRenderer.initialize(bus);
        bus.addListener(AppliedBotanicsClient::registerCellModels);
    }

    // Drive models of the mana cells; AE2 reads them on the client only
    private static void registerCellModels(InitializeClientRegistriesEvent event) {
        for (var tier : ABItems.Tier.values()) {
            var model = AppliedBotanics.id("block/drive/cells/" + tier.toString().toLowerCase(Locale.ROOT).substring(1)
                    + "_mana_cell");
            StorageCellModels.registerModel(ABItems.get(tier).get(), model);
            StorageCellModels.registerModel(ABItems.getPortableCell(tier).get(), model);
        }
        for (var size : List.of("1m", "4m", "16m", "64m", "256m")) {
            var cell = BuiltInRegistries.ITEM.getValue(AppliedBotanics.id("mana_storage_cell_" + size));
            StorageCellModels.registerModel(cell, AppliedBotanics.id("block/drive/cells/mana_storage_cell_" + size));
        }
    }

}
