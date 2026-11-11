package appliedbotanicalmachinery.extramachinery.util;

import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.inventory.VanillaWrapper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * LibX's BaseItemStackHandler with slot limits that can be changed after it is built.
 */
public class ResizeBaseItemStackHandler extends ItemStacksResourceHandler implements IAdvancedItemHandlerModifiable {

    private final int size;
    private final int defaultSlotLimit;
    private final Set<Integer> insertionOnlySlots;
    private final Set<Integer> outputSlots;
    private static Map<Integer, Integer> slotLimits;
    private final Map<Integer, Predicate<ItemStack>> slotValidators;
    private final Consumer<Integer> contentsChanged;
    private final List<SlotJournal> slotJournals;
    private Container vanilla = null;
    private Unrestricted unrestricted = null;

    private ResizeBaseItemStackHandler(int size, int defaultSlotLimit, Set<Integer> insertionOnlySlots, Set<Integer> outputSlots, Map<Integer, Integer> slotLimits, Map<Integer, Predicate<ItemStack>> slotValidators, Consumer<Integer> contentsChanged) {
        super(size);
        this.size = size;
        this.defaultSlotLimit = defaultSlotLimit;
        this.insertionOnlySlots = ImmutableSet.copyOf(insertionOnlySlots);
        this.outputSlots = ImmutableSet.copyOf(outputSlots);
        ResizeBaseItemStackHandler.slotLimits = ImmutableMap.copyOf(slotLimits);
        this.slotValidators = ImmutableMap.copyOf(slotValidators);
        this.contentsChanged = contentsChanged;
        List<SlotJournal> journals = new ArrayList<>(size);
        for (int i = 0; i < size; i++) journals.add(new SlotJournal(i));
        this.slotJournals = Collections.unmodifiableList(journals);
    }

    public void setSlotLimits(int limit) {
        Map<Integer, Integer> aa = new HashMap<>();
        for (Integer slot : slotLimits.keySet()) {
            aa.put(slot, limit);
        }
        slotLimits = ImmutableMap.copyOf(aa);
    }

    public int getSlotLimit(int slot) {
        return slotLimits.getOrDefault(slot, this.defaultSlotLimit);
    }

    private class SlotJournal extends SnapshotJournal<ItemStack> {
        private final int index;

        SlotJournal(int index) {
            this.index = index;
        }

        @Override
        protected ItemStack createSnapshot() {
            return ResizeBaseItemStackHandler.this.stacks.get(this.index).copy();
        }

        @Override
        protected void revertToSnapshot(ItemStack snapshot) {
            ResizeBaseItemStackHandler.this.stacks.set(this.index, snapshot);
        }

        @Override
        protected void onRootCommit(ItemStack originalState) {
            ResizeBaseItemStackHandler.this.onContentsChanged(this.index, originalState);
        }
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        return !resource.isEmpty() && (!this.slotValidators.containsKey(slot) || this.slotValidators.get(slot).test(resource.toStack()));
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        if (resource.isEmpty()) {
            return this.getSlotLimit(index);
        }
        return Math.min(this.getSlotLimit(index), resource.getMaxStackSize());
    }

    @Override
    public int insert(int index, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext tx) {
        return this.outputSlots.contains(index) ? 0 : super.insert(index, resource, amount, tx);
    }

    @Override
    public int extract(int index, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext tx) {
        return this.insertionOnlySlots.contains(index) ? 0 : super.extract(index, resource, amount, tx);
    }

    @Override
    protected void onContentsChanged(int index, @Nonnull ItemStack previousContents) {
        if (this.contentsChanged != null) {
            this.contentsChanged.accept(index);
        }
    }

    @Override
    public void deserialize(@Nonnull ValueInput input) {
        super.deserialize(input);
        if (this.stacks.size() != this.size) {
            NonNullList<ItemStack> oldStacks = this.stacks;
            this.stacks = NonNullList.withSize(this.size, ItemStack.EMPTY);
            for (int slot = 0; slot < Math.min(oldStacks.size(), this.size); slot++) {
                this.stacks.set(slot, oldStacks.get(slot));
            }
        }
    }

    public Container toVanilla() {
        if (this.vanilla == null) this.vanilla = new VanillaWrapper(this, null);
        return this.vanilla;
    }

    public IAdvancedItemHandlerModifiable getUnrestricted() {
        if (this.unrestricted == null) this.unrestricted = new Unrestricted();
        return this.unrestricted;
    }

    public static Builder builder(int size) {
        return new Builder(size);
    }

    private class Unrestricted implements IAdvancedItemHandlerModifiable {

        @Override
        public void set(int index, ItemResource resource, int amount) {
            ResizeBaseItemStackHandler.this.set(index, resource, amount);
        }

        @Override
        public int size() {
            return ResizeBaseItemStackHandler.this.size();
        }

        @Nonnull
        @Override
        public ItemResource getResource(int index) {
            return ResizeBaseItemStackHandler.this.getResource(index);
        }

        @Override
        public long getAmountAsLong(int index) {
            return ResizeBaseItemStackHandler.this.getAmountAsLong(index);
        }

        @Override
        public long getCapacityAsLong(int index, @Nonnull ItemResource resource) {
            return ResizeBaseItemStackHandler.this.getCapacityAsLong(index, resource);
        }

        @Override
        public boolean isValid(int index, @Nonnull ItemResource resource) {
            return true;
        }

