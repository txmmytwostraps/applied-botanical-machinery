package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalApothecary;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalApothecary;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalApothecaryRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalApothecary;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMechanicalApothecary extends MenuBlockBE<BlockEntityMechanicalApothecary, ContainerMenuMechanicalApothecary> {

    public BlockMechanicalApothecary(ModX mod, Class<BlockEntityMechanicalApothecary> teClass, AdvancedMenuType<ContainerMenuMechanicalApothecary, BlockPos> menu) {
        super(mod, teClass, menu, Properties.ofFullCopy(Blocks.STONE).strength(2, 10).dynamicShape().forceSolidOn(), new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.ItemHandler.BLOCK, (tile, side) -> tile.getItemHandler()));
        builder.registerNamed(null, "fluid_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.FluidHandler.BLOCK, (tile, side) -> tile.getFluidInventory()));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.Item(ItemStackRenderer::createProperties));
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalApothecary::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalApothecaryRenderer());
    }

    @Nonnull
    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntityMechanicalApothecary tile = this.getBlockEntity(level, pos);

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
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 0;
    }

    @Override
    protected boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected boolean shouldDropInventory(Level level, BlockPos pos, BlockState state) {
        return false;
    }
}
