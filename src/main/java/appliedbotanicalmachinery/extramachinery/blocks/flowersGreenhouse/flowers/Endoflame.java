package appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.flowers;

import appliedbotanicalmachinery.extramachinery.blocks.flowersGreenhouse.GenFlowers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import velrondevs.botania.common.block.mana.ManaSpreaderBlock;
import velrondevs.botania.xplat.XplatAbstractions;

public class Endoflame extends GenFlowers {
    @Override
    public boolean availableFuel(ItemStack fuel){
        return !fuel.isEmpty() && !(Block.byItem(fuel.getItem()) instanceof ManaSpreaderBlock) && XplatAbstractions.INSTANCE.getSmeltingBurnTime(fuel) > 0;
    }

    @Override
    public int getPerMana(ItemStack fuel){
        return !fuel.isEmpty() && !(Block.byItem(fuel.getItem()) instanceof ManaSpreaderBlock) ? XplatAbstractions.INSTANCE.getSmeltingBurnTime(fuel) : 0;
    }
}
