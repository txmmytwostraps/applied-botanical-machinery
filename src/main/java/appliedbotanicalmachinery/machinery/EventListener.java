package appliedbotanicalmachinery.machinery;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaPool;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "appliedbotanicalmachinery")
public class EventListener {

    @SubscribeEvent
    public static void resourcesReload(OnDatapackSyncEvent event) {
        BlockEntityMechanicalManaPool.invalidateCatalysts();
    }
}
