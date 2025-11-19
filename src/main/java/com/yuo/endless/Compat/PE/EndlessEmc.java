package com.yuo.endless.Compat.PE;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.*;
import moze_intel.projecte.api.imc.CustomEMCRegistration;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.emc.SimpleGraphMapper;
import moze_intel.projecte.emc.arithmetic.HiddenBigFractionArithmetic;
import moze_intel.projecte.emc.collector.MappingCollector;
import moze_intel.projecte.emc.mappers.APICustomEMCMapper;
import moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EndlessEmc {

    /**
     * 模组物品Emc设置
     */
    public static void registerEmc(){
        init(EndlessItems.neutroniumPile.getId(), 12800);
        init(EndlessItems.infinityFluidBucket.getId(), 24584370);
        init(EndlessItems.eternalSingularity.getId(), 214748364);
    }

    private static void init(ResourceLocation res, long v){
        APICustomEMCMapper.INSTANCE.registerCustomEMC(Endless.MOD_ID, new CustomEMCRegistration(NSSItem.createItem(res), v));
    }

    public static String name(String name) {
        return "Endless Avaritia" + name + "Mapper";
    }

}
