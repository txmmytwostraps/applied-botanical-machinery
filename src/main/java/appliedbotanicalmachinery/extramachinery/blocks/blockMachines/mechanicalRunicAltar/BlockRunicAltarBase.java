package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalRunicAltar;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalRunicAltar.ContainerRunicAltarBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarBase;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalRunicAltar.RendererRunicAltarBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalRunicAltar.BlockEntityRunicAltarBase;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

import javax.annotation.Nonnull;

public class BlockRunicAltarBase extends ExtraBotanicalBlock<BlockEntityRunicAltarBase, ContainerRunicAltarBase> {
    public static final RotationShape SHAPE;

    public BlockRunicAltarBase(ModX mod, Class<BlockEntityRunicAltarBase> teClass, AdvancedMenuType<ContainerRunicAltarBase, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenRunicAltarBase::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> {
            return new RendererRunicAltarBase();
        });
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(2.0, 5.0, 2.0, 14.0, 9.0, 14.0), box(6.0, 3.0, 6.0, 10.0, 5.0, 10.0), box(4.0, 1.0, 4.0, 12.0, 3.0, 12.0)));
    }
}
