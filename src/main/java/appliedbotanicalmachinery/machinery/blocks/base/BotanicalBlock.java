package appliedbotanicalmachinery.machinery.blocks.base;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.BlockEntityMenu;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.CapabilityInfo;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.api.BotaniaClientCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BotanicalBlock<T extends BotanicalTile, C extends BlockEntityMenu<T>> extends MenuBlockBE<T, C> {

    public final boolean fullCube;
    public final boolean specialRender;

    public BotanicalBlock(ModX mod, Class<T> teClass, AdvancedMenuType<C, ? super BlockPos> menu, boolean fullCube, boolean specialRender) {
        this(mod, teClass, menu, fullCube, specialRender, new Item.Properties());
    }

    public BotanicalBlock(ModX mod, Class<T> teClass, AdvancedMenuType<C, ? super BlockPos> menu, boolean fullCube, boolean specialRender, @Nullable Item.Properties itemProperties) {
        super(mod, teClass, menu, fullCube ?
                        Properties.ofFullCopy(Blocks.STONE).strength(2, 10)
                        : Properties.ofFullCopy(Blocks.STONE).strength(2, 10).dynamicShape().noOcclusion().forceSolidOn(),
                itemProperties
        );
        this.fullCube = fullCube;
        this.specialRender = specialRender;
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.registerNamed(null, "item_handler", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), Capabilities.ItemHandler.BLOCK, (tile, side) -> tile.getItemHandler()));
        builder.registerNamed(null, "mana_receiver", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), BotaniaCapabilities.MANA_RECEIVER, (tile, side) -> tile.actAsMana() ? tile : null));
        builder.registerNamed(null, "spark_attachable", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), BotaniaCapabilities.SPARK_ATTACHABLE, (tile, side) -> tile.actAsMana() ? tile : null));
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.registerNamed(null, "wand_hud", new CapabilityInfo.BlockEntity<>(this.getBlockEntityType(), BotaniaClientCapabilities.WAND_HUD, (tile, side) -> tile.actAsMana() ? tile : null));
        if (this.hasItemRenderer()) {
            builder.register(null, new ClientExtensionInfo.Item(ItemStackRenderer::createProperties));
        }
    }

    protected boolean hasItemRenderer() {
        return true;
    }

    @Override
    public void setupClient(SetupContext ctx) {
        if (this.specialRender) {
            ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
        }
    }

    @Nonnull
    @Override
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        ItemStack stack = new ItemStack(this);
        if (level.getBlockEntity(pos) instanceof BotanicalTile blockEntity) {
            blockEntity.saveToItem(stack, level.registryAccess());
        }
        return stack;
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
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        return this.getBlockEntity(level, pos).getComparatorOutput();
    }

    @Override
    protected int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return (!this.fullCube) ? 0 : super.getLightBlock(state, level, pos);
    }

    @Override
    protected boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return !this.fullCube;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return !this.fullCube;
    }

    @Override
    protected boolean shouldDropInventory(Level level, BlockPos pos, BlockState state) {
        return false;
    }
}
