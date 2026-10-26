package appliedbotanicalmachinery.extramachinery.events;

import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers.*;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityGreenhouse;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolUpgraded;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.masterfulbotany.registry.ModBlocks;

@EventBusSubscriber(modid = AppliedBotanics.MOD_ID)
public class EventListener {
    public EventListener() {
    }

    @SubscribeEvent
    public static void resourcesReload(OnDatapackSyncEvent event) {
        BlockEntityManaPoolBase.invalidateCatalysts();
        BlockEntityManaPoolUpgraded.invalidateCatalysts();
        BlockEntityManaPoolAdvanced.invalidateCatalysts();
        BlockEntityManaPoolUltimate.invalidateCatalysts();
    }

    @SubscribeEvent
    public static void flowerInit(OnDatapackSyncEvent event){
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.endoflame.asItem(), new Endoflame());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.entropinnyum.asItem(), new Entropinnyum());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.gourmaryllis.asItem(), new Gourmaryllis());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.kekimurus.asItem(), new Kekimurus());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.munchdew.asItem(), new Munchdew());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.narslimmus.asItem(), new Narslimmus());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.rafflowsia.asItem(), new Rafflowsia());
        GenFlowers.addAllGenFlowers(BotaniaFlowerBlocks.rosaArcana.asItem(), new RosaArcana());

        if (ModList.get().isLoaded("masterfulbotany")){
            GenFlowers.addAllGenFlowers(ModBlocks.witherAconite.asItem(), new WitherAconite());
        }

    }
}