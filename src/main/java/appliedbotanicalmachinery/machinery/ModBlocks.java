package appliedbotanicalmachinery.machinery;

import appliedbotanicalmachinery.AppliedBotanicalMachinery;
import appliedbotanicalmachinery.machinery.blocks.*;
import appliedbotanicalmachinery.machinery.blocks.containers.*;
import appliedbotanicalmachinery.machinery.blocks.tiles.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.base.BlockBase;
import org.moddingx.libx.base.tile.MenuBlockBE;
import org.moddingx.libx.menu.type.AdvancedMenuType;
import velrondevs.botania.registry.BotaniaBlocks;

@RegisterClass(registry = "BLOCK", priority = 1)
public class ModBlocks {

    public static final BlockBase manaEmeraldBlock = new BlockBase(AppliedBotanicalMachinery.getInstance(), Block.Properties.ofFullCopy(BotaniaBlocks.manaDiamondBlock).mapColor(MapColor.EMERALD).strength(5.0F, 6.0F).sound(SoundType.METAL));
    public static final MenuBlockBE<BlockEntityAlfheimMarket, ContainerMenuAlfheimMarket> alfheimMarket = new BlockAlfheimMarket(AppliedBotanicalMachinery.getInstance(), BlockEntityAlfheimMarket.class, AdvancedMenuType.create(ContainerMenuAlfheimMarket::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityIndustrialAgglomerationFactory, ContainerMenuIndustrialAgglomerationFactory> industrialAgglomerationFactory = new BlockIndustrialAgglomerationFactory(AppliedBotanicalMachinery.getInstance(), BlockEntityIndustrialAgglomerationFactory.class, AdvancedMenuType.create(ContainerMenuIndustrialAgglomerationFactory::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityMechanicalManaInfuser, ContainerMenuMechanicalManaInfuser> mechanicalManaInfuser = new BlockMechanicalManaInfuser(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalManaInfuser.class, AdvancedMenuType.create(ContainerMenuMechanicalManaInfuser::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityManaBattery, ContainerMenuManaBattery> manaBattery = new BlockManaBattery(AppliedBotanicalMachinery.getInstance(), BlockEntityManaBattery.class, AdvancedMenuType.create(ContainerMenuManaBattery::new, BlockPos.STREAM_CODEC), BlockManaBattery.Variant.NORMAL);
    public static final MenuBlockBE<BlockEntityManaBattery, ContainerMenuManaBattery> manaBatteryCreative = new BlockManaBattery(AppliedBotanicalMachinery.getInstance(), BlockEntityManaBattery.class, AdvancedMenuType.create(ContainerMenuManaBattery::new, BlockPos.STREAM_CODEC), BlockManaBattery.Variant.CREATIVE);
    public static final MenuBlockBE<BlockEntityMechanicalApothecary, ContainerMenuMechanicalApothecary> mechanicalApothecary = new BlockMechanicalApothecary(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalApothecary.class, AdvancedMenuType.create(ContainerMenuMechanicalApothecary::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityMechanicalBrewery, ContainerMenuMechanicalBrewery> mechanicalBrewery = new BlockMechanicalBrewery(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalBrewery.class, AdvancedMenuType.create(ContainerMenuMechanicalBrewery::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityMechanicalDaisy, ContainerMenuMechanicalDaisy> mechanicalDaisy = new BlockMechanicalDaisy(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalDaisy.class, AdvancedMenuType.create(ContainerMenuMechanicalDaisy::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityMechanicalManaPool, ContainerMenuMechanicalManaPool> mechanicalManaPool = new BlockMechanicalManaPool(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalManaPool.class, AdvancedMenuType.create(ContainerMenuMechanicalManaPool::new, BlockPos.STREAM_CODEC));
    public static final MenuBlockBE<BlockEntityMechanicalRunicAltar, ContainerMenuMechanicalRunicAltar> mechanicalRunicAltar = new BlockMechanicalRunicAltar(AppliedBotanicalMachinery.getInstance(), BlockEntityMechanicalRunicAltar.class, AdvancedMenuType.create(ContainerMenuMechanicalRunicAltar::new, BlockPos.STREAM_CODEC));
}
