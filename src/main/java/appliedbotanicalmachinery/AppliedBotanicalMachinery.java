package appliedbotanicalmachinery;

import javax.annotation.Nonnull;

import org.moddingx.libx.datagen.DatagenSystem;
import org.moddingx.libx.mod.ModXRegistration;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import appliedbotanicalmachinery.extramachinery.ModEntities;
import appliedbotanicalmachinery.extramachinery.data.BlockStates;
import appliedbotanicalmachinery.extramachinery.data.CommonTags;
import appliedbotanicalmachinery.extramachinery.data.ItemModels;
import appliedbotanicalmachinery.extramachinery.data.LootTables;
import appliedbotanicalmachinery.extramachinery.data.Recipes;
import appliedbotanicalmachinery.extramachinery.init.InitAppbot;
import appliedbotanicalmachinery.machinery.network.BotanicalMachineryNetwork;

@Mod(AppliedBotanics.MOD_ID)
public final class AppliedBotanicalMachinery extends ModXRegistration {

    private static AppliedBotanicalMachinery instance;
    private static BotanicalMachineryNetwork network;

    public AppliedBotanicalMachinery(IEventBus bus) {
        instance = this;
        network = new BotanicalMachineryNetwork(this);
        new AppliedBotanicalMachineryTab(this);

        new AppliedBotanicsForge(bus);

        bus.addListener((RegisterEvent event) -> event.register(Registries.ENTITY_TYPE,
                helper -> ModEntities.registerEntities((type, id) -> helper.register(id, type))));
        InitAppbot.initialize(bus);

        DatagenSystem.create(this, system -> {
            system.addRegistryProvider(LootTables::new);

            system.addDataProvider(BlockStates::new);
            system.addDataProvider(CommonTags::new);
            system.addDataProvider(ItemModels::new);
            system.addDataProvider(Recipes::new);
        });
    }

    @Nonnull
    public static AppliedBotanicalMachinery getInstance() {
        return instance;
    }

    @Nonnull
    public static BotanicalMachineryNetwork getNetwork() {
        return network;
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
    }
}
