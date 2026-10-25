package appbot.ae2;

import velrondevs.botania.api.mana.ManaCollector;
import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.spark.SparkAttachable;

public class ManaHelper {

    public static int getCapacity(ManaReceiver receiver) {
        if (receiver instanceof ManaPool pool) {
            return pool.getMaxMana();
        } else if (receiver instanceof ManaCollector collector) {
            return collector.getMaxMana();
        } else if (receiver instanceof SparkAttachable sparkAttachable) {
            return receiver.getCurrentMana() + sparkAttachable.getAvailableSpaceForMana();
        } else if (!receiver.isFull()) {
            return receiver.getCurrentMana() + 1000;
        }

        return 0;
    }
}
