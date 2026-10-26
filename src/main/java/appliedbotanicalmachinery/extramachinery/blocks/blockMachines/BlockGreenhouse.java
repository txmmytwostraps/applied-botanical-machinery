package appliedbotanicalmachinery.extramachinery.blocks.blockMachines;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.ContainerGreenhouse;
import appliedbotanicalmachinery.extramachinery.blocks.screens.ScreenGreenhouse;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.RenderGreenhouse;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.BlockEntityGreenhouse;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

import javax.annotation.Nonnull;

public class BlockGreenhouse extends ExtraBotanicalBlock<BlockEntityGreenhouse, ContainerGreenhouse> {
    public static final RotationShape SHAPE;

    public BlockGreenhouse(ModX mod, Class<BlockEntityGreenhouse> teClass, AdvancedMenuType<ContainerGreenhouse, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "energy_storage", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.EnergyStorage.BLOCK, (tile, side) -> tile));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenGreenhouse::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> {
            return new RenderGreenhouse();
        });
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityGreenhouse){
                ((BlockEntityGreenhouse) blockEntity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    static {
        SHAPE = new RotationShape(box(0, 0, 0, 16, 16, 16));
    }
}
