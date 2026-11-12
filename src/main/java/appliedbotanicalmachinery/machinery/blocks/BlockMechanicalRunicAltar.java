package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.client.ClientSetup;

import net.minecraft.world.level.block.state.BlockBehaviour;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalRunicAltarRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalRunicAltar;
import net.minecraft.core.BlockPos;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

public class BlockMechanicalRunicAltar extends BotanicalBlock<BlockEntityMechanicalRunicAltar, ContainerMenuMechanicalRunicAltar> {

    public BlockMechanicalRunicAltar(ModX mod, Class<BlockEntityMechanicalRunicAltar> teClass, AdvancedMenuType<ContainerMenuMechanicalRunicAltar, BlockPos> menu, BlockBehaviour.Properties properties) {
        super(mod, teClass, menu, false, true, properties);
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalRunicAltar::new));
    }

    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        ClientSetup.blockEntityRenderer(this.getBlockEntityType(), () -> new MechanicalRunicAltarRenderer());
    }
}
