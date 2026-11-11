package appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalRunicAltar;

import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalRunicAltar.BlockEntityRunicAltarUltimate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.BaseSlot;
import org.jetbrains.annotations.NotNull;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerRunicAltarUltimate extends BlockEntityMenu<BlockEntityRunicAltarUltimate> {
    public final static int WIDTH_GUI = 216;
    public final static int HEIGHT_GUI = 140;

    public ContainerRunicAltarUltimate(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 21, 37);
        IAdvancedItemHandlerModifiable inventory = this.blockEntity.getInventory();

        this.addSlot(new BaseSlot(inventory, inventory, 0, 81, 100));
        this.addSlot(new BaseSlot(inventory, inventory, 1, 100, 100));
        this.addSlot(new BaseSlot(inventory, inventory, 2, 119, 100));

        this.addSlot(new BaseSlot(inventory, inventory, 3, 45, 100));
        this.addSlot(new BaseSlot(inventory, inventory, 4, 155, 100));

        int index = this.addSlotBox(inventory, 5, 27, 25, 4, 18, 4, 18);
        this.addSlotBox(inventory, index, 119, 25, 4, 18, 4, 18, UnrestrictedOutputSlot::new);

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