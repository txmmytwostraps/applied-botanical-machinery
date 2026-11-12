package appliedbotanicalmachinery.item.cell;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.core.localization.Tooltips;

public class ManaCellHandler implements ICellHandler {
    public static final ManaCellHandler INSTANCE = new ManaCellHandler();

    @Override
    public boolean isCell(ItemStack is) {
        return !is.isEmpty() && is.getItem() instanceof IManaCellItem;
    }

    @Nullable
    @Override
    public ManaCellInventory getCellInventory(ItemStack is, @Nullable ISaveProvider host) {
        if (isCell(is)) {
            return new ManaCellInventory((IManaCellItem) is.getItem(), is, host);
        }
        return null;
    }

    public void addCellInformationToTooltip(ItemStack is, Consumer<Component> lines) {
        var handler = getCellInventory(is, null);
        if (handler == null) {
            return;
        }
        lines.accept(Tooltips.bytesUsed(handler.getUsedBytes(), handler.getTotalBytes()));
    }
}