        @Override
        public int insert(int index, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext transaction) {
            if (resource.isEmpty() || amount <= 0) return 0;
            ItemStack current = ResizeBaseItemStackHandler.this.stacks.get(index);
            int capacity = ResizeBaseItemStackHandler.this.getCapacity(index, resource);
            if (!current.isEmpty()) {
                if (!ItemResource.of(current).equals(resource)) return 0;
                capacity -= current.getCount();
            }
            if (capacity <= 0) return 0;
            int toInsert = Math.min(amount, capacity);
            ResizeBaseItemStackHandler.this.slotJournals.get(index).updateSnapshots(transaction);
            if (current.isEmpty()) {
                ResizeBaseItemStackHandler.this.stacks.set(index, resource.toStack(toInsert));
            } else {
                current.grow(toInsert);
            }
            return toInsert;
        }

        @Override
        public int extract(int index, @Nonnull ItemResource resource, int amount, @Nonnull TransactionContext transaction) {
            if (amount <= 0) return 0;
            ItemStack current = ResizeBaseItemStackHandler.this.stacks.get(index);
            if (current.isEmpty() || !ItemResource.of(current).equals(resource)) return 0;
            int toExtract = Math.min(current.getCount(), amount);
            ResizeBaseItemStackHandler.this.slotJournals.get(index).updateSnapshots(transaction);
            ResizeBaseItemStackHandler.this.stacks.set(index, current.copyWithCount(current.getCount() - toExtract));
            return toExtract;
        }
    }

    public static class Builder {
        private final int size;
        private int defaultSlotLimit = 64;
        private final Set<Integer> insertionOnlySlots = new HashSet<>();
        private final Set<Integer> outputSlots = new HashSet<>();
        private final Map<Integer, Integer> slotLimits = new HashMap<>();
        private final Map<Integer, Predicate<ItemStack>> slotValidators = new HashMap<>();
        private Consumer<Integer> contentsChanged = null;

        private Builder(int size) {
            this.size = size;
        }

        public Builder contentsChanged(Runnable action) {
            return this.contentsChanged((slot) -> {
                action.run();
            });
        }

        public Builder contentsChanged(Consumer<Integer> action) {
            if (this.contentsChanged == null) {
                this.contentsChanged = action;
            } else {
                Consumer<Integer> old = this.contentsChanged;
                this.contentsChanged = (slot) -> {
                    old.accept(slot);
                    action.accept(slot);
                };
            }

            return this;
        }

        public Builder output(int... slots) {
            int length = slots.length;

            for (int slot : slots) {
                this.outputSlots.add(slot);
            }

            return this;
        }

        public Builder output(Set<Integer> slots) {
            this.outputSlots.addAll(slots);
            return this;
        }

        public Builder output(Range<Integer> slots) {
            IntStream range = IntStream.range(0, this.size);
            Objects.requireNonNull(slots);
            range = range.filter(slots::contains);
            Set<Integer> integers = this.outputSlots;
            Objects.requireNonNull(integers);
            range.forEach(integers::add);
            return this;
        }

        public Builder insertionOnly(int... slots) {
            int var3 = slots.length;

            for (int slot : slots) {
                this.insertionOnlySlots.add(slot);
            }

            return this;
        }

        public Builder insertionOnly(Set<Integer> slots) {
            this.insertionOnlySlots.addAll(slots);
            return this;
        }

        public Builder insertionOnly(Range<Integer> slots) {
            IntStream range = IntStream.range(0, this.size);
            Objects.requireNonNull(slots);
            range = range.filter(slots::contains);
            Set<Integer> integers = this.insertionOnlySlots;
            Objects.requireNonNull(integers);
            range.forEach(integers::add);
            return this;
        }

        public Builder defaultSlotLimit(int defaultSlotLimit) {
            this.defaultSlotLimit = defaultSlotLimit;
            return this;
        }

        public Builder slotLimit(int slotLimit, int... slots) {
            int var4 = slots.length;

            for (int slot : slots) {
                this.slotLimits.put(slot, slotLimit);
            }

            return this;
        }

        public Builder slotLimit(int slotLimit, Set<Integer> slots) {

            for (int slot : slots) {
                this.slotLimits.put(slot, slotLimit);
            }

            return this;
        }

        public Builder slotLimit(int slotLimit, Range<Integer> slots) {
            IntStream range = IntStream.range(0, this.size);
            Objects.requireNonNull(slots);
            range.filter(slots::contains).forEach((slot) -> {
                this.slotLimits.put(slot, slotLimit);
            });
            return this;
        }

        public Builder validator(Predicate<ItemStack> validator, int... slots) {
            for (int slot : slots) {
                this.slotValidators.put(slot, validator);
            }

            return this;
        }

        public Builder validator(Predicate<ItemStack> validator, Set<Integer> slots) {
            for (int slot : slots) {
                this.slotValidators.put(slot, validator);
            }

            return this;
        }

        public Builder validator(Predicate<ItemStack> validator, Range<Integer> slots) {
            IntStream range = IntStream.range(0, this.size);
            Objects.requireNonNull(slots);
            range.filter(slots::contains).forEach((slot) -> {
                this.slotValidators.put(slot, validator);
            });
            return this;
        }

        public ResizeBaseItemStackHandler build() {
            Stream<Integer> stream = this.outputSlots.stream();
            Set<Integer> integers = this.insertionOnlySlots;
            Objects.requireNonNull(integers);
            if (stream.anyMatch(integers::contains)) {
                throw new IllegalStateException("Can't build ResizeBaseItemStackHandler: A slot can not be an insertion only and an output slot at the same time.");
            } else {
                return new ResizeBaseItemStackHandler(this.size, this.defaultSlotLimit, this.insertionOnlySlots, this.outputSlots, this.slotLimits, this.slotValidators, this.contentsChanged);
            }
        }
    }
}
