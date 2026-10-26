package appliedbotanicalmachinery.machinery.network;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityManaBattery;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.moddingx.libx.LibX;
import org.moddingx.libx.network.PacketHandler;

import javax.annotation.Nonnull;

public record ManaBatteryLockedMessage(BlockPos pos, boolean locked1, boolean locked2) implements CustomPacketPayload {

    public static final Type<ManaBatteryLockedMessage> TYPE = new Type<>(AppliedBotanicalMachinery.getInstance().resource("mana_battery_locked"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManaBatteryLockedMessage> CODEC = StreamCodec.of(
            (buffer, msg) -> {
                buffer.writeBlockPos(msg.pos);
                buffer.writeBoolean(msg.locked1);
                buffer.writeBoolean(msg.locked2);
            }, buffer -> new ManaBatteryLockedMessage(buffer.readBlockPos(), buffer.readBoolean(), buffer.readBoolean())
    );

    @Nonnull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends PacketHandler<ManaBatteryLockedMessage> {

        public Handler() {
            super(TYPE, PacketFlow.SERVERBOUND, CODEC, HandlerThread.MAIN);
        }

        @Override
        public void handle(ManaBatteryLockedMessage msg, IPayloadContext ctx) {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }

            ServerLevel level = (ServerLevel) player.level();
            //noinspection deprecation
            if (level.hasChunkAt(msg.pos)) {
                BlockEntity te = level.getBlockEntity(msg.pos);
                if (te instanceof BlockEntityManaBattery) {
                    ((BlockEntityManaBattery) te).setSlot1Locked(msg.locked1);
                    ((BlockEntityManaBattery) te).setSlot2Locked(msg.locked2);
                    LibX.getNetwork().updateBE(level, msg.pos);
                }
            }
        }
    }
}
