package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalApothecary;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalApothecary.ContainerApothecaryUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.pattern.BlockEntityApothecaryPattern;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary.ScreenApothecaryUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalApothecary.RenderApothecaryUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary.BlockEntityApothecaryUpgraded;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockApothecaryUpgraded extends MenuBlockBE<BlockEntityApothecaryUpgraded, ContainerApothecaryUpgraded> {
    public static final RotationShape SHAPE;

    public BlockApothecaryUpgraded(ModX mod, Class<BlockEntityApothecaryUpgraded> teClass, AdvancedMenuType<ContainerApothecaryUpgraded, BlockPos> menu) {
        super(mod, teClass, menu, Properties.ofFullCopy(Blocks.STONE).strength(2.0F, 10.0F).dynamicShape().forceSolidOn().noOcclusion(), new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        ExtraBotanicalBlock.registerTileCapabilities(this.getBlockEntityType(), builder);
        builder.registerNamed(null, "fluid_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.FluidHandler.BLOCK, (tile, side) -> tile.getFluidInventory()));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.Item(ItemStackRenderer::createProperties));
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenApothecaryUpgraded::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        BlockEntityRenderers.register(this.getBlockEntityType(), (context) -> new RenderApothecaryUpgraded());
    }

    @Nonnull
    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntityApothecaryPattern tile = this.getBlockEntity(level, pos);
            ItemStack held = player.getMainHandItem();
            FluidActionResult fluidActionResult = FluidUtil.tryEmptyContainer(held, tile.getFluidInventory(), 1000, player, true);
            if (fluidActionResult.isSuccess()) {
                tile.setDispatchable();

                if (!player.isCreative()) {
                    player.setItemInHand(hand, fluidActionResult.getResult());
                }

                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 0;
    }

    public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return true;
    }

    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    static {
        SHAPE = new RotationShape(Shapes.or(ExtraBotanicalBlock.FRAME_SHAPE, box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0), box(0.0, 1.0, 0.0, 1.0, 15.0, 1.0), box(15.0, 1.0, 0.0, 16.0, 15.0, 1.0), box(0.0, 1.0, 15.0, 1.0, 15.0, 16.0), box(15.0, 1.0, 15.0, 16.0, 15.0, 16.0), box(0.0, 15.0, 1.0, 1.0, 16.0, 15.0), box(15.0, 15.0, 1.0, 16.0, 16.0, 15.0), box(0.0, 15.0, 0.0, 16.0, 16.0, 1.0), box(0.0, 15.0, 15.0, 16.0, 16.0, 16.0), box(12.0, 10.0, 4.0, 13.0, 13.0, 12.0), box(3.0, 1.0, 3.0, 13.0, 2.0, 13.0), box(5.0, 2.0, 5.0, 11.0, 9.0, 11.0), box(3.0, 9.0, 3.0, 13.0, 10.0, 13.0), box(3.0, 10.0, 12.0, 13.0, 13.0, 13.0), box(3.0, 10.0, 3.0, 13.0, 13.0, 4.0), box(3.0, 10.0, 4.0, 4.0, 13.0, 12.0)));
    }
}
