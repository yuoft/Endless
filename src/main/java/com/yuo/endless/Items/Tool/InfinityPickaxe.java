package com.yuo.endless.Items.Tool;

import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.Consumer;

public class InfinityPickaxe extends PickaxeItem {

    private final ToolHelper handler;

    public InfinityPickaxe() {
        super(EndlessItemTiers.INFINITY_TOOL, -3, -2.8f, new Properties().tab(EndlessTab.endless).fireResistant());
        this.handler = new ToolHelper();
    }

    //锤形态下挖掘速度变慢
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getOrCreateTag().getBoolean("hammer")) {
            return 6.0F;
        }
        if (stack.isCorrectToolForDrops(state)){
            return this.getTier().getSpeed(); //对镐类挖掘方块有极高速度加成
        }
        return Math.max(super.getDestroySpeed(stack, state), 9.0f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) { //潜行右键切换形态
            CompoundTag tags = stack.getOrCreateTag();
            Enchantment enchantment = Enchantments.BLOCK_FORTUNE;
            int fortune = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
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
                stack.enchant(enchantment, 10);
            }
            tags.putBoolean("hammer", !tags.getBoolean("hammer"));
            player.swing(hand); //摆臂
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getTag() != null && stack.getTag().getBoolean("hammer")){
            if (!(target instanceof Player) && attacker instanceof Player){ //锤形态对非玩家生物进行高额击飞
                target.setDeltaMovement(-Math.sin(attacker.yRotO * (float) Math.PI / 180.0F) * 10 * 0.5F, 2.0D,
                        Math.cos(attacker.yRotO * (float) Math.PI / 180.0F) * 10 * 0.5F);
            }
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (itemstack.getOrCreateTag().getBoolean("hammer")){
            Level world = player.level;
            if (!world.isClientSide){
                BlockState state = world.getBlockState(pos);
                if (itemstack.isCorrectToolForDrops(state) && ToolHelper.MATERIAL_PICKAXE.contains(state.getMaterial())){ //初始坐标能挖掘
                    handler.onBlockStartBreak(itemstack, world, pos, player, 7);
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        int damage = stack.getDamageValue();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
        }
    }

    //创建一个能被快速捡起的物品实体
    @org.jetbrains.annotations.Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return new EndlessItemEntity(level, location, stack);
    }

    //允许创建自定义物品实体
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
