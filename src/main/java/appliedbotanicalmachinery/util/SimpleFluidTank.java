package appliedbotanicalmachinery.util;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * A one-tank fluid handler with a validator, in the shape of the old FluidTank.
 */
public class SimpleFluidTank extends FluidStacksResourceHandler {

    private final Predicate<FluidStack> validator;
    private final boolean canDrain;
    private final Runnable changed;

    /**
     * @param canDrain whether others (pipes, buckets) may take fluid out. The owner can always use {@link #drain}.
     */
    public SimpleFluidTank(int capacity, Predicate<FluidStack> validator, boolean canDrain, Runnable changed) {
        super(1, capacity);
        this.validator = validator;
        this.canDrain = canDrain;
        this.changed = changed;
    }

    public FluidStack getFluid() {
        FluidResource resource = this.getResource(0);
        return resource.isEmpty() ? FluidStack.EMPTY : resource.toStack(this.getAmountAsInt(0));
    }

    public void setFluid(FluidStack stack) {
        if (stack.isEmpty()) {
            this.set(0, FluidResource.EMPTY, 0);
        } else {
            this.set(0, FluidResource.of(stack), stack.getAmount());
        }
    }

    public int getFluidAmount() {
        return this.getAmountAsInt(0);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return this.getResource(0).isEmpty();
    }

    /**
     * Fills the tank and returns the amount that went in.
     */
    public int fill(FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return 0;
        }
        try (Transaction tx = Transaction.openRoot()) {
            int inserted = this.insert(0, FluidResource.of(stack), stack.getAmount(), tx);
            if (!simulate) {
                tx.commit();
            }
            return inserted;
        }
    }

    /**
     * Takes up to {@code amount} out of the tank, also when others may not drain it.
     */
    public FluidStack drain(int amount, boolean simulate) {
        FluidStack fluid = this.getFluid();
        int drained = Math.min(amount, fluid.getAmount());
        if (drained <= 0) {
            return FluidStack.EMPTY;
        }
        if (!simulate) {
            this.setFluid(fluid.copyWithAmount(fluid.getAmount() - drained));
        }
        return fluid.copyWithAmount(drained);
    }

    @Override
    public boolean isValid(int index, @Nonnull FluidResource resource) {
        return !resource.isEmpty() && this.validator.test(resource.toStack(1));
    }

    @Override
    public int extract(int index, @Nonnull FluidResource resource, int amount,
            @Nonnull TransactionContext transaction) {
        return this.canDrain ? super.extract(index, resource, amount, transaction) : 0;
    }

    @Override
    protected void onContentsChanged(int index, @Nonnull FluidStack previousContents) {
        this.changed.run();
    }
}
