package com.yuo.endless.Items.Tool;

import com.yuo.endless.EndlessTab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SkullfireSword extends SwordItem implements IManaUsingItem {
    public SkullfireSword() {
        super(MyItemTier.SKULL_FIRE, 0, -2.4f, new Properties().group(EndlessTab.endless).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("endless.text.itemInfo.skullfire_sword"));
    }

    @Override
    public boolean usesMana(ItemStack itemStack) {
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, 20);
    }
}
