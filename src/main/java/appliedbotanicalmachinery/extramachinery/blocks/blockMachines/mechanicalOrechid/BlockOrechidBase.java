package appliedbotanicalmachinery.extramachinery.blocks.blockMachines.mechanicalOrechid;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraBotanicalBlock;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalOrechid.ContainerOrechidBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid.ScreenOrechidBase;
import appliedbotanicalmachinery.extramachinery.blocks.tesr.mechanicalOrechid.RenderOrechidBase;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalOrechid.BlockEntityOrechidBase;
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
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

import javax.annotation.Nonnull;

public class BlockOrechidBase extends MenuBlockBE<BlockEntityOrechidBase, ContainerOrechidBase> {

    private static final VoxelShape COLLISION_SHAPE;
    public static final VoxelShape SHAPE;

    public BlockOrechidBase(ModX mod, Class<BlockEntityOrechidBase> teClass, AdvancedMenuType<ContainerOrechidBase, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, properties.strength(2.0F, 10.0F).dynamicShape().forceSolidOn().noOcclusion(), new Item.Properties());
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        ExtraBotanicalBlock.registerTileCapabilities(this.getBlockEntityType(), builder);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenOrechidBase::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new RenderOrechidBase());
    }

    public int getLightDampening(@Nonnull BlockState state) {
        return 0;
    }

    public boolean propagatesSkylightDown(BlockState state) {
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
    public VoxelShape getOcclusionShape(BlockState state) {
        return SHAPE;
    }

    static {
        COLLISION_SHAPE = Shapes.joinUnoptimized(box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), box(5.0, 2.0, 5.0, 11.0, 3.0, 11.0), BooleanOp.OR);
        SHAPE = box(0.0, 0.0, 0.0, 16.0, 11.4, 16.0);
    }
}
