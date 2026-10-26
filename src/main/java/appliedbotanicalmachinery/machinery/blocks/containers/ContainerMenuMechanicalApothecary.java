package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalApothecary;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalApothecary extends BlockEntityMenu<BlockEntityMechanicalApothecary> {

    public ContainerMenuMechanicalApothecary(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 17, 21);

        IItemHandlerModifiable inventory = this.blockEntity.getInventory();
        this.addSlot(new SlotItemHandler(inventory, 0, 90, 43));
        int index = this.addSlotBox(inventory, 1, 8, 26, 4, 18, 4, 18);
        this.addSlotBox(inventory, index, 118, 54, 2, 18, 2, 18, UnrestrictedOutputSlot::new);
        this.layoutPlayerInventorySlots(18, 113);
    }
}
