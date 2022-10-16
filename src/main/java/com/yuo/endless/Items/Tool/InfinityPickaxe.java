package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class InfinityPickaxe extends PickaxeItem {

    private final ToolHelper hander;

    public InfinityPickaxe() {
        super(MyItemTier.INFINITY_TOOL, -3, -2.8f, new Properties().group(ModGroup.endless).isImmuneToFire());
        this.hander = new ToolHelper();
    }

    //锤形态下挖掘速度变慢
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getOrCreateTag().getBoolean("hammer")) {
            return 6.0F;
        }
        if (state.getHarvestTool() == ToolType.PICKAXE){
            return this.getTier().getEfficiency(); //对镐类挖掘方块有极高速度加成
        }
        return Math.max(super.getDestroySpeed(stack, state), 9.0f);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) { //潜行右键切换形态
            CompoundNBT tags = stack.getOrCreateTag();
            Enchantment enchantment = Enchantments.FORTUNE;
            int fortune = EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
            if (fortune > 0) { //添加附魔
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    if (entry.getKey() == enchantment){
                        map.keySet().removeIf(enchant -> enchant == enchantment);
                        map.put(enchantment, 10);
                        EnchantmentHelper.setEnchantments(map, stack);
                    }
                }

            }else {
                stack.addEnchantment(enchantment, 10);
            }
            tags.putBoolean("hammer", !tags.getBoolean("hammer"));
            player.swingArm(hand); //摆臂
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getTag() != null && stack.getTag().getBoolean("hammer")){
            if (!(target instanceof PlayerEntity) && attacker instanceof PlayerEntity){ //锤形态对非玩家生物进行高额击飞
                target.addVelocity(-MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F, 2.0D, MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F);
            }
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        if (itemstack.getOrCreateTag().getBoolean("hammer")){
            World world = player.world;
            if (!world.isRemote){
                BlockState state = world.getBlockState(pos);
                if (state.getHarvestLevel() <= itemstack.getHarvestLevel(ToolType.PICKAXE, player, state)
                        && ToolHelper.MATERIAL_PICKAXE.contains(state.getMaterial())){ //初始坐标能挖掘
                    hander.onBlockStartBreak(itemstack, world, pos, player, 7, ToolType.PICKAXE);
                }
            }
        }
        return false; //范围挖掘成功与否与挖掘此方块无关
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int damage = stack.getDamage();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    //创建一个能被快速捡起的物品实体
    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }

    //允许创建自定义物品实体
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
