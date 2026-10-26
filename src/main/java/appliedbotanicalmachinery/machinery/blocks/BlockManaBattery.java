package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuManaBattery;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenManaBattery;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityManaBattery;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;
import org.moddingx.libx.render.ItemStackRenderer;

public class BlockManaBattery extends BotanicalBlock<BlockEntityManaBattery, ContainerMenuManaBattery> {

    public final Variant variant;

    public BlockManaBattery(ModX mod, Class<BlockEntityManaBattery> teClass, AdvancedMenuType<ContainerMenuManaBattery, BlockPos> menu, Variant variant) {
        super(mod, teClass, menu, true, false);
        this.variant = variant;
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenManaBattery::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), true);
    }

    @Override
    protected boolean hasItemRenderer() {
        return false;
    }

    public enum Variant {
        CREATIVE,
        NORMAL
    }
}
