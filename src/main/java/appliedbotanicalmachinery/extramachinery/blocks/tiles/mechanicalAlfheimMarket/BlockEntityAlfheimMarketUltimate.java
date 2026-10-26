package appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket;

import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityAlfheimMarketPattern;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig.AlfheimMarketSettings.ultimateAlfheimMarket;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAlfheimMarketUltimate extends BlockEntityAlfheimMarketPattern {

    private static final int UPGRADE_SLOT = 0;
    private static final int FIRST_INPUT_SLOT = 1;
    private static final int LAST_INPUT_SLOT = 12;
    private static final int FIRST_OUTPUT_SLOT = 13;
    private static final int LAST_OUTPUT_SLOT = 24;

    public BlockEntityAlfheimMarketUltimate(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, ultimateAlfheimMarket.manaStorage, ultimateAlfheimMarket.countCraft,
                new int[] {
                        FIRST_INPUT_SLOT, LAST_INPUT_SLOT,
                        FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT, UPGRADE_SLOT
                },
                new SettingPattern()
                        .addConfig("craftTime", Integer.toString(ultimateAlfheimMarket.craftTime)));

    }
}
