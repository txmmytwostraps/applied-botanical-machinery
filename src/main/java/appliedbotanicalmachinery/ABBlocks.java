package appliedbotanicalmachinery;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import appliedbotanicalmachinery.block.FluixPool;
import velrondevs.botania.registry.BotaniaBlocks;

public class ABBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AppliedBotanics.MOD_ID);

    public static final DeferredBlock<Block> FLUIX_MANA_POOL = BLOCKS.registerBlock("fluix_mana_pool", FluixPool::new,
            () -> BlockBehaviour.Properties.ofFullCopy(BotaniaBlocks.livingrock));

    public static void initialize(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
