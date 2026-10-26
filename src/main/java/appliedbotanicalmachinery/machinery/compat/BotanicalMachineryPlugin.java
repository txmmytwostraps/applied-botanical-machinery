package appliedbotanicalmachinery.machinery.compat;

import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.machinery.ModBlocks;
import appliedbotanicalmachinery.machinery.blocks.screens.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import velrondevs.botania.client.integration.jei.*;
import velrondevs.masterfulbotany.integration.jei.InfusionCategory;

import javax.annotation.Nonnull;

@JeiPlugin
public class BotanicalMachineryPlugin implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return AppliedBotanics.id("machinery");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ScreenAlfheimMarket.class, 77, 36, 22, 15, ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenMechanicalApothecary.class, 87, 65, 22, 15, PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenMechanicalBrewery.class, 96, 48, 22, 15, BreweryRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenMechanicalDaisy.class, 24, 16, 24, 48, PureDaisyRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenMechanicalManaPool.class, 77, 36, 22, 15, ManaPoolRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenMechanicalRunicAltar.class, 87, 65, 22, 15, RunicAltarRecipeCategory.TYPE);
        registration.addRecipeClickArea(ScreenIndustrialAgglomerationFactory.class, 73, 51, 30, 25, TerrestrialAgglomerationRecipeCategory.TYPE);
        if (MasterfulBotanyCompat.isLoaded()) {
            registration.addRecipeClickArea(ScreenMechanicalManaInfuser.class, 73, 51, 30, 25, InfusionCategory.TYPE);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.alfheimMarket), ElvenTradeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalApothecary), PetalApothecaryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalBrewery), BreweryRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalDaisy), PureDaisyRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalManaPool), ManaPoolRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalRunicAltar), RunicAltarRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.industrialAgglomerationFactory), TerrestrialAgglomerationRecipeCategory.TYPE);
        if (MasterfulBotanyCompat.isLoaded()) {
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.mechanicalManaInfuser), InfusionCategory.TYPE);
        }
    }
}
