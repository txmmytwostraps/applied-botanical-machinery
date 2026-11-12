package appliedbotanicalmachinery.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import appliedbotanicalmachinery.ABItems;
import appliedbotanicalmachinery.AppliedBotanics;
import velrondevs.botania.common.lib.BotaniaTags;

import appeng.api.features.P2PTunnelAttunement;

public class ItemTagsProvider extends net.neoforged.neoforge.common.data.ItemTagsProvider {

    public ItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, AppliedBotanics.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(P2PTunnelAttunement.getAttunementTag(ABItems.MANA_P2P_TUNNEL.get()))
                .addTag(BotaniaTags.Items.PETALS);
    }
}
