package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalManaPool;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalManaPool.ContainerManaPoolUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool.ScreenManaPoolUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalManaPool.RenderManaPoolUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolUpgraded;
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

public class BlockManaPoolUpgraded extends ExtraBotanicalBlock<BlockEntityManaPoolUpgraded, ContainerManaPoolUpgraded> {
    public static final RotationShape SHAPE;

    public BlockManaPoolUpgraded(ModX mod, Class<BlockEntityManaPoolUpgraded> teClass, AdvancedMenuType<ContainerManaPoolUpgraded, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenManaPoolUpgraded::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> {
            return new RenderManaPoolUpgraded();
        });
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(2.0, 1.0, 2.0, 14.0, 1.1, 14.0), box(2.0, 1.0, 13.0, 14.0, 6.0, 14.0), box(2.0, 1.0, 2.0, 14.0, 6.0, 3.0), box(13.0, 1.0, 3.0, 14.0, 6.0, 13.0), box(2.0, 1.0, 3.0, 3.0, 6.0, 13.0)));
    }
}
