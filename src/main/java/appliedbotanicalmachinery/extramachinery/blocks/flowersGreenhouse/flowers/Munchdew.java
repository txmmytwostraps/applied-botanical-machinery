package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class Munchdew extends GenFlowers {
    @Override
    public boolean availableFuel(ItemStack fuel) {
        return Block.byItem(fuel.getItem()).defaultBlockState().is(BlockTags.LEAVES);
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        return LibXServerConfig.GreenhouseSettings.Flowers.munchdew;
    }
}
