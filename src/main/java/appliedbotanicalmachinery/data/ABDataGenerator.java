package appliedbotanicalmachinery.data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ABDataGenerator {

    public static void onInitializeDataGenerator(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        // Block tags for every block of the mod, the Fluix Mana Pool included, come from the machinery datagen
        gen.addProvider(event.includeServer(),
                new ItemTagsProvider(output, lookupProvider,
                        CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
                        existingFileHelper));
        gen.addProvider(event.includeServer(), new RecipeProvider(output, lookupProvider));
        gen.addProvider(event.includeServer(), new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new,
                        LootContextParamSets.BLOCK)),
                lookupProvider));

        gen.addProvider(event.includeClient(), new ModelProvider(output, existingFileHelper));
    }
}
