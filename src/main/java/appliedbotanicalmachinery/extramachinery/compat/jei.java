package appliedbotanicalmachinery.extramachinery.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.extramachinery.ModBlocks;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalAlfheimMarket.ScreenAlfheimMarketAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalAlfheimMarket.ScreenAlfheimMarketBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalAlfheimMarket.ScreenAlfheimMarketUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalAlfheimMarket.ScreenAlfheimMarketUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary.ScreenApothecaryAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary.ScreenApothecaryBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary.ScreenApothecaryUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary.ScreenApothecaryUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy.ScreenDaisyAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy.ScreenDaisyBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy.ScreenDaisyUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalDaisy.ScreenDaisyUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory.ScreenIndustrialAgglomerationFactoryAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory.ScreenIndustrialAgglomerationFactoryBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory.ScreenIndustrialAgglomerationFactoryUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalIndustrialAgglomerationFactory.ScreenIndustrialAgglomerationFactoryUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaInfuser.ScreenManaInfuserAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaInfuser.ScreenManaInfuserBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaInfuser.ScreenManaInfuserUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaInfuser.ScreenManaInfuserUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool.ScreenManaPoolAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool.ScreenManaPoolBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool.ScreenManaPoolUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalManaPool.ScreenManaPoolUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid.ScreenOrechidAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid.ScreenOrechidBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid.ScreenOrechidUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalOrechid.ScreenOrechidUpgraded;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarAdvanced;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarUltimate;
import appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalRunicAltar.ScreenRunicAltarUpgraded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import velrondevs.botania.client.integration.jei.*;
import velrondevs.botania.client.integration.jei.orechid.OrechidRecipeCategory;
import velrondevs.masterfulbotany.integration.jei.InfusionCategory;

import javax.annotation.Nonnull;

@JeiPlugin
public class jei implements IModPlugin {
    public jei() {
    }

    @Nonnull
    public ResourceLocation getPluginUid() {
        return AppliedBotanics.id("extra_machinery");
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(ScreenManaPoolBase.class, 89, 56, 35, 11, ManaPoolRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenManaPoolUpgraded.class, 89, 47, 35, 11, ManaPoolRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenManaPoolAdvanced.class, 90, 62, 35, 11, ManaPoolRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenManaPoolUltimate.class, 90, 62, 35, 11, ManaPoolRecipeCategory.TYPE);

        registration.addRecipeClickArea(ScreenRunicAltarBase.class, 102, 41, 11, 37, RunicAltarRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenRunicAltarUpgraded.class, 102, 41, 11, 37, RunicAltarRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenRunicAltarAdvanced.class, 102, 41, 11, 37, RunicAltarRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenRunicAltarUltimate.class, 102, 41, 11, 37, RunicAltarRecipeCategory.TYPE);

        registration.addRecipeClickArea(ScreenDaisyBase.class, 91, 55, 16, 16, PureDaisyRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenDaisyUpgraded.class, 91, 55, 16, 16, PureDaisyRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenDaisyAdvanced.class, 91, 55, 16, 16, PureDaisyRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenDaisyUltimate.class, 97, 63, 16, 16, PureDaisyRecipeCategory.TYPE);

        registration.addRecipeClickArea(ScreenApothecaryBase.class, 92, 35, 11, 37, PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenApothecaryUpgraded.class, 93, 41, 11, 37, PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenApothecaryAdvanced.class, 103, 41, 11, 37, PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenApothecaryUltimate.class, 102, 41, 11, 37, PetalApothecaryRecipeCategory.TYPE);

        registration.addRecipeClickArea(ScreenIndustrialAgglomerationFactoryBase.class, 78, 57, 40, 16, TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenIndustrialAgglomerationFactoryUpgraded.class, 78, 57, 40, 16, TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenIndustrialAgglomerationFactoryAdvanced.class, 78, 57, 40, 16, TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenIndustrialAgglomerationFactoryUltimate.class, 88, 69, 40, 16, TerrestrialAgglomerationRecipeCategory.TYPE);

        if (ModList.get().isLoaded("masterfulbotany")) {
            registration.addRecipeClickArea(ScreenManaInfuserBase.class, 78, 57, 40, 16, InfusionCategory.TYPE);
            registration.addRecipeClickArea(ScreenManaInfuserUpgraded.class, 78, 57, 40, 16, InfusionCategory.TYPE);
            registration.addRecipeClickArea(ScreenManaInfuserAdvanced.class, 78, 57, 40, 16, InfusionCategory.TYPE);
            registration.addRecipeClickArea(ScreenManaInfuserUltimate.class, 88, 69, 40, 16, InfusionCategory.TYPE);
        }

        registration.addRecipeClickArea(ScreenAlfheimMarketBase.class, 90, 53, 16, 16, ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenAlfheimMarketUpgraded.class, 90, 53, 16, 16, ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenAlfheimMarketAdvanced.class, 90, 53, 16, 16, ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenAlfheimMarketUltimate.class, 90, 61, 16, 16, ElvenTradeRecipeCategory.TYPE);

        registration.addRecipeClickArea(ScreenOrechidBase.class, 71, 64, 54, 16, OrechidRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenOrechidUpgraded.class, 76, 64, 54, 16, OrechidRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenOrechidAdvanced.class, 76, 83, 54, 16, OrechidRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenOrechidUltimate.class, 76, 83, 54, 16, OrechidRecipeCategory.TYPE);

    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseManaPool), ManaPoolRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedManaPool), ManaPoolRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedManaPool), ManaPoolRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateManaPool), ManaPoolRecipeCategory.TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseRunicAltar), RunicAltarRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedRunicAltar), RunicAltarRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedRunicAltar), RunicAltarRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateRunicAltar), RunicAltarRecipeCategory.TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseDaisy), PureDaisyRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedDaisy), PureDaisyRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedDaisy), PureDaisyRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateDaisy), PureDaisyRecipeCategory.TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseApothecary), PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedApothecary), PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedApothecary), PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateApothecary), PetalApothecaryRecipeCategory.TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseIndustrialAgglomerationFactory), TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedIndustrialAgglomerationFactory), TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedIndustrialAgglomerationFactory), TerrestrialAgglomerationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateIndustrialAgglomerationFactory), TerrestrialAgglomerationRecipeCategory.TYPE);

        if (ModList.get().isLoaded("masterfulbotany")) {
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseManaInfuser), InfusionCategory.TYPE);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedManaInfuser), InfusionCategory.TYPE);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedManaInfuser), InfusionCategory.TYPE);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateManaInfuser), InfusionCategory.TYPE);
        }

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseAlfheimMarket), ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedAlfheimMarket), ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedAlfheimMarket), ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateAlfheimMarket), ElvenTradeRecipeCategory.TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.baseOrechid), OrechidRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.upgradedOrechid), OrechidRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.advancedOrechid), OrechidRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ultimateOrechid), OrechidRecipeCategory.TYPE);
    }
}