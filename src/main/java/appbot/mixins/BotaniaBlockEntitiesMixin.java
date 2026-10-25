package appbot.mixins;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

import java.util.function.BiFunction;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import appbot.ABBlocks;
import appbot.block.FluixPoolBlockEntity;
import velrondevs.botania.common.lib.LibBlockNames;
import velrondevs.botania.registry.BotaniaBlockEntities;

@Mixin(value = BotaniaBlockEntities.class, remap = false)
public abstract class BotaniaBlockEntitiesMixin {

    @ModifyVariable(method = "type(Lnet/minecraft/resources/ResourceLocation;Ljava/util/function/BiFunction;[Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/entity/BlockEntityType;", at = @At("HEAD"), index = 1, argsOnly = true)
    private static <T extends BlockEntity> BiFunction<BlockPos, BlockState, T> k(
            BiFunction<BlockPos, BlockState, T> factory, ResourceLocation id,
            BiFunction<BlockPos, BlockState, T> $factory, Block[] blocks) {
        if (prefix(LibBlockNames.POOL).equals(id)) {
            return (blockPos, blockState) -> {
                if (blockState.is(ABBlocks.FLUIX_MANA_POOL.get())) {
                    // noinspection unchecked
                    return (T) new FluixPoolBlockEntity(blockPos, blockState);
                } else {
                    return factory.apply(blockPos, blockState);
                }
            };
        }

        return factory;
    }

    @ModifyVariable(method = "type(Lnet/minecraft/resources/ResourceLocation;Ljava/util/function/BiFunction;[Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/entity/BlockEntityType;", at = @At("HEAD"), index = 2, argsOnly = true)
    private static <T extends BlockEntity> Block[] t(Block[] blocks, ResourceLocation id,
            BiFunction<BlockPos, BlockState, T> $factory, Block[] $blocks) {
        if (prefix(LibBlockNames.POOL).equals(id)) {
            return ArrayUtils.add(blocks, ABBlocks.FLUIX_MANA_POOL.get());
        }

        return blocks;
    }
}
