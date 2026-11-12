package appliedbotanicalmachinery.data;

import java.util.List;
import java.util.Set;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ABDataGenerator {

    /**
     * The data run is a client data run with --all, so client and server providers are added here.
     */
    public static void onInitializeDataGenerator(GatherDataEvent.Client event) {
        // Block tags for every block of the mod, the Fluix Mana Pool included, come from the machinery datagen
        event.createProvider(ItemTagsProvider::new);
        event.createProvider(RecipeProvider.Runner::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new,
                        LootContextParamSets.BLOCK)),
                lookupProvider));
        event.createProvider(ModelProvider::new);
    }
}
