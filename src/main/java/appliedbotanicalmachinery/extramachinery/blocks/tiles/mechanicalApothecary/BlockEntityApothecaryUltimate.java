package appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary;

import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityApothecaryPattern;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig.RenderingVisualContent.ApothecarySettings;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig.ApothecarySettings.ultimateApothecary;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityApothecaryUltimate extends BlockEntityApothecaryPattern {

    private static final int SEEDS_SLOT = 0;
    private static final int UPGRADE_SLOT_1 = 1;
    private static final int UPGRADE_SLOT_2 = 2;

    private static final int FIRST_INPUT_SLOT = 3;
    private static final int LAST_INPUT_SLOT = 18;
    private static final int FIRST_OUTPUT_SLOT = 19;
    private static final int LAST_OUTPUT_SLOT = 34;

    public BlockEntityApothecaryUltimate(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, new int[] {
                SEEDS_SLOT, FIRST_INPUT_SLOT, LAST_INPUT_SLOT, FIRST_OUTPUT_SLOT, LAST_OUTPUT_SLOT,
                        UPGRADE_SLOT_1, UPGRADE_SLOT_2
        }, ultimateApothecary.countCraft,
                new SettingPattern()
                        .addConfig("mechanicalApothecaryRender", Boolean.toString(ApothecarySettings.apothecaryUltimate))
                        .addConfig("craftTime", Integer.toString(ultimateApothecary.craftTime))
                        .addConfig("fluidStorage", Integer.toString(ultimateApothecary.fluidStorage)));
    }
}
