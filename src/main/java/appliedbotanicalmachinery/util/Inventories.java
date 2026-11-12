package appliedbotanicalmachinery.util;

import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Slot-based helpers over NeoForge's item {@link ResourceHandler}.
 */
public final class Inventories {

    private Inventories() {
    }

    /**
     * A copy of the stack in the slot. Changing it does not change the inventory. Use {@link #set} for that.
     */
    public static ItemStack get(ResourceHandler<ItemResource> handler, int slot) {
        return ItemUtil.getStack(handler, slot);
    }

    public static void set(IAdvancedItemHandlerModifiable handler, int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            handler.set(slot, ItemResource.EMPTY, 0);
        } else {
            handler.set(slot, ItemResource.of(stack), stack.getCount());
        }
    }

    /**
     * Removes up to {@code amount} items from the slot, ignoring stack size limits of the handler.
     */
    public static void shrink(IAdvancedItemHandlerModifiable handler, int slot, int amount) {
        ItemStack stack = get(handler, slot);
        stack.shrink(amount);
        set(handler, slot, stack);
    }

    /**
     * Inserts into one slot and returns what did not fit.
     */
    public static ItemStack insert(ResourceHandler<ItemResource> handler, int slot, ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(handler, slot, stack, simulate, null);
    }

    /**
     * Inserts into the whole handler and returns what did not fit.
     */
    public static ItemStack insert(ResourceHandler<ItemResource> handler, ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(handler, stack, simulate, null);
    }

    /**
     * Extracts up to {@code amount} items from one slot and returns them.
     */
    public static ItemStack extract(ResourceHandler<ItemResource> handler, int slot, int amount, boolean simulate) {
        ItemResource resource = handler.getResource(slot);
        if (resource.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        try (Transaction tx = Transaction.openRoot()) {
            int extracted = handler.extract(slot, resource, amount, tx);
            if (!simulate) {
                tx.commit();
            }
            return resource.toStack(extracted);
        }
    }

    /**
     * The stack limit of the slot for this stack.
     */
    public static int limit(ResourceHandler<ItemResource> handler, int slot, ItemStack stack) {
        return handler.getCapacityAsInt(slot, stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack));
    }
}
