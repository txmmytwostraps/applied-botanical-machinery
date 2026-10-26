package appliedbotanicalmachinery.extramachinery.entities.manaSpark;

import appliedbotanicalmachinery.extramachinery.ModEntities;
import appliedbotanicalmachinery.extramachinery.ModItems;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class EntityAureateManaSpark extends EntityManaSparkPattern {

    private static int RATE = LibXServerConfig.SparkTier.aureateSpark;

    public EntityAureateManaSpark(EntityType<?> entityEntityType, Level level) {
        super(entityEntityType, level);
        this.TRANSFER_RATE = RATE;
    }

    public static int getRate(){
        return RATE;
    }

    public EntityAureateManaSpark(Level level){
        this(ModEntities.AUREATE_SPARK, level);
    }

    @Override
    protected Item getSparkItem() {
        return ModItems.aureateSpark;
    }
}

