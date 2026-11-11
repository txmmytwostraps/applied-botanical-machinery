package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import net.minecraft.core.component.DataComponents;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import net.minecraft.world.item.ItemStack;

public class Gourmaryllis extends GenFlowers {

    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.get(DataComponents.FOOD) != null;
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        int food = Math.min(12, fuel.get(DataComponents.FOOD).nutrition());

        return (int)((double)(food * food * 70) * 1.8);
    }
}

