package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndestPearlEntity;
import com.yuo.endless.Entity.EntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EndestPearl extends Item {

    public EndestPearl() {
        super(new Properties().tab(EndlessTab.endless).stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL,
                0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            EndestPearlEntity pearl = new EndestPearlEntity(EntityRegistry.ENDEST_PEARL.get(), player, world);
            pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(pearl);
            player.getCooldowns().addCooldown(stack.getItem(), 30);
        }
        return InteractionResultHolder.success(stack);
    }

}
