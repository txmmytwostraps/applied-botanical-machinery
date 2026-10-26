package appliedbotanicalmachinery.extramachinery;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.creativetab.CreativeTabX;
import org.moddingx.libx.mod.ModX;

public class ExtraMachineryTab extends CreativeTabX {

    public ExtraMachineryTab(ModX mod) {
        super(mod);
    }

    @Override
    protected void buildTab(CreativeModeTab.Builder builder) {
        super.buildTab(builder);
        builder.title(Component.translatable("itemGroup.appliedbotanicalmachinery.extra"));
        builder.icon(() -> new ItemStack(ModBlocks.shadowDragonstoneBlock));
    }

    @Override
    protected void addItems(TabContext ctx) {
        this.addModItems(ctx);
    }
}