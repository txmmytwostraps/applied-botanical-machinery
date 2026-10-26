package appliedbotanicalmachinery.machinery;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import net.minecraft.world.item.Item;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.base.ItemBase;

@RegisterClass(registry = "ITEM")
public class ModItems {

    public static final Item manaEmerald = new ItemBase(AppliedBotanicalMachinery.getInstance(), new Item.Properties());
}
