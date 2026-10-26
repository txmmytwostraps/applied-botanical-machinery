package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.base.BotanicalTile;
import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalBrewery;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalBrewery extends BlockEntityMenu<BlockEntityMechanicalBrewery> {

    public ContainerMenuMechanicalBrewery(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 7, 8);

        IItemHandlerModifiable inventory = ((BotanicalTile) this.blockEntity).getInventory();
        this.addSlot(new SlotItemHandler(inventory, 0, 44, 48));
        this.addSlot(new SlotItemHandler(inventory, 1, 29, 18));
        this.addSlot(new SlotItemHandler(inventory, 2, 59, 18));
        this.addSlot(new SlotItemHandler(inventory, 3, 14, 48));
        this.addSlot(new SlotItemHandler(inventory, 4, 74, 48));
        this.addSlot(new SlotItemHandler(inventory, 5, 29, 78));
        this.addSlot(new SlotItemHandler(inventory, 6, 59, 78));
        this.addSlot(new UnrestrictedOutputSlot(inventory, 7, 128, 49));
        this.layoutPlayerInventorySlots(8, 110);
    }
}
