package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.util.SimpleFluidTank;

import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalApothecary;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalApothecary;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalApothecaryRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalApothecary;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
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

    public BlockMechanicalApothecary(ModX mod, Class<BlockEntityMechanicalApothecary> teClass, AdvancedMenuType<ContainerMenuMechanicalApothecary, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, properties.strength(2, 10).dynamicShape().forceSolidOn(), new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.Item.BLOCK, (tile, side) -> tile.getItemHandler()));
        builder.registerNamed(null, "fluid_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.Fluid.BLOCK, (tile, side) -> tile.getFluidInventory()));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalApothecary::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new MechanicalApothecaryRenderer());
    }

    @Nonnull
    @Override
    protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntityMechanicalApothecary tile = this.getBlockEntity(level, pos);

            ItemStack held = player.getMainHandItem();
            if (FluidUtil.interactWithFluidHandler(player, hand, pos, tile.getFluidInventory(), null)) {
                tile.setDispatchable();

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
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
    protected int getLightDampening(@Nonnull BlockState state) {
        return 0;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
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
