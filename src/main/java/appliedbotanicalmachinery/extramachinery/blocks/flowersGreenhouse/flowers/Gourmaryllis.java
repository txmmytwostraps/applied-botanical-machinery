package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import net.minecraft.world.item.ItemStack;

public class Gourmaryllis extends GenFlowers {

    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.getFoodProperties(null) != null;
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        int food = Math.min(12, fuel.getFoodProperties(null).nutrition());

        return (int)((double)(food * food * 70) * 1.8);
    }
}

