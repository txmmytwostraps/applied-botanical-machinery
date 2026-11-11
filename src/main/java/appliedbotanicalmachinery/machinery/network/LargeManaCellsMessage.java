package appliedbotanicalmachinery.machinery.network;

import net.neoforged.neoforge.network.registration.HandlerThread;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import appliedbotanicalmachinery.extramachinery.compat.LargeManaCellsClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.moddingx.libx.network.PacketHandler;

import javax.annotation.Nonnull;

/**
 * Tells a joining client whether the server leaves out the 1M to 256M mana cells (MEGA Cells replaces them).
 * Sent when the server syncs its data packs, before the client's recipe viewers start.
 */
public record LargeManaCellsMessage(boolean replaced) implements CustomPacketPayload {

    public static final Type<LargeManaCellsMessage> TYPE = new Type<>(AppliedBotanicalMachinery.getInstance().id("large_mana_cells"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LargeManaCellsMessage> CODEC = ByteBufCodecs.BOOL
            .map(LargeManaCellsMessage::new, LargeManaCellsMessage::replaced).cast();

    @Nonnull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends PacketHandler<LargeManaCellsMessage> {

        public Handler() {
            super(TYPE, PacketFlow.CLIENTBOUND, CODEC, HandlerThread.MAIN);
        }

        @Override
        public void handle(LargeManaCellsMessage msg, IPayloadContext ctx) {
            LargeManaCellsClient.setServerReplaced(msg.replaced());
        }
    }
}
