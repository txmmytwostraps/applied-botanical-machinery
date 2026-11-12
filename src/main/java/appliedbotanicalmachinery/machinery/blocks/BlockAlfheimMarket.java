package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.tesr.AlfheimMarketRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityAlfheimMarket;
import net.minecraft.core.BlockPos;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockAlfheimMarket extends BotanicalBlock<BlockEntityAlfheimMarket, ContainerMenuAlfheimMarket> {

    public BlockAlfheimMarket(ModX mod, Class<BlockEntityAlfheimMarket> teClass, AdvancedMenuType<ContainerMenuAlfheimMarket, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, false, true, properties);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenAlfheimMarket::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new AlfheimMarketRenderer());
    }
}
