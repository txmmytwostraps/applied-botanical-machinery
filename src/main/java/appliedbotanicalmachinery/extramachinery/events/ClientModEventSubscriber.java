package appliedbotanicalmachinery.extramachinery.events;

import appliedbotanicalmachinery.AppliedBotanics;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AppliedBotanics.MOD_ID, value = Dist.CLIENT)
public class ClientModEventSubscriber {
    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(AppliedBotanics.MOD_ID, "block/greenhouse/pylons")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(AppliedBotanics.MOD_ID, "block/greenhouse/circle_1")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(AppliedBotanics.MOD_ID, "block/greenhouse/circle_2")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(AppliedBotanics.MOD_ID, "block/greenhouse/circle_3")));
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(AppliedBotanics.MOD_ID, "block/greenhouse/garden_bed")));
    }
}