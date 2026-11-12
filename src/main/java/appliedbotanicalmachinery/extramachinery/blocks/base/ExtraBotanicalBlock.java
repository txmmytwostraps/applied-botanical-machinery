package appliedbotanicalmachinery.extramachinery.blocks.base;

import net.minecraft.world.level.block.state.BlockBehaviour;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.BlockItem;

import net.minecraft.world.level.storage.TagValueOutput;

import net.minecraft.util.ProblemReporter;

import net.minecraft.core.Direction;

import appeng.api.AECapabilities;
import appeng.api.networking.IInWorldGridNodeHost;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.BlockEntityMenu;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.render.ItemStackRenderer;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.api.BotaniaClientCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtraBotanicalBlock<T extends ExtraBotanicalTile, C extends BlockEntityMenu<T>> extends MenuBlockBE<T, C> {
    public static final VoxelShape FRAME_SHAPE = Shapes.or(box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0), box(0.0, 0.0, 0.0, 1.0, 16.0, 1.0), box(15.0, 0.0, 0.0, 16.0, 16.0, 1.0), box(0.0, 0.0, 15.0, 1.0, 16.0, 16.0), box(15.0, 0.0, 15.0, 16.0, 16.0, 16.0), box(0.0, 15.0, 0.0, 1.0, 16.0, 16.0), box(0.0, 15.0, 0.0, 16.0, 16.0, 1.0), box(15.0, 15.0, 0.0, 16.0, 16.0, 16.0), box(0.0, 15.0, 15.0, 16.0, 16.0, 16.0));
    public final boolean fullCube;
    public final boolean specialRender;

    public ExtraBotanicalBlock(ModX mod, Class<T> teClass, AdvancedMenuType<C, ? super BlockPos> menu, boolean fullCube, boolean specialRender, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, fullCube ? properties.strength(2.0F, 10.0F) : properties.strength(2.0F, 10.0F).dynamicShape().noOcclusion().forceSolidOn(), new Item.Properties());
        this.fullCube = fullCube;
        this.specialRender = specialRender;
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        registerTileCapabilities(this.getBlockEntityType(), builder);
    }

    public static <T extends ExtraBotanicalTile> void registerTileCapabilities(BlockEntityType<T> type, EntryCollector builder) {
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(type, Capabilities.Item.BLOCK, ExtraBotanicalTile::getItemHandler));
        builder.registerNamed(null, "mana_receiver", new CapabilityInfo.BlockEntity<>(type, BotaniaCapabilities.MANA_RECEIVER, (tile, side) -> tile.actAsMana() ? tile : null));
        builder.registerNamed(null, "spark_attachable", new CapabilityInfo.BlockEntity<>(type, BotaniaCapabilities.SPARK_ATTACHABLE, (tile, side) -> tile.actAsMana() ? tile : null));
        builder.registerNamed(null, "grid_node_host", new CapabilityInfo.BlockEntity<>(type, AECapabilities.IN_WORLD_GRID_NODE_HOST, (tile, side) -> tile instanceof IInWorldGridNodeHost host ? host : null));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.registerNamed(null, "wand_hud", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), BotaniaClientCapabilities.WAND_HUD, (tile, side) -> tile.actAsMana() ? tile : null));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        if (this.specialRender) {
            ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        }
    }

    @Nonnull
    @Override
    public ItemStack getCloneItemStack(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean includeData, @Nonnull Player player) {
        ItemStack stack = new ItemStack(this);
        if (level.getBlockEntity(pos) instanceof ExtraBotanicalTile blockEntity) {
            // the machine keeps its contents when picked, with or without includeData
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LogUtils.getLogger())) {
                TagValueOutput output = TagValueOutput.createWithContext(reporter, level.registryAccess());
                blockEntity.saveCustomOnly(output);
                BlockItem.setBlockEntityData(stack, blockEntity.getType(), output);
            }
        }
        return stack;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Direction direction) {
        return this.getBlockEntity(level, pos).getComparatorOutput();
    }

    @Override
    protected int getLightDampening(@Nonnull BlockState state) {
        return !this.fullCube ? 0 : super.getLightDampening(state);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return !this.fullCube;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return !this.fullCube;
    }

    @Override
    @Nonnull
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return !this.fullCube ? FRAME_SHAPE : super.getShape(state, level, pos, context);
    }

    @Override
    protected boolean shouldDropInventory(Level level, BlockPos pos, BlockState state) {
        return true;
    }
}
