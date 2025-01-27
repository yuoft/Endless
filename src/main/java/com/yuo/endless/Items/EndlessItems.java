package com.yuo.endless.Items;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Event.ModSpawnEgg;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Items.Armor.CrystalMatrixArmor;
import com.yuo.endless.Items.Armor.InfinityArmor;
import com.yuo.endless.Items.Armor.MyArmorMaterial;
import com.yuo.endless.Items.Armor.NeutroniumArmor;
import com.yuo.endless.Items.Tool.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class EndlessItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Endless.MOD_ID);
	public static final Item.Properties BLOCK_TAB = new Item.Properties().group(EndlessTab.endless);

	//食物
	public static RegistryObject<Item> cosmicMeatBalls = ITEMS.register("cosmic_meatballs", () -> new OrdinaryFood(MyFoods.MEAT_BALLS));
	public static RegistryObject<Item> ultimateStew = ITEMS.register("ultimate_stew", () -> new OrdinaryFood(MyFoods.STEW));

	//物品
	public static RegistryObject<Item> diamondLattice = ITEMS.register("diamond_lattice", OrdinaryItem::new);
	public static RegistryObject<Item> crystalMatrixIngot = ITEMS.register("crystal_matrix_ingot", OrdinaryItem::new);
	public static RegistryObject<Item> neutroniumPile = ITEMS.register("neutronium_pile", OrdinaryItem::new);
	public static RegistryObject<Item> neutroniumNugget = ITEMS.register("neutronium_nugget", OrdinaryItem::new);
	public static RegistryObject<Item> neutroniumIngot = ITEMS.register("neutronium_ingot", OrdinaryItem::new);
	public static RegistryObject<Item> endestPearl = ITEMS.register("endest_pearl", EndestPearl::new);
	public static RegistryObject<Item> infinityCatalyst = ITEMS.register("infinity_catalyst", InfinityItem::new);
	public static RegistryObject<Item> infinityIngot = ITEMS.register("infinity_ingot", InfinityItem::new);
	public static RegistryObject<Item> recordFragment = ITEMS.register("record_fragment", OrdinaryItem::new);
	public static RegistryObject<Item> matterCluster = ITEMS.register("matter_cluster", MatterCluster::new);
	public static RegistryObject<Item> neutroniumGear = ITEMS.register("neutronium_gear", OrdinaryItem::new);
	public static RegistryObject<Item> starFuel = ITEMS.register("star_fuel", OrdinaryItem::new);
	public static RegistryObject<Item> infinityNugget = ITEMS.register("infinity_nugget", InfinityItem::new);
	public static RegistryObject<Item> infinityArrow = ITEMS.register("infinity_arrow", InfinityArrow::new);

	public static RegistryObject<ModSpawnEgg> infinityMobSpawnEgg = ITEMS.register("infinity_mob_spawn_egg", () ->
			new ModSpawnEgg(EntityRegistry.INFINITY_MOB, 0xff2233, 0x3ff211));

	//奇点
	public static RegistryObject<Item> singularity = ITEMS.register("singularity", Singularity::new);
	public static RegistryObject<Item> eternalSingularity = ITEMS.register("eternal_singularity", InfinityItem::new);

	//工具
	public static RegistryObject<Item> infinityPickaxe = ITEMS.register("infinity_pickaxe", InfinityPickaxe::new);
	public static RegistryObject<Item> infinityAxe = ITEMS.register("infinity_axe", InfinityAxe::new);
	public static RegistryObject<Item> infinityShovel = ITEMS.register("infinity_shovel", InfinityShovel::new);
	public static RegistryObject<Item> infinityHoe = ITEMS.register("infinity_hoe", InfinityHoe::new);
	public static RegistryObject<Item> infinitySword = ITEMS.register("infinity_sword", InfinitySword::new);
	public static RegistryObject<Item> skullfireSword = ITEMS.register("skullfire_sword", SkullfireSword::new);
	public static RegistryObject<Item> infinityBow = ITEMS.register("infinity_bow", InfinityBow::new);
	public static RegistryObject<Item> infinityCrossBow = ITEMS.register("infinity_crossbow", InfinityCrossBow::new);
	public static RegistryObject<Item> infinityBucket = ITEMS.register("infinity_bucket", InfinityBucket::new);
	public static RegistryObject<Item> infinityTotem = ITEMS.register("infinity_totem", InfinityTotem::new);

	public static RegistryObject<Item> infinityFluidBucket = ITEMS.register("infinity_fluid_bucket",
			() -> new InfinityFluidBucket(EndlessFluids.infinityFluid));

	public static RegistryObject<Item> crystalMatrixSword = ITEMS.register("crystal_matrix_sword", () -> new OrdinarySword(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalMatrixPickaxe = ITEMS.register("crystal_matrix_pickaxe", () -> new OrdinaryPickaxe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalMatrixAxe = ITEMS.register("crystal_matrix_axe", () -> new OrdinaryAxe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalMatrixShovel = ITEMS.register("crystal_matrix_shovel", () -> new OrdinaryShovel(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalMatrixHoe = ITEMS.register("crystal_matrix_hoe", () -> new OrdinaryHoe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> neutroniumSword = ITEMS.register("neutronium_sword", () -> new OrdinarySword(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutroniumPickaxe = ITEMS.register("neutronium_pickaxe", () -> new OrdinaryPickaxe(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutroniumAxe = ITEMS.register("neutronium_axe", () -> new OrdinaryAxe(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutroniumShovel = ITEMS.register("neutronium_shovel", () -> new OrdinaryShovel(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutroniumHoe = ITEMS.register("neutronium_hoe", () -> new OrdinaryHoe(MyItemTier.NEUTRON));


	//盔甲
	public static RegistryObject<ArmorItem> infinityHead = ITEMS.register("infinity_helmet", () -> new InfinityArmor(EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> infinityChest = ITEMS.register("infinity_chestplate", () -> new InfinityArmor(EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> infinityLegs = ITEMS.register("infinity_leggings", () -> new InfinityArmor(EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> infinityFeet = ITEMS.register("infinity_boots", () -> new InfinityArmor(EquipmentSlotType.FEET));
	public static RegistryObject<ArmorItem> crystalMatrixHead = ITEMS.register("crystal_matrix_helmet", () -> new CrystalMatrixArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> crystalMatrixChest = ITEMS.register("crystal_matrix_chestplate", () -> new CrystalMatrixArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> crystalMatrixLegs = ITEMS.register("crystal_matrix_leggings", () -> new CrystalMatrixArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> crystalMatrixFeet = ITEMS.register("crystal_matrix_boots", () -> new CrystalMatrixArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.FEET));
	public static RegistryObject<ArmorItem> neutroniumHead = ITEMS.register("neutronium_helmet", () -> new NeutroniumArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> neutroniumChest = ITEMS.register("neutronium_chestplate", () -> new NeutroniumArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> neutroniumLegs = ITEMS.register("neutronium_leggings", () -> new NeutroniumArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> neutroniumFeet = ITEMS.register("neutronium_boots", () -> new NeutroniumArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.FEET));


	//注册方块物品
	public static RegistryObject<BlockItem> infinityBlock = ITEMS.register("infinity_block",
			() -> new BlockItem(EndlessBlocks.infinityBlock.get(), BLOCK_TAB.isImmuneToFire()));
	public static RegistryObject<BlockItem> crystalMatrixBlock = ITEMS.register("crystal_matrix_block",
			() -> new BlockItem(EndlessBlocks.crystalMatrixBlock.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> neutroniumBlock = ITEMS.register("neutronium_block",
			() -> new BlockItem(EndlessBlocks.neutroniumBlock.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> compressedCraftingTable = ITEMS.register("compressed_crafting_table",
			() -> new BlockItem(EndlessBlocks.compressedCraftingTable.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> doubleCompressedCraftingTable = ITEMS.register("double_compressed_crafting_table",
			() -> new BlockItem(EndlessBlocks.doubleCompressedCraftingTable.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> extremeCraftingTable = ITEMS.register("extreme_crafting_table",
			() -> new BlockItem(EndlessBlocks.extremeCraftingTable.get(), BLOCK_TAB.isImmuneToFire()));
	public static RegistryObject<BlockItem> enderCraftingTable = ITEMS.register("ender_crafting_table",
			() -> new BlockItem(EndlessBlocks.enderCraftingTable.get(), BLOCK_TAB.isImmuneToFire()));
	public static RegistryObject<BlockItem> netherCraftingTable = ITEMS.register("nether_crafting_table",
			() -> new BlockItem(EndlessBlocks.netherCraftingTable.get(), BLOCK_TAB.isImmuneToFire()));
	public static RegistryObject<BlockItem> neutroniumCollector = ITEMS.register("neutronium_collector",
			() -> new BlockItem(EndlessBlocks.neutroniumCollector.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> denseNeutroniumCollector = ITEMS.register("dense_neutronium_collector",
			() -> new BlockItem(EndlessBlocks.denseNeutroniumCollector.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> denserNeutroniumCollector = ITEMS.register("denser_neutronium_collector",
			() -> new BlockItem(EndlessBlocks.denserNeutroniumCollector.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> densestNeutroniumCollector = ITEMS.register("densest_neutronium_collector",
			() -> new BlockItem(EndlessBlocks.densestNeutroniumCollector.get(), BLOCK_TAB.isImmuneToFire()));
	public static RegistryObject<BlockItem> neutronCompressor = ITEMS.register("neutron_compressor",
			() -> new BlockItem(EndlessBlocks.neutronCompressor.get(), BLOCK_TAB));
	public static RegistryObject<BlockItem> compressedChest = ITEMS.register("compressed_chest",
			() -> new BlockItem(EndlessBlocks.compressedChest.get(), BLOCK_TAB.maxStackSize(1)));
	public static RegistryObject<BlockItem> infinityBox = ITEMS.register("infinity_chest",
			() -> new BlockItem(EndlessBlocks.infinityBox.get(), BLOCK_TAB.maxStackSize(1).isImmuneToFire()));

	//强力装备
	public static void registerSpaceArmsItem(){
		Singularity.addSingularity(new Singularity.SingularityData("ruby", 0xe02e35,0xe25e63));//红宝石
		Singularity.addSingularity(new Singularity.SingularityData("dragon", 0x550a56,0xe04fe2));//龙晶
		Singularity.addSingularity(new Singularity.SingularityData("space", 0x000000,0xffffff));//空间
		Singularity.addSingularity(new Singularity.SingularityData("xray", 0x3affff,0xe4ffff));//X光
		Singularity.addSingularity(new Singularity.SingularityData("ultra", 0x7f6a00,0x4cff00));//极限
	}

	//冰与火
	public static void registerIafItem(){
		Singularity.addSingularity(new Singularity.SingularityData("silver", 0xf3faff,0xe5ecf7));//银
		Singularity.addSingularity(new Singularity.SingularityData("copper", 0x95654c,0xbd896d));//铜
	}

	//机械动力
	public static void registerCreate(){
		Singularity.addSingularity(new Singularity.SingularityData("zinc", 0xaab59d,0xb5d1ba));//锌
	}

	//热力
	public static void registerThermal(){
		Singularity.addSingularity(new Singularity.SingularityData("nickel", 0x916e4d,0xf9f5ab));//铂
		Singularity.addSingularity(new Singularity.SingularityData("lead", 0x232457,0x393c61));//铝
		Singularity.addSingularity(new Singularity.SingularityData("tin", 0x517c88,0x88a2a7));//镍
	}

	//龙之研究
	public static void registerDE(){
		Singularity.addSingularity(new Singularity.SingularityData("draconium", 0x6b369b,0x6c389a));//龙锭
		Singularity.addSingularity(new Singularity.SingularityData("awakened_draconium", 0xf45100,0xeecb3d));//觉醒龙锭
	}

	//植物魔法
	public static void registerBOT(){
		Singularity.addSingularity(new Singularity.SingularityData("manasteel", 0x49a5ee,0x50b4ff));//魔钢
		Singularity.addSingularity(new Singularity.SingularityData("terrasteel", 0x51dc24,0x57ef26));//泰拉钢
		Singularity.addSingularity(new Singularity.SingularityData("elementium", 0xe464ff,0xe784ff));//源质钢
	}

	//等价交换
	public static void registerPE(){
		Singularity.addSingularity(new Singularity.SingularityData("dark_matter", 0x0c0c0c,0x171717));//暗物质
		Singularity.addSingularity(new Singularity.SingularityData("red_matter", 0x340303,0x4d0404));//红物质
	}
	//匠魂3
	public static void registerTC3(){
		Singularity.addSingularity(new Singularity.SingularityData("cobalt", 0x0753b8,0x59a6ef));//钴
		Singularity.addSingularity(new Singularity.SingularityData("manyullyn", 0xa97de0,0xcfacf9));//玛玉灵
	}
}
