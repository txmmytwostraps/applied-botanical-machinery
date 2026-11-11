package appliedbotanicalmachinery.machinery.network;

import appliedbotanicalmachinery.extramachinery.init.InitAppbot;

import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import net.neoforged.neoforge.common.NeoForge;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityManaBattery;
import net.neoforged.neoforge.network.PacketDistributor;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.network.NetworkX;

public class BotanicalMachineryNetwork extends NetworkX {

    public BotanicalMachineryNetwork(ModX mod) {
        super(mod);

        this.register(new ManaBatteryLockedMessage.Handler());
        this.register(new LargeManaCellsMessage.Handler());
        NeoForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> {
            LargeManaCellsMessage msg = new LargeManaCellsMessage(InitAppbot.replacedByMegaCells());
            event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, msg));
        });
    }

    @Override
    protected String getVersion() {
        return "3";
    }

    public void updateLockedState(BlockEntityManaBattery tile) {
        if (tile.getLevel() != null && tile.getLevel().isClientSide()) {
            ClientPacketDistributor.sendToServer(new ManaBatteryLockedMessage(tile.getBlockPos(), tile.isSlot1Locked(), tile.isSlot2Locked()));
        }
    }
}
