package com.yuo.endless.Items;

import com.yuo.endless.Armor.InfinityArmor;
import com.yuo.endless.Armor.MyArmorMaterial;
import com.yuo.endless.Armor.OrdinaryArmor;
import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.Tool.*;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Endless.MOD_ID);

	//食物
	public static RegistryObject<Item> meatballs = ITEMS.register("meat_balls", () -> new OrdinaryFood(MyFoods.MEAT_BALLS));
	public static RegistryObject<Item> stew = ITEMS.register("stew", () -> new OrdinaryFood(MyFoods.STEW));

	//物品
	public static RegistryObject<Item> diamondLattice = ITEMS.register("diamond_lattice", OrdinaryItem::new);
	public static RegistryObject<Item> crystalMatrixIngot = ITEMS.register("crystal_matrix_ingot", OrdinaryItem::new);
	public static RegistryObject<Item> neutronPile = ITEMS.register("neutron_pile", OrdinaryItem::new);
	public static RegistryObject<Item> neutronNugget = ITEMS.register("neutron_nugget", OrdinaryItem::new);
	public static RegistryObject<Item> neutroniumIngot = ITEMS.register("neutronium_ingot", OrdinaryItem::new);
	public static RegistryObject<Item> endestPearl = ITEMS.register("endest_pearl", EndestPearl::new);
	public static RegistryObject<Item> infinityCatalyst = ITEMS.register("infinity_catalyst", OrdinaryItem::new);
	public static RegistryObject<Item> infinityIngot = ITEMS.register("infinity_ingot", OrdinaryItem::new);
	public static RegistryObject<Item> recordFragment = ITEMS.register("record_fragment", OrdinaryItem::new);
	public static RegistryObject<Item> matterCluster = ITEMS.register("matter_cluster", MatterCluster::new);
	public static RegistryObject<Item> neutroniumGear = ITEMS.register("neutronium_gear", OrdinaryItem::new);
	public static RegistryObject<Item> starFuel = ITEMS.register("star_fuel", OrdinaryItem::new);
	public static RegistryObject<Item> infinityNugget = ITEMS.register("infinity_nugget", OrdinaryItem::new);
	public static RegistryObject<Item> infinityArrow = ITEMS.register("infinity_arrow", InfinityArrow::new);

	//奇点
	public static RegistryObject<Item> singularityCoal = ITEMS.register("singularity_coal",
			() -> new Singularity(0x1f1e1e, Blocks.COAL_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityIron = ITEMS.register("singularity_iron",
			() -> new Singularity(0xe6e6e6, Blocks.IRON_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityGold = ITEMS.register("singularity_gold",
			() -> new Singularity(0xfffd90, Blocks.GOLD_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityDiamond = ITEMS.register("singularity_diamond",
			() -> new Singularity(0x9efeeb, Blocks.DIAMOND_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityNetherite = ITEMS.register("singularity_netherite",
			() -> new Singularity(0x4c4143, 0x4d494d));
	public static RegistryObject<Item> singularityEmerald = ITEMS.register("singularity_emerald",
			() -> new Singularity(0x82f6ad, Blocks.EMERALD_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityLapis = ITEMS.register("singularity_lapis",
			() -> new Singularity(0x31618b, Blocks.LAPIS_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityRedstone = ITEMS.register("singularity_redstone",
			() -> new Singularity(0xbd2008, Blocks.REDSTONE_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityQuartz = ITEMS.register("singularity_quartz",
			() -> new Singularity(0xeee6de, Blocks.QUARTZ_BLOCK.getMaterialColor().colorValue));
	public static RegistryObject<Item> singularityClay = ITEMS.register("singularity_clay",
			() -> new Singularity(0xacaebd, 0xafb9d6));

	//工具
	public static RegistryObject<Item> infinityPickaxe = ITEMS.register("infinity_pickaxe", InfinityPickaxe::new);
	public static RegistryObject<Item> infinityAxe = ITEMS.register("infinity_axe", InfinityAxe::new);
	public static RegistryObject<Item> infinityShovel = ITEMS.register("infinity_shovel", InfinityShovel::new);
	public static RegistryObject<Item> infinityHoe = ITEMS.register("infinity_hoe", InfinityHoe::new);
	public static RegistryObject<Item> infinitySword = ITEMS.register("infinity_sword", InfinitySword::new);
	public static RegistryObject<Item> skullfireSword = ITEMS.register("skullfire_sword", SkullfireSword::new);
	public static RegistryObject<Item> infinityBow = ITEMS.register("infinity_bow", InfinityBow::new);
	public static RegistryObject<Item> infinityCrossBow = ITEMS.register("infinity_crossbow", InfinityCrossBow::new);
	public static RegistryObject<Item> crystalSword = ITEMS.register("crystal_sword", () -> new OrdinarySword(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalPickaxe = ITEMS.register("crystal_pickaxe", () -> new OrdinaryPickaxe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalAxe = ITEMS.register("crystal_axe", () -> new OrdinaryAxe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalShovel = ITEMS.register("crystal_shovel", () -> new OrdinaryShovel(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> crystalHoe = ITEMS.register("crystal_hoe", () -> new OrdinaryHoe(MyItemTier.CRYSTAL));
	public static RegistryObject<Item> neutronSword = ITEMS.register("neutron_sword", () -> new OrdinarySword(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutronPickaxe = ITEMS.register("neutron_pickaxe", () -> new OrdinaryPickaxe(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutronAxe = ITEMS.register("neutron_axe", () -> new OrdinaryAxe(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutronShovel = ITEMS.register("neutron_shovel", () -> new OrdinaryShovel(MyItemTier.NEUTRON));
	public static RegistryObject<Item> neutronHoe = ITEMS.register("neutron_hoe", () -> new OrdinaryHoe(MyItemTier.NEUTRON));


	//盔甲
	public static RegistryObject<ArmorItem> infinityHead = ITEMS.register("infinity_head", () -> new InfinityArmor(EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> infinityChest = ITEMS.register("infinity_chest", () -> new InfinityArmor(EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> infinityLegs = ITEMS.register("infinity_legs", () -> new InfinityArmor(EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> infinityFeet = ITEMS.register("infinity_feet", () -> new InfinityArmor(EquipmentSlotType.FEET));
	public static RegistryObject<ArmorItem> crystalHead = ITEMS.register("crystal_head", () -> new OrdinaryArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> crystalChest = ITEMS.register("crystal_chest", () -> new OrdinaryArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> crystalLegs = ITEMS.register("crystal_legs", () -> new OrdinaryArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> crystalFeet = ITEMS.register("crystal_feet", () -> new OrdinaryArmor(MyArmorMaterial.CRYSTAL, EquipmentSlotType.FEET));
	public static RegistryObject<ArmorItem> neutronHead = ITEMS.register("neutron_head", () -> new OrdinaryArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.HEAD));
	public static RegistryObject<ArmorItem> neutronChest = ITEMS.register("neutron_chest", () -> new OrdinaryArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.CHEST));
	public static RegistryObject<ArmorItem> neutronLegs = ITEMS.register("neutron_legs", () -> new OrdinaryArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.LEGS));
	public static RegistryObject<ArmorItem> neutronFeet = ITEMS.register("neutron_feet", () -> new OrdinaryArmor(MyArmorMaterial.NEUTRON, EquipmentSlotType.FEET));


	//注册方块物品
	public static RegistryObject<BlockItem> blockInfinity = ITEMS.register("infinity_block",
			() -> new BlockItem(BlockRegistry.infinityBlock.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> crystalMatrix = ITEMS.register("crystal_matrix",
			() -> new BlockItem(BlockRegistry.crystalMatrix.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> neutroniumBlock = ITEMS.register("neutronium_block",
			() -> new BlockItem(BlockRegistry.neutroniumBlock.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> triple_craft = ITEMS.register("triple_craft",
			() -> new BlockItem(BlockRegistry.tripleCraft.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> double_craft = ITEMS.register("double_craft",
			() -> new BlockItem(BlockRegistry.doubleCraft.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> extremeCraftingTable = ITEMS.register("extreme_crafting_table",
			() -> new BlockItem(BlockRegistry.extremeCraftingTable.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> neutronCollector = ITEMS.register("neutron_collector",
			() -> new BlockItem(BlockRegistry.neutronCollector.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> denseNeutronCollector = ITEMS.register("dense_neutron_collector",
			() -> new BlockItem(BlockRegistry.denseNeutronCollector.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> doubleNeutronCollector = ITEMS.register("double_neutron_collector",
			() -> new BlockItem(BlockRegistry.doubleNeutronCollector.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> tripleNeutronCollector = ITEMS.register("triple_neutron_collector",
			() -> new BlockItem(BlockRegistry.tripleNeutronCollector.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> neutroniumCompressor = ITEMS.register("neutronium_compressor",
			() -> new BlockItem(BlockRegistry.neutroniumCompressor.get(), new Item.Properties().group(ModGroup.endless)));
	public static RegistryObject<BlockItem> compressorChest = ITEMS.register("compressor_chest",
			() -> new BlockItem(BlockRegistry.compressorChest.get(), new Item.Properties().group(ModGroup.endless).maxStackSize(1)));
	public static RegistryObject<BlockItem> infinityBox = ITEMS.register("infinity_box",
			() -> new BlockItem(BlockRegistry.infinityBox.get(), new Item.Properties().group(ModGroup.endless).maxStackSize(1)));

	//联动奇点
	public static RegistryObject<Item> singularityRuby; //红宝石
	public static RegistryObject<Item> singularityDragon; //龙晶
	public static RegistryObject<Item> singularitySpace; //空间
	public static RegistryObject<Item> singularityXray; //X光
	public static RegistryObject<Item> singularityUltra; //极限
	public static RegistryObject<Item> singularitySilver; //银
	public static RegistryObject<Item> singularityCopper; //铜
	public static RegistryObject<Item> singularityZinc; //锌
//	public static RegistryObject<Item> singularityPlatinum; //铂
//	public static RegistryObject<Item> singularityAluminum; //铝
	public static RegistryObject<Item> singularityNickel; //镍
	public static RegistryObject<Item> singularityLead; //铅
	public static RegistryObject<Item> singularityTin; //锡
//	public static RegistryObject<Item> singularityIridium; //铱

	//强力装备
	public static void registerSpaceArmsItem(){
		singularityRuby = ITEMS.register("singularity_ruby",
				() -> new Singularity(0xe02e35, 0xe25e63));
		singularityDragon = ITEMS.register("singularity_dragon",
				() -> new Singularity(0x550a56, 0xe04fe2));
		singularitySpace = ITEMS.register("singularity_space",
				() -> new Singularity(0x000000, 0xffffff));
		singularityXray = ITEMS.register("singularity_xray",
				() -> new Singularity(0x3affff, 0xe4ffff));
		singularityUltra = ITEMS.register("singularity_ultra",
				() -> new Singularity(0x7f6a00, 0x4cff00));
	}

	//冰与火
	public static void registerIafItem(){
		singularitySilver = ITEMS.register("singularity_silver",
				() -> new Singularity(0xf3faff, 0xe5ecf7));
		singularityCopper = ITEMS.register("singularity_copper",
				() -> new Singularity(0x95654c, 0xbd896d));
	}

	//机械动力
	public static void registerCreate(){
		singularityZinc = ITEMS.register("singularity_zinc", () -> new Singularity(0xaab59d, 0xb5d1ba));
	}

	//热力
	public static void registerThermal(){
//		singularityPlatinum = ITEMS.register("singularity_platinum", () -> new Singularity(0x649db2, 0xd3fcff));
//		singularityAluminum = ITEMS.register("singularity_aluminum", () -> new Singularity(0x, 0x));
		singularityNickel = ITEMS.register("singularity_nickel", () -> new Singularity(0x916e4d, 0xf9f5ab));
		singularityLead = ITEMS.register("singularity_lead", () -> new Singularity(0x232457, 0x393c61));
		singularityTin = ITEMS.register("singularity_tin", () -> new Singularity(0x517c88, 0x88a2a7));
//		singularityIridium = ITEMS.register("singularity_iridium", () -> new Singularity(0xaab59d, 0xb5d1ba));
	}
}
