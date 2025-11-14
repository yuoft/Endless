package com.yuo.endless.Mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.InfinityItem;
import com.yuo.endless.Items.Tool.InfinityAxe;
import com.yuo.endless.Items.Tool.InfinityShovel;
import com.yuo.endless.Items.Tool.InfinitySword;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    /**
     * @author yuo
     * @reason 修复give指令获取无尽物品时得到2份
     */
    @Inject(method = "giveItem", at = {@At("HEAD")}, cancellable = true)
    private static void giveItem(CommandSourceStack source, ItemInput itemIn, Collection<ServerPlayer> targets, int count, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        Item item = itemIn.getItem();
        if (EventHandler.isInfinityItem(item) || item instanceof InfinityItem) {
            for(ServerPlayer serverPlayer : targets) {
                int i = count;

                while(i > 0) {
                    int j = Math.min(itemIn.getItem().getMaxStackSize(), i);
                    i -= j;
                    ItemStack itemstack = itemIn.createItemStack(j, false);
                    boolean flag = serverPlayer.getInventory().add(itemstack);
                    if (flag && itemstack.isEmpty()) {
                        itemstack.setCount(1);
                        if (itemstack.getItem().hasCustomEntity(itemstack) && itemstack.getItem().getDescriptionId().contains("endless")){
                            System.out.println("Fix give endless item, but get two bug.");
                        }else {
                            ItemEntity itementity1 = serverPlayer.drop(itemstack, false);
                            if (itementity1 != null) {
                                itementity1.makeFakeItem();
                            }
                        }

                        serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        serverPlayer.containerMenu.broadcastChanges();
                    } else {
                        ItemEntity itementity = serverPlayer.drop(itemstack, false);
                        if (itementity != null) {
                            itementity.setDefaultPickUpDelay();
                            itementity.setThrower(serverPlayer.getUUID());
                        }
                    }
                }
            }

            if (targets.size() == 1) {
                source.sendSuccess(() ->
                {
                    try {
                        return Component.translatable("commands.give.success.single", count, itemIn.createItemStack(count, false).getDisplayName(), targets.iterator().next().getDisplayName());
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }, true);
            } else {
                source.sendSuccess(() ->
                {
                    try {
                        return Component.translatable("commands.give.success.single", count, itemIn.createItemStack(count, false).getDisplayName(), targets.size());
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }, true);
            }
            cir.setReturnValue(targets.size());
        }
    }
}
