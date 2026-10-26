package appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalDaisy;

import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityDaisyPattern;
import appliedbotanicalmachinery.extramachinery.config.LibXServerConfig.DaisySettings.advancedDaisy;
import appliedbotanicalmachinery.extramachinery.config.LibXClientConfig.RenderingVisualContent.DaisySettings;
import appliedbotanicalmachinery.extramachinery.util.SettingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityDaisyAdvanced extends BlockEntityDaisyPattern {

    public static final int SLOT_INVENTORY = 14;

    public BlockEntityDaisyAdvanced(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, SLOT_INVENTORY,
                new SettingPattern().addConfig("durationTime", Integer.toString(advancedDaisy.durationTime))
                        .addConfig("renderingDaisy", Boolean.toString(DaisySettings.daisyAdvanced))
                        .addConfig("sizeSlots", Integer.toString(advancedDaisy.sizeItemSlots)));
    }
}
