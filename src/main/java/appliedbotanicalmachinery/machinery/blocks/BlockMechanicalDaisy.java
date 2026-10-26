package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalDaisy;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalDaisy;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalDaisyRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalDaisy;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

import javax.annotation.Nonnull;

public class BlockMechanicalDaisy extends MenuBlockBE<BlockEntityMechanicalDaisy, ContainerMenuMechanicalDaisy> {

    private static final VoxelShape COLLISION_SHAPE = Shapes.joinUnoptimized(
            box(0, 0, 0, 16, 2, 16),
            box(5, 2, 5, 11, 3, 11),
            BooleanOp.OR
    );

    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 11.4, 16);

    public BlockMechanicalDaisy(ModX mod, Class<BlockEntityMechanicalDaisy> teClass, AdvancedMenuType<ContainerMenuMechanicalDaisy, BlockPos> menu) {
        super(mod, teClass, menu, Properties.ofFullCopy(Blocks.STONE).strength(2, 10).dynamicShape().forceSolidOn(),
                new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.ItemHandler.BLOCK, BlockEntityMechanicalDaisy::getItemHandler));
        builder.registerNamed(null, "fluid_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.FluidHandler.BLOCK, (tile, side) -> tile.getInventory()));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.Item(ItemStackRenderer::createProperties));
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalDaisy::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalDaisyRenderer());
    }

    @Override
    protected int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 0;
    }

    @Override
    protected boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return true;
    }

    @Nonnull
    @Override
    protected VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Nonnull
    @Override
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    @Override
    protected VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected boolean shouldDropInventory(Level level, BlockPos pos, BlockState state) {
        return false;
    }
}
