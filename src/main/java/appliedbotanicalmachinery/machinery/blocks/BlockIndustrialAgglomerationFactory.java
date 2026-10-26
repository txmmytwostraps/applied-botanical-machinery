package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.tesr.IndustrialAgglomerationFactoryRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityIndustrialAgglomerationFactory;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockIndustrialAgglomerationFactory extends BotanicalBlock<BlockEntityIndustrialAgglomerationFactory, ContainerMenuIndustrialAgglomerationFactory> {

    public BlockIndustrialAgglomerationFactory(ModX mod, Class<BlockEntityIndustrialAgglomerationFactory> teClass, AdvancedMenuType<ContainerMenuIndustrialAgglomerationFactory, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenIndustrialAgglomerationFactory::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new IndustrialAgglomerationFactoryRenderer());
    }
}
