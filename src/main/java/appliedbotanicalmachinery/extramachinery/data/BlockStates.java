package appliedbotanicalmachinery.extramachinery.data;

import appliedbotanicalmachinery.ABBlocks;
import appliedbotanicalmachinery.extramachinery.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.model.BlockStateProviderBase;

public class BlockStates extends BlockStateProviderBase {
    public BlockStates(DatagenContext context) {
        super(context);
    }

    protected void setup() {
        // Applied Botanics generates its own block states
        this.manualState(ABBlocks.FLUIX_MANA_POOL.get());

        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalDaisy);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.alfheimMarket);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalManaPool);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalRunicAltar);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.industrialAgglomerationFactory);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalManaInfuser);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalBrewery);
        this.manualModel(appliedbotanicalmachinery.machinery.ModBlocks.mechanicalApothecary);
        this.orientableBattery(appliedbotanicalmachinery.machinery.ModBlocks.manaBattery);
        this.orientableBattery(appliedbotanicalmachinery.machinery.ModBlocks.manaBatteryCreative);

        this.manualModel(ModBlocks.malachiteDragonstoneBlock);
        this.manualModel(ModBlocks.saffronDragonstoneBlock);
        this.manualModel(ModBlocks.shadowDragonstoneBlock);
        this.manualModel(ModBlocks.crimsonDragonstoneBlock);
        this.manualModel(ModBlocks.aureateIngotBlock);
        this.manualModel(ModBlocks.mazarineIngotBlock);
        this.manualModel(ModBlocks.greenhouse);

        this.manualModel(ModBlocks.baseAlfheimMarket);
        this.manualModel(ModBlocks.upgradedAlfheimMarket);
        this.manualModel(ModBlocks.advancedAlfheimMarket);
        this.manualModel(ModBlocks.ultimateAlfheimMarket);

        this.manualModel(ModBlocks.baseApothecary);
        this.manualModel(ModBlocks.upgradedApothecary);
        this.manualModel(ModBlocks.advancedApothecary);
        this.manualModel(ModBlocks.ultimateApothecary);

        this.manualModel(ModBlocks.baseDaisy);
        this.manualModel(ModBlocks.upgradedDaisy);
        this.manualModel(ModBlocks.advancedDaisy);
        this.manualModel(ModBlocks.ultimateDaisy);

        this.manualModel(ModBlocks.baseManaPool);
        this.manualModel(ModBlocks.upgradedManaPool);
        this.manualModel(ModBlocks.advancedManaPool);
        this.manualModel(ModBlocks.ultimateManaPool);

        this.manualModel(ModBlocks.baseRunicAltar);
        this.manualModel(ModBlocks.upgradedRunicAltar);
        this.manualModel(ModBlocks.advancedRunicAltar);
        this.manualModel(ModBlocks.ultimateRunicAltar);

        this.manualModel(ModBlocks.baseIndustrialAgglomerationFactory);
        this.manualModel(ModBlocks.upgradedIndustrialAgglomerationFactory);
        this.manualModel(ModBlocks.advancedIndustrialAgglomerationFactory);
        this.manualModel(ModBlocks.ultimateIndustrialAgglomerationFactory);

        this.manualModel(ModBlocks.baseOrechid);
        this.manualModel(ModBlocks.upgradedOrechid);
        this.manualModel(ModBlocks.advancedOrechid);
        this.manualModel(ModBlocks.ultimateOrechid);

        this.manualState(ModBlocks.baseManaInfuser);
        this.manualState(ModBlocks.upgradedManaInfuser);
        this.manualState(ModBlocks.advancedManaInfuser);
        this.manualState(ModBlocks.ultimateManaInfuser);

        this.manualModel(ModBlocks.jadedAmaranthus);
    }

    private void orientableBattery(Block block) {
        //noinspection ConstantConditions
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        this.manualModel(block, this.models().orientable(name,
                this.modLoc("block/" + name + "_side"),
                this.modLoc("block/" + name + "_front"),
                this.modLoc("block/" + name + "_top")));
    }
}
