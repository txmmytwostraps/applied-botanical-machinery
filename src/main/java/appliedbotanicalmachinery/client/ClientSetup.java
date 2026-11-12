package appliedbotanicalmachinery.client;

import java.util.function.Supplier;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Client registrations called from common classes. Blocks hand over a supplier, so their own bytecode never names a
 * client class and still loads on a dedicated server.
 */
public final class ClientSetup {

    private ClientSetup() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends BlockEntity> void blockEntityRenderer(BlockEntityType<? extends T> type,
            Supplier<?> renderer) {
        BlockEntityRenderers.register((BlockEntityType) type,
                context -> (BlockEntityRenderer) renderer.get());
    }
}
