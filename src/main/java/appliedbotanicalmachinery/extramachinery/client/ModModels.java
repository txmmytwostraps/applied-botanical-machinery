package appliedbotanicalmachinery.extramachinery.client;

import appliedbotanicalmachinery.AppliedBotanics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import velrondevs.botania.client.core.helper.RenderHelper;
import net.minecraft.resources.Identifier;

public class ModModels {

    public static final ModModels INSTANCE = new ModModels();

    public final Icon baseSparkWorldIcon = mainAtlas("item/base_spark");
    public final Icon malachiteSparkWorldIcon = mainAtlas("item/malachite_spark");
    public final Icon saffronSparkWorldIcon = mainAtlas("item/saffron_spark");
    public final Icon shadowSparkWorldIcon = mainAtlas("item/shadow_spark");
    public final Icon crimsonSparkWorldIcon = mainAtlas("item/crimson_spark");
    public final Icon aureateSparkWorldIcon = mainAtlas("item/aureate_spark");
    public final Icon mazarineSparkWorldIcon = mainAtlas("item/mazarine_spark");

    private static Icon mainAtlas(String name) {
        return new Icon(res(name));
    }

    /**
     * An item texture, looked up on the items atlas when drawn (items have their own atlas since 1.21.5).
     */
    public record Icon(Identifier id) {
        public TextureAtlasSprite sprite() {
            return RenderHelper.itemSprite(this.id);
        }
    }

    private static Identifier res(String name){
        return Identifier.fromNamespaceAndPath(AppliedBotanics.MOD_ID, name);
    }

}
