package appliedbotanicalmachinery.machinery;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaPool;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = "appliedbotanicalmachinery")
public class EventListener {

    @SubscribeEvent
    public static void resourcesReload(OnDatapackSyncEvent event) {
        BlockEntityMechanicalManaPool.invalidateCatalysts();
    }
}
