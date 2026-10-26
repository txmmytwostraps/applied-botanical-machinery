package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WitherAconite extends GenFlowers {
    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.getItem() == Items.NETHER_STAR;
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        return LibXServerConfig.GreenhouseSettings.Flowers.witherAconite;
    }
}
