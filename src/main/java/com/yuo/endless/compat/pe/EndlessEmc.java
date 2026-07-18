package com.yuo.endless.compat.pe;

import com.yuo.endless.Endless;
import com.yuo.endless.items.EndlessItems;
import moze_intel.projecte.api.imc.CustomEMCRegistration;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.emc.mappers.APICustomEMCMapper;
import net.minecraft.resources.ResourceLocation;

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
