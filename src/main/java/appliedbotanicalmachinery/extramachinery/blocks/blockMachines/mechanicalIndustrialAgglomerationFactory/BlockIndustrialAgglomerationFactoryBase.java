package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalIndustrialAgglomerationFactory;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalIndustrialAgglomerationFactory.ContainerIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory.ScreenIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalIndustrialAgglomerationFactory.RenderIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalIndustrialAgglomerationFactory.BlockEntityIndustrialAgglomerationFactoryBase;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class BlockIndustrialAgglomerationFactoryBase extends ExtraBotanicalBlock<BlockEntityIndustrialAgglomerationFactoryBase, ContainerIndustrialAgglomerationFactoryBase> {
    public static final RotationShape SHAPE;

    public BlockIndustrialAgglomerationFactoryBase(ModX mod, Class<BlockEntityIndustrialAgglomerationFactoryBase> teClass, AdvancedMenuType<ContainerIndustrialAgglomerationFactoryBase, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenIndustrialAgglomerationFactoryBase::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> {
            return new RenderIndustrialAgglomerationFactoryBase();
        });
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(2.6, 0.0, 2.6, 13.4, 4.6, 13.4), box(6.2, 0.0, 6.2, 9.8, 5.3, 9.8)));
    }
}
