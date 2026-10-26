package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.tesr.AlfheimMarketRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityAlfheimMarket;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockAlfheimMarket extends BotanicalBlock<BlockEntityAlfheimMarket, ContainerMenuAlfheimMarket> {

    public BlockAlfheimMarket(ModX mod, Class<BlockEntityAlfheimMarket> teClass, AdvancedMenuType<ContainerMenuAlfheimMarket, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenAlfheimMarket::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new AlfheimMarketRenderer());
    }
}
