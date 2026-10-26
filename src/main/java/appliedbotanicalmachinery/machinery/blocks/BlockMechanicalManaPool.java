package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.ModBlocks;
import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalManaPool;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalManaPool;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalManaPoolRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaPool;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

public class BlockMechanicalManaPool extends BotanicalBlock<BlockEntityMechanicalManaPool, ContainerMenuMechanicalManaPool> {

    public BlockMechanicalManaPool(ModX mod, Class<BlockEntityMechanicalManaPool> teClass, MenuType<ContainerMenuMechanicalManaPool> menu) {
        super(mod, teClass, menu, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(SetupContext ctx) {
        super.registerClient(ctx);
        MenuScreens.register(ModBlocks.mechanicalManaPool.menu, ScreenMechanicalManaPool::new);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalManaPoolRenderer());
    }
}
