package appliedbotanicalmachinery.extramachinery.events;

import appliedbotanicalmachinery.AppliedBotanics;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

@EventBusSubscriber(modid = AppliedBotanics.MOD_ID, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    public static final StandaloneModelKey<BlockStateModelPart> GREENHOUSE_PYLONS = key("block/greenhouse/pylons");
    public static final StandaloneModelKey<BlockStateModelPart> GREENHOUSE_CIRCLE_1 = key("block/greenhouse/circle_1");
    public static final StandaloneModelKey<BlockStateModelPart> GREENHOUSE_CIRCLE_2 = key("block/greenhouse/circle_2");
    public static final StandaloneModelKey<BlockStateModelPart> GREENHOUSE_CIRCLE_3 = key("block/greenhouse/circle_3");
    public static final StandaloneModelKey<BlockStateModelPart> GREENHOUSE_GARDEN_BED = key("block/greenhouse/garden_bed");

    private static StandaloneModelKey<BlockStateModelPart> key(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(AppliedBotanics.MOD_ID, path);
        return new StandaloneModelKey<>(id::toString);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterStandalone event) {
        register(event, GREENHOUSE_PYLONS, "block/greenhouse/pylons");
        register(event, GREENHOUSE_CIRCLE_1, "block/greenhouse/circle_1");
        register(event, GREENHOUSE_CIRCLE_2, "block/greenhouse/circle_2");
        register(event, GREENHOUSE_CIRCLE_3, "block/greenhouse/circle_3");
        register(event, GREENHOUSE_GARDEN_BED, "block/greenhouse/garden_bed");
    }

    private static void register(ModelEvent.RegisterStandalone event, StandaloneModelKey<BlockStateModelPart> key, String path) {
        event.register(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(AppliedBotanics.MOD_ID, path)));
    }
}
