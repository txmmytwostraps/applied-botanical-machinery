package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalTile;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaPool;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.BaseSlot;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalManaPool extends BlockEntityMenu<BlockEntityMechanicalManaPool> {

    public ContainerMenuMechanicalManaPool(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 2, 3);

        IAdvancedItemHandlerModifiable inventory = ((BotanicalTile) this.blockEntity).getInventory();
        this.addSlot(new BaseSlot(inventory, inventory, 0, 53, 47));
        this.addSlot(new BaseSlot(inventory, inventory, 1, 53, 25));
        this.addSlot(new UnrestrictedOutputSlot(inventory, 2, 111, 37));
        this.layoutPlayerInventorySlots(8, 84);
    }
}
