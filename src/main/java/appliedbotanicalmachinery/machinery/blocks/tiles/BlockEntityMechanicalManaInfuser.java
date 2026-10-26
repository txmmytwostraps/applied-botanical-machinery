package appliedbotanicalmachinery.machinery.blocks.tiles;

import appliedbotanicalmachinery.machinery.blocks.base.WorkingTile;
import appliedbotanicalmachinery.machinery.config.LibXClientConfig;
import appliedbotanicalmachinery.machinery.config.LibXServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.moddingx.libx.crafting.RecipeHelper;
import org.moddingx.libx.inventory.BaseItemStackHandler;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.masterfulbotany.common.crafting.InfuserRecipe;
import velrondevs.masterfulbotany.common.crafting.StackListInput;
import velrondevs.masterfulbotany.registry.ModRecipes;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityMechanicalManaInfuser extends WorkingTile<StackListInput, InfuserRecipe> {

    public static final int MAX_MANA_PER_TICK = 50000;

    private final BaseItemStackHandler inventory;

    public BlockEntityMechanicalManaInfuser(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, ModRecipes.infuser, pos, state, LibXServerConfig.MaxManaCapacity.mechanicalManaInfuser, 0, 3);
        this.inventory = BaseItemStackHandler.builder(4)
                .validator(stack -> this.level != null && RecipeHelper.isItemValidInput(this.level.getRecipeManager(), ModRecipes.infuser, stack), 0, 1, 2)
                .output(3)
                .contentsChanged(() -> {
                    this.setChanged();
                    this.setDispatchable();
                    this.needsRecipeUpdate();
                })
                .build();
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            this.runRecipeTick();
        } else if (this.level != null && LibXClientConfig.AdvancedRendering.all && LibXClientConfig.AdvancedRendering.mechanicalManaInfuser) {
            if (this.getProgress() > 0) {
                double time = this.getProgress() / (double) this.getMaxProgress();
                if (time < 0.8) {
                    time = time * 1.25;
                    double y = this.worldPosition.getY() + 6 / 16d + ((5 / 16d) * time);
                    double x1 = this.worldPosition.getX() + 0.2 + (0.3 * time);
                    double x2 = this.worldPosition.getX() + 0.8 - (0.3 * time);
                    double z1 = this.worldPosition.getZ() + 0.2 + (0.3 * time);
                    double z2 = this.worldPosition.getZ() + 0.8 - (0.3 * time);
                    WispParticleData data = WispParticleData.wisp(0.1f, 0, (float) time, (float) (1 - time), 1);
                    this.level.addParticle(data, x1, y, z1, 0, 0, 0);
                    this.level.addParticle(data, x1, y, z2, 0, 0, 0);
                    this.level.addParticle(data, x2, y, z1, 0, 0, 0);
                    this.level.addParticle(data, x2, y, z2, 0, 0, 0);
                }
            }
        }
    }

    @Override
    protected Predicate<Integer> getExtracts(Supplier<IItemHandlerModifiable> inventory) {
        return slot -> slot == 3;
    }

    @Nonnull
    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected int getMaxProgress(InfuserRecipe recipe) {
        return recipe.getManaUsage();
    }

    @Override
    public int getMaxManaPerTick() {
        return MAX_MANA_PER_TICK * LibXServerConfig.WorkingDurationMultiplier.mechanicalManaInfuser;
    }
}
