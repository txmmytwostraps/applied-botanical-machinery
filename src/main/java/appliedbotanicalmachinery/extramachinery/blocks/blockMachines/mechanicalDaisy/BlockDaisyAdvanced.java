package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalDaisy;

import appeng.api.AECapabilities;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalDaisy.ContainerDaisyAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityDaisyPattern;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy.ScreenDaisyAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalDaisy.RenderDaisyAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalDaisy.BlockEntityDaisyAdvanced;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

import javax.annotation.Nonnull;

public class BlockDaisyAdvanced extends MenuBlockBE<BlockEntityDaisyAdvanced, ContainerDaisyAdvanced> {
    private static final VoxelShape COLLISION_SHAPE;
    private static final VoxelShape SHAPE;

    public BlockDaisyAdvanced(ModX mod, Class<BlockEntityDaisyAdvanced> teClass, AdvancedMenuType<ContainerDaisyAdvanced, BlockPos> menu) {
        super(mod, teClass, menu, Properties.ofFullCopy(Blocks.STONE).strength(2.0F, 10.0F).dynamicShape().forceSolidOn().noOcclusion(), new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.ItemHandler.BLOCK, BlockEntityDaisyPattern::getItemHandler));
        builder.registerNamed(null, "fluid_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.FluidHandler.BLOCK, (tile, side) -> tile.getFluidHandler()));
        builder.registerNamed(null, "grid_node_host", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), AECapabilities.IN_WORLD_GRID_NODE_HOST, (tile, side) -> tile));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.Item(ItemStackRenderer::createProperties));
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenDaisyAdvanced::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> {
            return new RenderDaisyAdvanced();
        });
    }

    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 0;
    }

    public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return true;
    }

    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return SHAPE;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityDaisyAdvanced){
                ((BlockEntityDaisyAdvanced) blockEntity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    static {
        COLLISION_SHAPE = Shapes.joinUnoptimized(box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), box(5.0, 2.0, 5.0, 11.0, 3.0, 11.0), BooleanOp.OR);
        SHAPE = box(0.0, 0.0, 0.0, 16.0, 11.4, 16.0);
    }
}
