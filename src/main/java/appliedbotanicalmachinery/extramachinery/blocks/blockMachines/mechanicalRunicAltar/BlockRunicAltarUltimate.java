package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalRunicAltar;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalRunicAltar.ContainerRunicAltarUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalRunicAltar.RendererRunicAltarUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalRunicAltar.BlockEntityRunicAltarUltimate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

import javax.annotation.Nonnull;

public class BlockRunicAltarUltimate extends ExtraBotanicalBlock<BlockEntityRunicAltarUltimate, ContainerRunicAltarUltimate> {
    public static final RotationShape SHAPE;

    public BlockRunicAltarUltimate(ModX mod, Class<BlockEntityRunicAltarUltimate> teClass, AdvancedMenuType<ContainerRunicAltarUltimate, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, false, true, properties);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenRunicAltarUltimate::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new RendererRunicAltarUltimate());
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(2.0, 5.0, 2.0, 14.0, 9.0, 14.0), box(6.0, 3.0, 6.0, 10.0, 5.0, 10.0), box(4.0, 1.0, 4.0, 12.0, 3.0, 12.0)));
    }
}
