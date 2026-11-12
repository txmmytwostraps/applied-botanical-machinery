package appliedbotanicalmachinery.data;

import java.util.Locale;
import java.util.stream.Stream;

import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import appliedbotanicalmachinery.ABBlocks;
import appliedbotanicalmachinery.ABItems;
import appliedbotanicalmachinery.AppliedBotanics;

import appeng.client.item.PortableCellColorTintSource;
import appeng.client.item.StorageCellStateTintSource;
import appeng.core.AppEng;

public class ModelProvider extends net.minecraft.client.data.models.ModelProvider {

    private static final Identifier P2P_TUNNEL_BASE_ITEM = AppEng.makeId("item/p2p_tunnel_base");
    private static final Identifier P2P_TUNNEL_BASE_PART = AppEng.makeId("part/p2p_tunnel_base");
    private static final Identifier STORAGE_CELL_LED = AppEng.makeId("item/storage_cell_led");
    private static final Identifier PORTABLE_CELL_LED = AppEng.makeId("item/portable_cell_led");
    private static final Identifier MANASTEEL_BLOCK = Identifier.fromNamespaceAndPath("botania",
            "block/manasteel_block");

    private static final TextureSlot TYPE = TextureSlot.create("type");
    private static final TextureSlot LAYER3 = TextureSlot.create("layer3");
    private static final ModelTemplate FOUR_LAYERED_ITEM = ModelTemplates.createItem("generated", TextureSlot.LAYER0,
            TextureSlot.LAYER1, TextureSlot.LAYER2, LAYER3);

    public ModelProvider(PackOutput output) {
        super(output, AppliedBotanics.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // The Fluix Mana Pool uses its hand-written block model
        var poolModel = AppliedBotanics.id("block/fluix_mana_pool");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ABBlocks.FLUIX_MANA_POOL.get(),
                BlockModelGenerators.plainVariant(poolModel)));
        blockModels.registerSimpleItemModel(ABItems.FLUIX_MANA_POOL.get(), poolModel);

        itemModels.generateFlatItem(ABItems.MANA_CELL_HOUSING.get(), ModelTemplates.FLAT_ITEM);

        for (var tier : ABItems.Tier.values()) {
            var cell = ABItems.get(tier).get();
            var cellModel = itemModels.generateLayeredItem(cell,
                    new Material(AppliedBotanics.id("item/" + ABItems.get(tier).getId().getPath())),
                    new Material(STORAGE_CELL_LED));
            itemModels.itemModelOutput.accept(cell, ItemModelUtils.tintedModel(cellModel,
                    new Constant(-1), new StorageCellStateTintSource()));

            var portableCell = ABItems.getPortableCell(tier).get();
            var portableModel = FOUR_LAYERED_ITEM.create(portableCell,
                    TextureMapping.layered(
                            new Material(AppliedBotanics.id("item/portable_mana_cell_housing")),
                            new Material(PORTABLE_CELL_LED),
                            new Material(AppliedBotanics.id("item/portable_cell_screen")))
                            .put(LAYER3, new Material(AppliedBotanics.id(
                                    "item/portable_mana_cell" + tier.toString().toLowerCase(Locale.ROOT)))),
                    itemModels.modelOutput);
            itemModels.itemModelOutput.accept(portableCell, ItemModelUtils.tintedModel(portableModel,
                    new Constant(-1), new StorageCellStateTintSource(), new PortableCellColorTintSource()));
        }

        // The tunnel's item model and its part model (named by assets/<ns>/ae2/parts/mana_p2p_tunnel.json)
        var tunnel = ABItems.MANA_P2P_TUNNEL.get();
        var tunnelItem = new ModelTemplate(java.util.Optional.of(P2P_TUNNEL_BASE_ITEM), java.util.Optional.empty(),
                TYPE)
                .create(tunnel, new TextureMapping().put(TYPE, new Material(MANASTEEL_BLOCK)), itemModels.modelOutput);
        blockModels.registerSimpleItemModel(tunnel, tunnelItem);
        new ModelTemplate(java.util.Optional.of(P2P_TUNNEL_BASE_PART), java.util.Optional.empty(), TYPE)
                .create(AppliedBotanics.id("part/mana_p2p_tunnel"),
                        new TextureMapping().put(TYPE, new Material(MANASTEEL_BLOCK)), itemModels.modelOutput);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of(ABBlocks.FLUIX_MANA_POOL);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ABItems.getEntries().stream();
    }
}
