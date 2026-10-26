package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import velrondevs.botania.common.block.flower.generating.EntropinnyumBlockEntity;

public class Entropinnyum extends GenFlowers {

    @Override
    public boolean availableFuel(ItemStack fuel) {
        return fuel.getItem() == Blocks.TNT.asItem();
    }

    @Override
    public int getPerMana(ItemStack fuel) {
        return LibXServerConfig.GreenhouseSettings.Flowers.entropinnyum;
    }
}
