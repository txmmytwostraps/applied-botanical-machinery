package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import velrondevs.botania.xplat.XplatAbstractions;

public class Narslimmus extends GenFlowers {
    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.getItem() == Items.SLIME_BALL || fuel.getItem() == Items.SLIME_BLOCK;
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        return (XplatAbstractions.INSTANCE.gogLoaded() ? LibXServerConfig.GreenhouseSettings.Flowers.narslimmus : LibXServerConfig.GreenhouseSettings.Flowers.narslimmus * 4)
                * (fuel.getItem() == Items.SLIME_BALL ? 1 : 9);
    }
}
