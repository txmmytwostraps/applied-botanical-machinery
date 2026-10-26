package appliedbotanicalmachinery.extramachinery.entities.manaSpark;

import appliedbotanicalmachinery.extramachinery.ModEntities;
import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class EntityMalachiteManaSpark extends EntityManaSparkPattern {

    private static int RATE = LibXServerConfig.SparkTier.malachiteSpark;

    public EntityMalachiteManaSpark(EntityType<?> entityEntityType, Level level) {
        super(entityEntityType, level);
        this.TRANSFER_RATE = RATE;
    }

    public static int getRate(){
        return RATE;
    }

    public EntityMalachiteManaSpark(Level level){
        this(ModEntities.MALACHITE_SPARK, level);
    }

    @Override
    protected Item getSparkItem() {
        return ModItems.malachiteSpark;
    }
}