package appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityDaisyPattern;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig.RenderingVisualContent.DaisySettings;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig.DaisySettings.upgradedDaisy;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDaisyUpgraded extends BlockEntityDaisyPattern {

    public static final int SLOT_INVENTORY = 12;

    public BlockEntityDaisyUpgraded(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, SLOT_INVENTORY,
                new SettingPattern().addConfig("durationTime", Integer.toString(upgradedDaisy.durationTime))
                        .addConfig("renderingDaisy", Boolean.toString(DaisySettings.daisyUpgraded))
                        .addConfig("sizeSlots", Integer.toString(upgradedDaisy.sizeItemSlots)));
    }
}
