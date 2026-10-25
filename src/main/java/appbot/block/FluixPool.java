package appbot.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.registry.BotaniaBlocks;

public class FluixPool extends ManaPoolBlock {

    public FluixPool() {
        super(ManaPoolBlock.Variant.FABULOUS, BlockBehaviour.Properties.ofFullCopy(BotaniaBlocks.livingrock));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluixPoolBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity,
            ItemStack itemStack) {
        if (livingEntity instanceof Player player
                && level.getBlockEntity(blockPos) instanceof FluixPoolBlockEntity blockEntity) {
            blockEntity.getMainNode().setOwningPlayer(player);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        var pool = (FluixPoolBlockEntity) world.getBlockEntity(pos);
        return pool != null ? pool.calculateComparatorLevel() : 0;
    }
}
