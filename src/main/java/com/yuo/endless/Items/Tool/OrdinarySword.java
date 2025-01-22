package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class OrdinarySword extends SwordItem{
    public static String NBT_TIMER = "timer";

    public OrdinarySword(IItemTier tier) {
        super(tier, 0,-2.4f, new Properties().group(EndlessTab.endless).isImmuneToFire());
    }


//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
//        ItemStack itemstack = playerIn.getHeldItem(handIn);
//        playerIn.setActiveHand(handIn);
//        return ActionResult.resultConsume(itemstack);
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity living, int i) {
//        if (getTime(i) >= 250 && living instanceof PlayerEntity){
//            PlayerEntity player  = (PlayerEntity) living;
//            player.getPersistentData().putInt(NBT_TIMER, 0);
//            AxisAlignedBB grow = living.getBoundingBox().grow(5);
//            world.getEntitiesWithinAABB(LivingEntity.class, grow).forEach(entity -> {
//                if (entity != living){
//                    entity.onKillCommand();
//                }
//            });
//            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
//        }
//    }

//    @Override
//    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
//        int time = getTime(count);
//        player.getPersistentData().putInt(NBT_TIMER, time);
//        if (time == 10){
//            World world = player.world;
//            SwordVoidEntity swordVoid = new SwordVoidEntity(EntityRegistry.INFINITY_SWORD.get(), world);
//            BlockPos pos = player.getPosition();
//            BlockPos offset = pos.offset(player.getHorizontalFacing(), 3);
//            swordVoid.setPositionAndUpdate(offset.getX(), offset.getY(), offset.getZ());
//            world.addEntity(swordVoid);
//        }
//    }
//
//    private int getTime(int count){
//        return getUseDuration(new ItemStack(this)) - count;
//    }
//
//    @Override
//    public int getUseDuration(ItemStack stack) {
//        if (stack.getItem() == EndlessItems.crystalMatrixSword.get()) return 600;
//        return super.getUseDuration(stack);
//    }
//
//    @Override
//    public UseAction getUseAction(ItemStack stack) {
//        if (stack.getItem() == EndlessItems.crystalMatrixSword.get()) return UseAction.BOW;
//        return super.getUseAction(stack);
//    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,0.04d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
