package com.yuo.endless.Items;

import com.yuo.endless.Entity.EndestPearlEntity;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Client.Render.Pulse.PulseRender;
import com.yuo.endless.EndlessTab;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class EndestPearl extends Item {

    public EndestPearl() {
        super(new Properties().group(EndlessTab.endless).setISTER(() -> PulseRender::new).maxStackSize(16));
    }

    @Override
    public ActionResult onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.abilities.isCreativeMode) {
            stack.shrink(1);
        }
        world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EndestPearlEntity pearl = new EndestPearlEntity(EntityRegistry.ENDEST_PEARL.get(), player, world);
            pearl.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(pearl);
            player.getCooldownTracker().setCooldown(stack.getItem(), 30);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}
