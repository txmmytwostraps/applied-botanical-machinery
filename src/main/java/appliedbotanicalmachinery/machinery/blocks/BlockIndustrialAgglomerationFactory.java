package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.ModBlocks;
import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenIndustrialAgglomerationFactory;
import appliedbotanicalmachinery.machinery.blocks.tesr.IndustrialAgglomerationFactoryRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityIndustrialAgglomerationFactory;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

public class BlockIndustrialAgglomerationFactory extends BotanicalBlock<BlockEntityIndustrialAgglomerationFactory, ContainerMenuIndustrialAgglomerationFactory> {

    public BlockIndustrialAgglomerationFactory(ModX mod, Class<BlockEntityIndustrialAgglomerationFactory> teClass, MenuType<ContainerMenuIndustrialAgglomerationFactory> menu) {
        super(mod, teClass, menu, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(SetupContext ctx) {
        super.registerClient(ctx);
        MenuScreens.register(ModBlocks.industrialAgglomerationFactory.menu, ScreenIndustrialAgglomerationFactory::new);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new IndustrialAgglomerationFactoryRenderer());
    }
}
