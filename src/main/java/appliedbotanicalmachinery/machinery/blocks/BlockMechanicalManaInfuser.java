package appliedbotanicalmachinery.machinery.blocks;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalBlock;
import appliedbotanicalmachinery.machinery.blocks.containers.ContainerMenuMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.blocks.screens.ScreenMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.blocks.tesr.MechanicalManaInfuserRenderer;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.compat.MasterfulBotanyCompat;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;
import org.moddingx.libx.registration.util.ClientExtensionInfo;

import javax.annotation.Nonnull;

public class BlockMechanicalManaInfuser extends BotanicalBlock<BlockEntityMechanicalManaInfuser, ContainerMenuMechanicalManaInfuser> {

    private final Item item;

    public BlockMechanicalManaInfuser(ModX mod, Class<BlockEntityMechanicalManaInfuser> teClass, AdvancedMenuType<ContainerMenuMechanicalManaInfuser, BlockPos> menu) {
        super(mod, teClass, menu, false, true, null);
        this.item = new BlockItem(this, new Item.Properties()) {

            @Override
            public boolean isEnabled(@Nonnull FeatureFlagSet enabledFeatures) {
                return MasterfulBotanyCompat.isLoaded();
            }
        };
    }

    @Override
    public void registerClientAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerClientAdditional(ctx, builder);
        builder.register(null, new ClientExtensionInfo.MenuScreen<>(this.menu, ScreenMechanicalManaInfuser::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setupClient(SetupContext ctx) {
        super.setupClient(ctx);
        BlockEntityRenderers.register(this.getBlockEntityType(), context -> new MechanicalManaInfuserRenderer());
    }

    @Override
    public boolean isEnabled(@Nonnull FeatureFlagSet enabledFeatures) {
        return MasterfulBotanyCompat.isLoaded();
    }

    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        super.registerAdditional(ctx, builder);
        builder.register(Registries.ITEM, this.item);
    }
}
