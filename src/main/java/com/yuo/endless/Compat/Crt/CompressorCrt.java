package com.yuo.endless.Compat.Crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.endless.CompressorRecipe")
@ZenRegister
public class CompressorCrt {

    /**
     * 压缩机配方添加
     * @param id 配方id
     * @param output 输出
     * @param count 所需材料数量
     * @param inputs 材料类别
     */
    @ZenCodeType.Method
    public static void addShaped(String id, IItemStack output, int count, IItemStack[] inputs) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ResourceLocation res = new ResourceLocation("crafttweaker", id);
                //转化为列表
                NonNullList<ItemStack> ingredients = NonNullList.create();
                for (IItemStack itemStack : inputs) {
                    ingredients.add(itemStack.getInternal());
                }

                //添加到模组配方管理
                ItemStack internal = output.getInternal();
                String type = internal.getOrCreateTag().getString(Singularity.NBT_TYPE);
                NeutroniumRecipe recipe = new NeutroniumRecipe(res, ingredients, count, Singularity.getSingularity(type));
                CompressorManager.addRecipe(recipe);
            }

            @Override
            public String describe() {
                return "Adding CompressorRecipe recipe for " + output.getCommandString();
            }
        });
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ItemStack internal = stack.getInternal();
                String type = internal.getOrCreateTag().getString(Singularity.NBT_TYPE);
                CompressorManager.removeRecipe(Singularity.getSingularity(type));
            }

            @Override
            public String describe() {
                return "Removing CompressorRecipe recipes for " + stack.getCommandString();
            }
        });
    }
}
