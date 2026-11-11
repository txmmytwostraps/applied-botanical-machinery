package appliedbotanicalmachinery.item;

import java.util.function.Consumer;

import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import appliedbotanicalmachinery.ABMenus;
import appliedbotanicalmachinery.AppliedBotanics;
import appliedbotanicalmachinery.item.cell.IManaCellItem;

import appeng.api.upgrades.Upgrades;
import appeng.items.tools.powered.AbstractPortableCell;

public class PortableManaCellItem extends AbstractPortableCell implements IManaCellItem {

    private final int totalBytes;
    private final double idleDrain;

    public PortableManaCellItem(Properties props, int kilobytes, double idleDrain) {
        super(ABMenus.PORTABLE_MANA_CELL_TYPE, props, 0x67b9ee);
        this.totalBytes = kilobytes * 1000;
        this.idleDrain = idleDrain;
    }

    @Override
    public long getTotalBytes() {
        return this.totalBytes;
    }

    @Override
    public double getIdleDrain() {
        return this.idleDrain;
    }

    @Override
    public Identifier getRecipeId() {
        return AppliedBotanics.id(Objects.requireNonNull(getRegistryName()).getPath());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> lines,
            TooltipFlag advancedTooltips) {
        super.appendHoverText(stack, context, tooltipDisplay, lines, advancedTooltips);
        addCellInformationToTooltip(stack, lines);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return getCellTooltipImage(stack);
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return 80d + 80d * Upgrades.getEnergyCardMultiplier(getUpgrades(stack));
    }
}
