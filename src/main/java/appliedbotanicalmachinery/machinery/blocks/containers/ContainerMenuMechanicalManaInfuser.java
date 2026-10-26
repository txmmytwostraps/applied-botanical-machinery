package appliedbotanicalmachinery.machinery.blocks.containers;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalManaInfuser;
import appliedbotanicalmachinery.machinery.helper.UnrestrictedOutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.moddingx.libx.menu.BlockEntityMenu;

public class ContainerMenuMechanicalManaInfuser extends BlockEntityMenu<BlockEntityMechanicalManaInfuser> {

    public ContainerMenuMechanicalManaInfuser(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 3, 4);

        IItemHandlerModifiable inventory = this.blockEntity.getInventory();
        this.addSlot(new SlotItemHandler(inventory, 0, 61, 83));
        this.addSlot(new SlotItemHandler(inventory, 1, 80, 83));
        this.addSlot(new SlotItemHandler(inventory, 2, 99, 83));
        this.addSlot(new UnrestrictedOutputSlot(inventory, 3, 80, 25));
        this.layoutPlayerInventorySlots(8, 113);
    }
}
