package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalTile;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalBrewery;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.BaseSlot;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalBrewery extends BlockEntityMenu<BlockEntityMechanicalBrewery> {

    public ContainerMenuMechanicalBrewery(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 7, 8);

        IAdvancedItemHandlerModifiable inventory = ((BotanicalTile) this.blockEntity).getInventory();
        this.addSlot(new BaseSlot(inventory, inventory, 0, 44, 48));
        this.addSlot(new BaseSlot(inventory, inventory, 1, 29, 18));
        this.addSlot(new BaseSlot(inventory, inventory, 2, 59, 18));
        this.addSlot(new BaseSlot(inventory, inventory, 3, 14, 48));
        this.addSlot(new BaseSlot(inventory, inventory, 4, 74, 48));
        this.addSlot(new BaseSlot(inventory, inventory, 5, 29, 78));
        this.addSlot(new BaseSlot(inventory, inventory, 6, 59, 78));
        this.addSlot(new UnrestrictedOutputSlot(inventory, 7, 128, 49));
        this.layoutPlayerInventorySlots(8, 110);
    }
}
