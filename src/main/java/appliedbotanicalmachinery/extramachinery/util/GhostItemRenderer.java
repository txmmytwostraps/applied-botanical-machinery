package appliedbotanicalmachinery.extramachinery.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import velrondevs.botania.client.core.handler.ClientTickHandler;

import java.util.List;

public class GhostItemRenderer {

    public static void renderGhostItem(List<ItemStack> stacks, GuiGraphicsExtractor guiGraphics, int x, int y) {
        if (!stacks.isEmpty()) {
            ItemStack stack = stacks.get((ClientTickHandler.ticksInGame / 20 % stacks.size() + stacks.size()) % stacks.size());
            renderGhostItem(stack, guiGraphics, x, y);
        }
    }

    public static void renderGhostItem(ItemStack stack, GuiGraphicsExtractor guiGraphics, int x, int y) {
        if (!stack.isEmpty()) {
            guiGraphics.fakeItem(stack, x, y);
            guiGraphics.fill(x, y, x + 16, y + 16, 822083583);
        }
    }
}
