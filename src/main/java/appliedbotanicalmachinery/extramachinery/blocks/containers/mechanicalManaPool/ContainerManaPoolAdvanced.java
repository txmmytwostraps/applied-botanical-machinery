package appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalManaPool;

import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalManaPool.BlockEntityManaPoolAdvanced;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerManaPoolAdvanced extends BlockEntityMenu<BlockEntityManaPoolAdvanced> {
    public final static int WIDTH_GUI = 196;
    public final static int HEIGHT_GUI = 124;

    public ContainerManaPoolAdvanced(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 11, 17);
        IItemHandlerModifiable inventory = this.blockEntity.getInventory();

        this.addSlot(new SlotItemHandler(inventory, 0, 98, 72));
        this.addSlot(new SlotItemHandler(inventory, 1, 98, 36));

        int index = this.addSlotBox(inventory, 2, 33, 36, 3, 18, 3, 18);
        this.addSlotBox(inventory, index, 128, 36, 2, 18, 3, 18, UnrestrictedOutputSlot::new);

        int[] x_y = ScreenAddInventory.getCoordInventorySlot(ScreenInventory.ADVANCED, WIDTH_GUI, HEIGHT_GUI);
        this.layoutPlayerInventorySlots(x_y[0], x_y[1]);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (level != null && level.getBlockState(pos).isAir()) {
            player.closeContainer();
            return false;
        }
        return super.stillValid(player);
    }
}
