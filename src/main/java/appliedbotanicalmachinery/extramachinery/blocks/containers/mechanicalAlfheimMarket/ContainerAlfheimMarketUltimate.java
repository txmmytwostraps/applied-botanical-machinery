package appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalAlfheimMarket;

import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalAlfheimMarket.BlockEntityAlfheimMarketUltimate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerAlfheimMarketUltimate extends BlockEntityMenu<BlockEntityAlfheimMarketUltimate> {
    public final static int WIDTH_GUI = 196;
    public final static int HEIGHT_GUI = 140;

    public ContainerAlfheimMarketUltimate(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 13, 25);
        IItemHandlerModifiable inventory = this.blockEntity.getInventory();

        this.addSlot(new SlotItemHandler(inventory, 0, 90, 96));

        int index = this.addSlotBox(inventory, 1, 29, 34, 3, 18, 4, 18);
        this.addSlotBox(inventory, index, 115, 34, 3, 18, 4, 18, UnrestrictedOutputSlot::new);

        int[] x_y = ScreenAddInventory.getCoordInventorySlot(ScreenInventory.ULTIMATE, WIDTH_GUI, HEIGHT_GUI);
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
