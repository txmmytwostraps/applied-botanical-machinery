package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalTile;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalRunicAltar;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.BaseSlot;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalRunicAltar extends BlockEntityMenu<BlockEntityMechanicalRunicAltar> {

    public ContainerMenuMechanicalRunicAltar(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 17, 33);

        IAdvancedItemHandlerModifiable inventory = ((BotanicalTile) this.blockEntity).getInventory();
        this.addSlot(new BaseSlot(inventory, inventory, 0, 90, 43));
        int index = this.addSlotBox(inventory, 1, 8, 26, 4, 18, 4, 18);
        this.addSlotBox(inventory, index, 118, 26, 4, 18, 4, 18, UnrestrictedOutputSlot::new);
        this.layoutPlayerInventorySlots(28, 113);
    }
}
