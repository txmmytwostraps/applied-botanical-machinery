package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public class RosaArcana extends GenFlowers {

    private Random random = new Random();

    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        return random.nextInt(3, 33) * LibXServerConfig.GreenhouseSettings.Flowers.rosaArcana;
    }
}

