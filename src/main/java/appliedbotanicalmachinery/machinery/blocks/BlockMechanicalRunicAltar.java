package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.ModBlocks;
import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalRunicAltarRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalRunicAltar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;

public class BlockMechanicalRunicAltar extends BotanicalBlock<BlockEntityMechanicalRunicAltar, ContainerMenuMechanicalRunicAltar> {

    public BlockMechanicalRunicAltar(ModX mod, Class<BlockEntityMechanicalRunicAltar> teClass, MenuType<ContainerMenuMechanicalRunicAltar> menu) {
        super(mod, teClass, menu, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerClient(SetupContext ctx) {
        super.registerClient(ctx);
        MenuScreens.register(ModBlocks.mechanicalRunicAltar.menu, ScreenMechanicalRunicAltar::new);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalRunicAltarRenderer());
    }
}
