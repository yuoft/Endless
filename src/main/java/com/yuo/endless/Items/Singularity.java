package com.yuo.endless.Items;

import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Singularity extends Item{

    private final int colorIndex; //底色
    private final int color; //主色

    public Singularity(int colorIndex, int color) {
        super(new Properties().group(ModGroup.endless));
        this.colorIndex = colorIndex;
        this.color = color;
    }

    //获取颜色代码
    public static int getColor(ItemStack stack, int colorIndex){
        int rgb = 0;
        int rgbIndex = 0;
        if (stack.getItem() instanceof Singularity){
            Singularity item = (Singularity) stack.getItem();
            if (item.color != 0) rgb = item.color;
            if (item.colorIndex != 0) rgbIndex = item.colorIndex;
        }
        return colorIndex == 1 ? rgb : rgbIndex;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == ItemRegistry.singularityClay.get()){
            tooltip.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("endless.text.itemInfo.singularity_clay"))));
        }
        if (item == ItemRegistry.singularityNetherite.get()){
            tooltip.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("endless.text.itemInfo.singularity_netherite"))));
        }
    }

}
