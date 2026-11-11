package appliedbotanicalmachinery.machinery.helper;

import org.moddingx.libx.inventory.BaseItemStackHandler;
import org.moddingx.libx.inventory.IAdvancedItemHandlerModifiable;
import org.moddingx.libx.menu.slot.OutputSlot;

public class UnrestrictedOutputSlot extends OutputSlot {

    public UnrestrictedOutputSlot(IAdvancedItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
        super(getHandler(itemHandler), index, xPosition, yPosition);
    }

    private static IAdvancedItemHandlerModifiable getHandler(IAdvancedItemHandlerModifiable handler) {
        if (handler instanceof BaseItemStackHandler b) {
            return b.getUnrestricted();
        } else {
            return handler;
        }
    }
}
