package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.ModBlocks;
import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenAlfheimMarket;
import appliedbotanicalmachinery.machinery.blocks.tesr.AlfheimMarketRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityAlfheimMarket;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

public class BlockAlfheimMarket extends BotanicalBlock<BlockEntityAlfheimMarket, ContainerMenuAlfheimMarket> {

    public BlockAlfheimMarket(ModX mod, Class<BlockEntityAlfheimMarket> teClass, MenuType<ContainerMenuAlfheimMarket> menu) {
        super(mod, teClass, menu, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(SetupContext ctx) {
        super.registerClient(ctx);
        MenuScreens.register(ModBlocks.alfheimMarket.menu, ScreenAlfheimMarket::new);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new AlfheimMarketRenderer());
    }
}
