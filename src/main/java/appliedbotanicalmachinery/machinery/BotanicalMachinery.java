package appliedbotanicalmachinery.machinery;

import appliedbotanicalmachinery.machinery.data.*;
import appliedbotanicalmachinery.machinery.network.BotanicalMachineryNetwork;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.moddingx.libx.datagen.DatagenSystem;
import org.moddingx.libx.mod.ModXRegistration;
import org.moddingx.libx.registration.RegistrationBuilder;

import javax.annotation.Nonnull;

@Mod("appliedbotanicalmachinery")
public final class BotanicalMachinery extends ModXRegistration {

    private static BotanicalMachinery instance;
    private static BotanicalMachineryNetwork network;
    private static BotanicalTab tab;

    public BotanicalMachinery() {
        instance = this;
        network = new BotanicalMachineryNetwork(this);
        tab = new BotanicalTab(this);

        DatagenSystem.create(this, system -> {
            system.addDataProvider(BlockStates::new);
            system.addDataProvider(CommonTags::new);
            system.addDataProvider(ItemModels::new);
            system.addDataProvider(LootTables::new);
            system.addDataProvider(Recipes::new);
            system.addDataProvider(MythicRecipes::new);
        });
    }

    @Nonnull
    public static BotanicalMachinery getInstance() {
        return instance;
    }

    @Nonnull
    public static BotanicalMachineryNetwork getNetwork() {
        return network;
    }

    @Nonnull
    public static BotanicalTab getTab() {
        return tab;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        // NO-OP
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        // NO-OP
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        // NO-OP
    }
}
