package appliedbotanicalmachinery.extramachinery.init;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Recipe condition {@code appliedbotanicalmachinery:large_mana_cells}: true unless MEGA Cells replaces the 1M to 256M
 * mana cells (see {@link InitAppbot#replacedByMegaCells()}).
 */
public final class LargeManaCellsCondition implements ICondition {

    public static final LargeManaCellsCondition INSTANCE = new LargeManaCellsCondition();
    public static final MapCodec<LargeManaCellsCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private LargeManaCellsCondition() {
    }

    @Override
    public boolean test(IContext context) {
        return !InitAppbot.replacedByMegaCells();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "large_mana_cells";
    }
}
