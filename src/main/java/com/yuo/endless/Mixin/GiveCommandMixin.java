package com.yuo.endless.Mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.command.impl.GiveCommand;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collection;

@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    /**
     * @author yuo
     * @reason 修复give指令获取无尽物品时得到2份
     */
    @Overwrite
    private static int giveItem(CommandSource source, ItemInput itemIn, Collection<ServerPlayerEntity> targets, int count) throws CommandSyntaxException {
        for(ServerPlayerEntity serverplayerentity : targets) {
            int i = count;

            while(i > 0) {
                int j = Math.min(itemIn.getItem().getMaxStackSize(), i);
                i -= j;
                ItemStack itemstack = itemIn.createStack(j, false);
                boolean flag = serverplayerentity.inventory.addItemStackToInventory(itemstack);
                if (flag && itemstack.isEmpty()) {
                    itemstack.setCount(1);
                    if (itemstack.getItem().hasCustomEntity(itemstack) && itemstack.getItem().getRegistryName() != null
                            && "endless".equals(itemstack.getItem().getRegistryName().getNamespace())){
                        System.out.println("Fix give endless item, but get two bug.");
                    }else {
                        ItemEntity itementity1 = serverplayerentity.dropItem(itemstack, false);
                        if (itementity1 != null) {
                            itementity1.makeFakeItem();
                        }
                    }

                    serverplayerentity.world.playSound(null, serverplayerentity.getPosX(), serverplayerentity.getPosY(), serverplayerentity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((serverplayerentity.getRNG().nextFloat() - serverplayerentity.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    serverplayerentity.container.detectAndSendChanges();
                } else {
                    ItemEntity itementity = serverplayerentity.dropItem(itemstack, false);
                    if (itementity != null) {
                        itementity.setNoPickupDelay();
                        itementity.setOwnerId(serverplayerentity.getUniqueID());
                    }
                }
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(new TranslationTextComponent("commands.give.success.single", count, itemIn.createStack(count, false).getTextComponent(), targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(new TranslationTextComponent("commands.give.success.single", count, itemIn.createStack(count, false).getTextComponent(), targets.size()), true);
        }

        return targets.size();
    }
}
