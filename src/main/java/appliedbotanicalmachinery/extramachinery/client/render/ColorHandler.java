package appliedbotanicalmachinery.extramachinery.client.render;

import appliedbotanicalmachinery.AppliedBotanics;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import velrondevs.botania.client.core.handler.ClientTickHandler;

import javax.annotation.Nullable;

public class ColorHandler {

    /**
     * The shifting rainbow of the petal catalysts. Item models name it as tint
     * {@code appliedbotanicalmachinery:catalyst_rainbow}.
     */
    public record CatalystRainbow() implements ItemTintSource {

        public static final Identifier ID = AppliedBotanics.id("catalyst_rainbow");
        public static final MapCodec<CatalystRainbow> MAP_CODEC = MapCodec.unit(CatalystRainbow::new);

        @Override
        public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
            return ARGB.opaque(Mth.hsvToRgb(ClientTickHandler.ticksInGame * 2 % 360 / 360F, 0.25F, 1F));
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return MAP_CODEC;
        }
    }
}
