package com.yuo.endless.Armor;

import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Items.Tool.EndlessItemEntity;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InfinityArmor extends ArmorItem{

	public InfinityArmor(EquipmentSlotType slot) {
		super(MyArmorMaterial.INFINITY, slot, new Properties().maxStackSize(1).group(ModGroup.myGroup));
	}

	//不会触发末影人仇恨
	@Override
	public boolean isEnderMask(ItemStack stack, PlayerEntity player, EndermanEntity endermanEntity) {
		return true;
	}

	//盔甲在身上时触发效果
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		for (ItemStack next : player.getArmorInventoryList()) {
			if (!next.isEmpty()) {
				Item item = next.getItem();
				if (item.equals(ItemRegistry.infinityHead.get())) {
					if (player.areEyesInFluid(FluidTags.WATER)) { //玩家视线在水中
						player.setAir(300);
					}
					player.getFoodStats().addStats(20, 20f); //饱腹
					if (next.hasTag() && next.getOrCreateTag().getBoolean("flag"))
						player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 300, 0)); //夜视
				}
				if (item.equals(ItemRegistry.infinityChest.get())) {
					//清除所有负面效果
					Collection<EffectInstance> effects = player.getActivePotionEffects();
					if (effects.size() > 0) {
						List<Effect> bad = new ArrayList<>();
						effects.forEach((e) -> {
							if (!e.getPotion().isBeneficial())
								bad.add(e.getPotion());
						});
						if (bad.size() > 0) {
							bad.forEach((e) -> {
//								player.removeActivePotionEffect(e);
								player.clearActivePotions();
							});
						}
					}
				}
				if (item.equals(ItemRegistry.infinityLegs.get())) {
					if (player.isBurning()) player.extinguish();//着火时熄灭
					player.isImmuneToFire(); //免疫火伤
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) { //切换无尽装备模式
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote && playerIn.isSneaking()){
			CompoundNBT tags = stack.getTag();
			if (tags == null) {
				tags = new CompoundNBT();
				stack.setTag(tags);
			}
			tags.putBoolean("flag", !tags.getBoolean("flag"));
			playerIn.swingArm(handIn); //摆臂
		}
		return ActionResult.resultSuccess(stack);
	}

//	@Nullable
//	@Override
//	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
//		ModelRenderer wing = new ModelRenderer(_default);
//		wing.setRotationPoint(0, 0, 0);
//		wing.setTextureOffset(0,32);
//		wing.addBox(-16.0F, -16.0F, 10.0F, 32.0F, 16.0F,0, 0.0f, false);
//		_default.bipedBody.addChild(wing);
//		return (A) new ArmorWingModel();
//	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (slot == EquipmentSlotType.HEAD) {
			tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_head"));
			if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
				tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_head1"));
		}
		if (slot == EquipmentSlotType.CHEST) {
			tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_chest"));
			if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
				tooltip.add(new StringTextComponent(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + "100" +
						TextFormatting.RESET + "" + TextFormatting.BLUE + "% FlySpeed"));
		}
		if (slot == EquipmentSlotType.LEGS) {
			tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_legs"));
			if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
				tooltip.add(new StringTextComponent(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + "300" +
						TextFormatting.RESET + "" + TextFormatting.BLUE + "% WalkSpeed"));
		}
		if (slot == EquipmentSlotType.FEET) {
			tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_feet"));
			if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
				tooltip.add(new StringTextComponent(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + "400" +
						TextFormatting.RESET + "" + TextFormatting.BLUE + "% JumpHeight"));
		}

	}

	@Nullable
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EndlessItemEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

}
