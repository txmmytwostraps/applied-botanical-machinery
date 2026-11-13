package appliedbotanicalmachinery.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;

import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
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

    /**
     * Saved inventory data in the layout handlers read. Worlds from the Minecraft 1.21.1 version store {Size, Items:
     * [{Slot, ...}]} and the mechanical daisy a "fluids" list next to it. That data comes back as {stacks: [...]} and
     * {fluids: {stacks: [...]}}. Data already in that layout is returned as it is.
     */
    public static ValueInput upgrade(ValueInput input) {
        Optional<ValueInput.ValueInputList> items = input.childrenList("Items");
        if (items.isEmpty() || input.childrenList(StacksResourceHandler.VALUE_IO_KEY).isPresent()) {
            return input;
        }
        List<ItemStack> stacks = new ArrayList<>(
                Collections.nCopies(Math.max(input.getIntOr("Size", 0), 0), ItemStack.EMPTY));
        for (ValueInput entry : items.get()) {
            int slot = entry.getIntOr("Slot", -1);
            if (slot < 0 || entry.getStringOr("id", "").isEmpty()) {
                continue;
            }
            ItemStack stack = entry.read(ItemStack.MAP_CODEC).orElse(ItemStack.EMPTY);
            while (stacks.size() <= slot) {
                stacks.add(ItemStack.EMPTY);
            }
            stacks.set(slot, stack);
        }
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, input.lookup());
        output.store(StacksResourceHandler.VALUE_IO_KEY, ItemStack.OPTIONAL_CODEC.listOf(), stacks);
        input.childrenList("fluids").ifPresent(list -> {
            List<FluidStack> fluids = new ArrayList<>();
            for (ValueInput entry : list) {
                fluids.add(entry.getStringOr("id", "").isEmpty() ? FluidStack.EMPTY
                        : entry.read(FluidStack.MAP_CODEC).orElse(FluidStack.EMPTY));
            }
            output.child("fluids").store(StacksResourceHandler.VALUE_IO_KEY, FluidStack.OPTIONAL_CODEC.listOf(),
                    fluids);
        });
        return TagValueInput.create(ProblemReporter.DISCARDING, input.lookup(), output.buildResult());
    }
}
