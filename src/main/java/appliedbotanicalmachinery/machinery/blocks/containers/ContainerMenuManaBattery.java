package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityManaBattery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.BaseSlot;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuManaBattery extends BlockEntityMenu<BlockEntityManaBattery> {

    public ContainerMenuManaBattery(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 2, 2);

        IAdvancedItemHandlerModifiable inventory = this.blockEntity.getInventory();
        this.addSlot(new BaseSlot(inventory, inventory, 0, 53, 25));
        this.addSlot(new BaseSlot(inventory, inventory, 1, 107, 25));
        this.layoutPlayerInventorySlots(8, 84);
    }
}
