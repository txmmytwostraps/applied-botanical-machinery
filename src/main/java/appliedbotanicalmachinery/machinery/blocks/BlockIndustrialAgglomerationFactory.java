package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.tesr.IndustrialAgglomerationFactoryRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityIndustrialAgglomerationFactory;
import net.minecraft.core.BlockPos;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockIndustrialAgglomerationFactory extends BotanicalBlock<BlockEntityIndustrialAgglomerationFactory, ContainerMenuIndustrialAgglomerationFactory> {

    public BlockIndustrialAgglomerationFactory(ModX mod, Class<BlockEntityIndustrialAgglomerationFactory> teClass, AdvancedMenuType<ContainerMenuIndustrialAgglomerationFactory, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, false, true, properties);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenIndustrialAgglomerationFactory::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new IndustrialAgglomerationFactoryRenderer());
    }
}
