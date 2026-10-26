package appliedbotanicalmachinery.integration.emi;

import appliedbotanicalmachinery.ABBlocks;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import velrondevs.botania.client.integration.emi.BotaniaEmiPlugin;

public class ABEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(BotaniaEmiPlugin.MANA_INFUSION,
                EmiStack.of(ABBlocks.FLUIX_MANA_POOL.get()));
    }
}
