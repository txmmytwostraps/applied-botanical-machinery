package appliedbotanicalmachinery.machinery.blocks.containers;

import org.moddingx.libx.menu.slot.BaseSlot;

import net.neoforged.neoforge.transfer.transaction.Transaction;

import net.neoforged.neoforge.transfer.access.ItemAccess;

import net.neoforged.neoforge.transfer.fluid.FluidResource;

import net.neoforged.neoforge.transfer.ResourceHandler;

import appliedbotanicalmachinery.util.Inventories;

import appliedbotanicalmachinery.machinery.blocks.tiles.BlockEntityMechanicalDaisy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.moddingx.libx.menu.BlockEntityMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerMenuMechanicalDaisy extends BlockEntityMenu<BlockEntityMechanicalDaisy> {

    private final BlockEntityMechanicalDaisy.InventoryHandler inventory;

    public ContainerMenuMechanicalDaisy(MenuType<? extends BlockEntityMenu<?>> type, int windowId, Level level, BlockPos pos, Player player, Inventory playerContainer) {
        super(type, windowId, level, pos, player, playerContainer, 8, 8);

        this.inventory = this.blockEntity.getInventory();
        this.addSlot(new ItemAndFluidSlot(this.inventory, 0, 79, 16));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 1, 100, 16));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 2, 121, 16));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 3, 79, 37));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 4, 121, 37));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 5, 79, 58));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 6, 100, 58));
        this.addSlot(new ItemAndFluidSlot(this.inventory, 7, 121, 58));

        this.layoutPlayerInventorySlots(8, 84);
    }

    @Override
    public void clicked(int slot, int dragType, @Nonnull ContainerInput clickType, @Nonnull Player player) {
        if (clickType == ContainerInput.PICKUP && slot >= 0 && slot < 8 && !this.getCarried().isEmpty() && this.getCarried().getCount() == 1) {
            ItemStack inMouse = this.getCarried();

            @Nullable
            ResourceHandler<FluidResource> fluidCap = inMouse.getItem() instanceof BlockItem ? null : ItemAccess.forPlayerCursor(player, this).getCapability(Capabilities.Fluid.ITEM);

            if (fluidCap == null || fluidCap.size() != 1) {
                super.clicked(slot, dragType, clickType, player);
                return;
            }

            FluidStack slotFluid = this.inventory.getFluidInTank(slot);
            try (Transaction tx = Transaction.openRoot()) {
                if (fluidCap.getResource(0).isEmpty()) {
                    // Pick up the fluid
                    if (!slotFluid.isEmpty()) {
                        int transferred = fluidCap.insert(0, FluidResource.of(slotFluid), slotFluid.getAmount(), tx);
                        if (transferred > 0) {
                            tx.commit();
                            this.inventory.setFluid(slot, slotFluid.copyWithAmount(slotFluid.getAmount() - transferred));
                        }
                    }
                } else if (slotFluid.isEmpty() && Inventories.get(this.inventory, slot).isEmpty()) {
                    // Fill the fluid
                    FluidResource resource = fluidCap.getResource(0);
                    if (this.inventory.getFluidHandler().isValid(slot, resource)) {
                        int drained = fluidCap.extract(0, resource, this.inventory.getTankCapacity(slot), tx);
                        if (drained > 0) {
                            tx.commit();
                            this.inventory.setFluid(slot, resource.toStack(drained));
                        }
                    }
                }
            }

            this.broadcastChanges();
        }

        super.clicked(slot, dragType, clickType, player);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            final int inventorySize = 5;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index < 8) {
                if (!this.moveItemStackTo(stack, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, itemstack);
            } else {
                ResourceHandler<FluidResource> fluidCap = ItemAccess.forPlayerSlot(player, slot.getContainerSlot()).getCapability(Capabilities.Fluid.ITEM);
                if (fluidCap != null && fluidCap.size() == 1) {
                    this.tryToDepositFluid(fluidCap);
                    return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(stack, 0, 8, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    // We need to override this to prevent shift-clicking items in slots filled with fluids.
    @Override
    protected boolean moveItemStackTo(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack) && slot.mayPlace(stack) && (i >= 8 || this.inventory.getFluidInTank(i).isEmpty())) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack) && (i >= 8 || this.inventory.getFluidInTank(i).isEmpty())) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(stack.split(stack.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    private void tryToDepositFluid(ResourceHandler<FluidResource> fluidCap) {
        for (int i = 0; i < 8; i++) {
            if (Inventories.get(this.inventory, i).isEmpty() && this.inventory.getFluidInTank(i).isEmpty()) {
                FluidResource resource = fluidCap.getResource(0);
                if (!resource.isEmpty() && this.inventory.getFluidHandler().isValid(i, resource)) {
                    try (Transaction tx = Transaction.openRoot()) {
                        int drained = fluidCap.extract(0, resource, this.inventory.getTankCapacity(i), tx);
                        if (drained > 0) {
                            tx.commit();
                            this.inventory.setFluid(i, resource.toStack(drained));
                        }
                    }
                }
            }
        }
    }

    public static class ItemAndFluidSlot extends BaseSlot {

        public final BlockEntityMechanicalDaisy.InventoryHandler inventory;

        public ItemAndFluidSlot(BlockEntityMechanicalDaisy.InventoryHandler inventory, int index, int xPosition, int yPosition) {
            super(inventory, inventory, index, xPosition, yPosition);
            this.inventory = inventory;
        }
    }
}
