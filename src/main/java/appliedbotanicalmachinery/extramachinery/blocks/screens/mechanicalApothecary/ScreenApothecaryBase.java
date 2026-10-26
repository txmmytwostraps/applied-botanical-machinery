package appliedbotanicalmachinery.extramachinery.blocks.screens.mechanicalApothecary;

import appliedbotanicalmachinery.extramachinery.blocks.base.ExtraScreenBase;
import appliedbotanicalmachinery.extramachinery.blocks.containers.mechanicalApothecary.ContainerApothecaryBase;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenAddInventory;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenDrawLabelText;
import appliedbotanicalmachinery.extramachinery.blocks.screens.uitlScreen.ScreenInventory;
import appliedbotanicalmachinery.extramachinery.blocks.tiles.mechanicalApothecary.BlockEntityApothecaryBase;
import appliedbotanicalmachinery.extramachinery.core.LibResources;
import appliedbotanicalmachinery.extramachinery.gui.AllBars;
import appliedbotanicalmachinery.extramachinery.gui.Bars;
import appliedbotanicalmachinery.extramachinery.gui.SlotInfo;
import appliedbotanicalmachinery.extramachinery.util.GhostItemRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import vazkii.botania.client.core.helper.RenderHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenApothecaryBase extends ExtraScreenBase<ContainerApothecaryBase> {
    private final BlockEntityApothecaryBase blockEntity;
    ScreenAddInventory screenAddInventory = new ScreenAddInventory(ScreenInventory.BASE);
    List<ItemStack> items = new ArrayList<>();
    Bars bars;
    SlotInfo slotInfo;

    public ScreenApothecaryBase(ContainerApothecaryBase screenMenu, Inventory inventory, Component title) {
        super(screenMenu, inventory, title);

        bars = new Bars(this);
        slotInfo = new SlotInfo(this);

        this.imageWidth = ContainerApothecaryBase.WIDTH_GUI;
        this.imageHeight = ContainerApothecaryBase.HEIGHT_GUI;

        bars.setBar(AllBars.WATER);
        bars.setDrawCoord(33, 100);

        blockEntity = this.menu.getBlockEntity();

        Map<Integer, int[]> slots = new HashMap<>();
        slots.put(0, new int[] {90, 79});

        slotInfo.setCoord(slots);
        slotInfo.setTranslatableText(new String[] {"appliedbotanicalmachinery.tooltip.screen.seed_slot"});

        BuiltInRegistries.ITEM.getTagOrEmpty(Tags.Items.SEEDS).forEach(holder -> items.add(new ItemStack(holder.value())));
    }

    @Override
    protected ScreenAddInventory getAddInventory() {
        return screenAddInventory;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultGuiBackgroundLayer(guiGraphics, LibResources.BASE_MECHANICAL_APOTHECARY_GUI, screenAddInventory,
                new int[] {blockEntity.getFluidInventory().getFluidAmount()}, new int[] {blockEntity.getFluidInventory().getCapacity()}, bars, slotInfo);

        ScreenDrawLabelText.drawLabelText(guiGraphics, this.font, "block.appliedbotanicalmachinery.base_apothecary",
                new int[] {this.leftPos, this.topPos}, new int[] {this.imageWidth, this.imageHeight}, 6);

        if (this.blockEntity.getInventory().getStackInSlot(0).isEmpty()) {
            GhostItemRenderer.renderGhostItem(items, guiGraphics, this.leftPos + 90, this.topPos + 79);
        }

        if (this.blockEntity.getProgress() > 0) {
            float pctProgress = Math.min((float)this.blockEntity.getProgress() / (float)this.blockEntity.getMaxProgress(), 1.0F);
            RenderHelper.drawTexturedModalRect(guiGraphics, LibResources.BASE_MECHANICAL_APOTHECARY_GUI,this.leftPos + 92, this.topPos + 35, this.imageWidth, 0, Math.round(11.0F * pctProgress), 37);
        }

        slotInfo.renderHoveredToolTip(guiGraphics, mouseX, mouseY, this.blockEntity.getInventory());
        bars.renderHoveredToolTip(guiGraphics, mouseX, mouseY, blockEntity.getFluidInventory().getFluidAmount(), blockEntity.getFluidInventory().getCapacity(), 0);
    }
}
