package appliedbotanicalmachinery.extramachinery.client;

import appliedbotanicalmachinery.extramachinery.Items.render.*;
import appliedbotanicalmachinery.extramachinery.ModEntities;
import velrondevs.botania.client.render.entity.EntityRenderers.EntityRendererConsumer;

public class EntityRenderers {

    public static void registerEntityRenderers(EntityRendererConsumer consumer) {
        consumer.accept(ModEntities.BASE_SPARK, RenderBaseManaSpark::new);
        consumer.accept(ModEntities.MALACHITE_SPARK, RenderMalachiteManaSpark::new);
        consumer.accept(ModEntities.SAFFRON_SPARK, RenderSaffronManaSpark::new);
        consumer.accept(ModEntities.SHADOW_SPARK, RenderShadowManaSpark::new);
        consumer.accept(ModEntities.CRIMSON_SPARK, RenderCrimsonManaSpark::new);
        consumer.accept(ModEntities.AUREATE_SPARK, RenderAureateManaSpark::new);
        consumer.accept(ModEntities.MAZARINE_SPARK, RenderMazarineManaSpark::new);
    }

}
