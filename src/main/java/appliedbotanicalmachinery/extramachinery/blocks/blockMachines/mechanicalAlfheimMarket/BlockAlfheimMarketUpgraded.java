package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalAlfheimMarket;

import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalAlfheimMarket.ContainerAlfheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalAlfheimMarket.ScreenAlfheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalAlfheimMarket.RenderAlpheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketUpgraded;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

import javax.annotation.Nonnull;

public class
BlockAlfheimMarketUpgraded extends ExtraBotanicalBlock<BlockEntityAlfheimMarketUpgraded, ContainerAlfheimMarketUpgraded> {
    public static final RotationShape SHAPE;

    public BlockAlfheimMarketUpgraded(ModX mod, Class<BlockEntityAlfheimMarketUpgraded> teClass, MenuType<ContainerAlfheimMarketUpgraded> menu) {
        super(mod, teClass, menu, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        super.registerClient(ctx);
        MenuScreens.register(ModBlocks.upgradedAlfheimMarket.menu, ScreenAlfheimMarketUpgraded::new);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> new RenderAlpheimMarketUpgraded());
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(4.4, 1.0, 8.8, 11.6, 13.0, 11.2), box(0.0, 0.0, 8.8, 0.0, 0.0, 11.2), box(3.2, 0.0, 3.6, 6.8, 7.4, 7.2), box(8.8, 0.0, 3.6, 12.4, 7.4, 7.2)));
    }
}
