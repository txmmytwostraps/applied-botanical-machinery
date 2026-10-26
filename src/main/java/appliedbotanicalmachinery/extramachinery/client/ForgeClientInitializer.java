package appliedbotanicalmachinery.extramachinery.client;

import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.extramachinery.ModEntities;
import appliedbotanicalmachinery.extramachinery.client.render.ColorHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.api.block.WandHUD;

import java.util.function.Function;

@EventBusSubscriber(modid = AppliedBotanics.MOD_ID, value = Dist.CLIENT)
public class ForgeClientInitializer {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
        ColorHandler.submitItems(evt::register);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent evt) {
        ModEntities.registerWandHudCaps((factory, types) -> {
            for (EntityType<?> type : types) {
                registerWandHud(evt, type, factory);
            }
        });
    }

    private static <E extends Entity> void registerWandHud(RegisterCapabilitiesEvent evt, EntityType<E> type, Function<Entity, WandHUD> factory) {
        evt.registerEntity(BotaniaClientCapabilities.ENTITY_WAND_HUD, type, (entity, ctx) -> factory.apply(entity));
    }
}
