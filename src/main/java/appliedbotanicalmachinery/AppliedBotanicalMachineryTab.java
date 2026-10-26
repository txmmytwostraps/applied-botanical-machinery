package appliedbotanicalmachinery;

import java.util.stream.Stream;

import org.moddingx.libx.creativetab.CreativeTabX;
import org.moddingx.libx.mod.ModX;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.implementations.items.IAEItemPowerStorage;

public class AppliedBotanicalMachineryTab extends CreativeTabX {

    public AppliedBotanicalMachineryTab(ModX mod) {
        super(mod);
    }

    @Override
    protected void buildTab(CreativeModeTab.Builder builder) {
        super.buildTab(builder);
        builder.icon(() -> new ItemStack(ABItems.FLUIX_MANA_POOL.get()));
    }

    @Override
    protected void addItems(TabContext ctx) {
        // Powered items also show up fully charged
        this.addModItemStacks(ctx, item -> {
            var stack = new ItemStack(item);
            if (item instanceof IAEItemPowerStorage storage) {
                var charged = stack.copy();
                storage.injectAEPower(charged, storage.getAEMaxPower(charged), Actionable.MODULATE);
                return Stream.of(stack, charged);
            }
            return Stream.of(stack);
        });
    }
}
