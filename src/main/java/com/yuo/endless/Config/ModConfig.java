package com.yuo.endless.Config;

import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Items.Singularity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModConfig {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ServerConfig SERVER;

    static {
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_CONFIG = specPair.getRight();
            SERVER = specPair.getLeft();
        }
    }

    public static Set<Block> pickaxeBlocks = new HashSet<>();
    public static Set<Block> axeBlocks = new HashSet<>();
    public static Set<Block> shovelBlocks = new HashSet<>();
    public static Set<Block> hoeBlocks = new HashSet<>();
    public static Set<String> errorInfo = new HashSet<>();

    public static Set<String> customSingularities = new HashSet<>();

    public static void loadConfig(){
        getToolBlocks(SERVER.pickaxeBlackList.get(), pickaxeBlocks);
        getToolBlocks(SERVER.axeBlackList.get(), axeBlocks);
        getToolBlocks(SERVER.shovelBlackList.get(), shovelBlocks);
        getToolBlocks(SERVER.hoeBlackList.get(), hoeBlocks);
        getCustomSingularities(SERVER.singularityCustomList.get(), customSingularities);
    }

    /**
     * 将方块添加到set
     * @param list 方块id
     * @param set 指定set集合
     */
    private static void getToolBlocks(List<? extends String> list, Set<Block> set){
        for (String s : list) {
            ResourceLocation resourceLocation = EndlessUtils.parse(s);
            Block block = BuiltInRegistries.BLOCK.get(resourceLocation);
            if (block == Blocks.AIR){
                errorInfo.add("error block for ["+ s + "]");
            }else set.add(block);
        }
    }

    /**
     * 通过配置文件获取自定义奇点
     * @param list 字符串列表
     * @param set 奇点集合
     */
    private static void getCustomSingularities(List<? extends String> list, Set<String> set){
        int size = list.size();
        if (size == 0) return;
        if (size % 3 != 0){
            errorInfo.add("error singularity definition for [String size]");
        }else {
            for (int i = 0; i < list.size(); i += 3){
                String s = list.get(i);
                if (!isTypeFlag(s)) {
                    errorInfo.add("Error singularity definition for ["+ list.get(i) +"]");
                    continue;
                }
                String s1 = list.get(i + 1);
                if (!s1.matches("^color0:0x[a-z0-9]{6}$")) {
                    errorInfo.add("Error singularity definition for ["+ s1 +"]");
                    continue;
                }
                String s2 = list.get(i + 2);
                if (!s2.matches("^color1:0x[a-z0-9]{6}$")) {
                    errorInfo.add("Error singularity definition for ["+ list.get(i + 2) +"]");
                    continue;
                }
                String[] split = s.split(":");
                String[] split1 = s1.split(":");
                String[] split2 = s2.split(":");
                Singularity.addSingularity(new Singularity.SingularityData(split[1],
                        Integer.parseInt(split1[1].substring(2), 16), Integer.parseInt(split2[1].substring(2), 16)));
                set.add(split[1]);
            }
        }
    }

    /**
     * 判断字符串是否符合要求
     * @param s 字符串
     * @return 符合
     */
    private static boolean isTypeFlag(String s){
        //字符串结构是否正确
        if (s.matches("^name:[a-z_]+$") && !s.matches(":_")){
            String[] split = s.split(":");
            return !Singularity.linkageTypes.contains(split[1]); //名称是否冲突
        }
        return false;
    }


}
