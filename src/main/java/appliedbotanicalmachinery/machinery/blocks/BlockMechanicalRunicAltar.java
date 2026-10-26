package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalRunicAltarRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalRunicAltar;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockMechanicalRunicAltar extends BotanicalBlock<BlockEntityMechanicalRunicAltar, ContainerMenuMechanicalRunicAltar> {

    public BlockMechanicalRunicAltar(ModX mod, Class<BlockEntityMechanicalRunicAltar> teClass, AdvancedMenuType<ContainerMenuMechanicalRunicAltar, BlockPos> menu) {
        super(mod, teClass, menu, false, true);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalRunicAltar::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalRunicAltarRenderer());
    }
}
