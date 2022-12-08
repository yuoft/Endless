package com.yuo.endless.Items;

import appeng.core.features.ItemStackSrc;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Singularity extends Item{
    //初始奇点
    private static final String[] types = new String[]{ "coal", "iron", "gold", "diamond", "netherite", "emerald", "lapis", "redstone", "quartz", "clay",
            "ruby", "dragon", "space", "xray", "ultra", "silver", "copper", "zinc", "nickel", "lead", "tin", "draconium", "awakened_draconium",
            "manasteel", "terrasteel", "elementium", "dark_matter", "red_matter", "cobalt", "manyullyn"};
    private static final Integer[] colors = new Integer[]{ 0x1f1e1e, 0xe6e6e6, 0xfffd90, 0x9efeeb, 0x4c4143, 0x82f6ad, 0x31618b, 0xbd2008, 0xeee6de, 0xacaebd,
            0xe02e35, 0x550a56, 0x000000, 0x3affff, 0x7f6a00, 0xf3faff, 0x95654c, 0xaab59d, 0x916e4d, 0x232457, 0x517c88, 0x6b369b, 0xf45100,
            0x49a5ee, 0x51dc24, 0xe464ff, 0x0c0c0c, 0x340303, 0x0753b8, 0xa97de0};
    private static final Integer[] colors2 = new Integer[]{ 0x050505, 0xf2f2f2, 0xffd83e, 0x70fbf0, 0x4d494d, 0x17dd62, 0x1b3588, 0x941400, 0xf2efed, 0xafb9d6,
            0xe25e63, 0xe04fe2, 0xffffff, 0xe4ffff, 0x4cff00, 0xe5ecf7, 0xbd896d, 0xb5d1ba, 0xf9f5ab, 0x393c61, 0x88a2a7, 0x6c389a, 0xeecb3d,
            0x50b4ff, 0x57ef26, 0xe784ff, 0x171717, 0x4d0404, 0x59a6ef, 0xcfacf9};
    //用于添加新奇点
    public static final List<String> TYPE = new ArrayList<>(); //奇点类型
    public static final List<Integer> INDEX = new ArrayList<>(); //底色
    public static final List<Integer> MAIN = new ArrayList<>(); //主色
    //nbt数据key字符
    public static final String NBT_MOD = "endless_singularity";
    public static final String NBT_TYPE = "type";
    public static final String NBT_INDEX = "index";
    public static final String NBT_MAIN = "main";

    public static ItemStack EMPTY; //默认奇点 错误奇点
    static {
        EMPTY = new ItemStack(new Singularity());
        setStackData(EMPTY, new SingularityData());
        TYPE.addAll(Arrays.asList(types));
        INDEX.addAll(Arrays.asList(colors));
        MAIN.addAll(Arrays.asList(colors2));
    }

    public Singularity() {
        super(new Properties().group(ModGroup.endless));
    }

    /**
     * 比较两个奇点是否相同 判断类型是否相同
     * @param singularity 1
     * @param singularity1 2
     * @return 相同 true
     */
    public static boolean isEqual(ItemStack singularity, ItemStack singularity1){
        SingularityData data = getStackData(singularity);
        SingularityData data1 = getStackData(singularity1);
        return data.type.equals(data1.type);
    }

    /**
     * 获取奇点的nbt数据
     * @param stack 奇点
     * @return 数据
     */
    private static SingularityData getStackData(ItemStack stack){
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = (CompoundNBT) tag.get(NBT_MOD);
        if (nbt != null){
            String type = nbt.getString(NBT_TYPE);
            int index = nbt.getInt(NBT_INDEX);
            int main = nbt.getInt(NBT_MAIN);
            return new SingularityData(type, index, main);
        }
        return new SingularityData();
    }

    /**
     * 奇点nbt数据类
     */
    private static class SingularityData{
        private final String type;
        private final int index;
        private final int main;
        SingularityData(){
            this.type = "null";
            this.index = 0xff00ff;
            this.main = 0x000000;
        }

        SingularityData(String typeIn, int indexIn, int mainIn){
            this.type = typeIn;
            this.index = indexIn;
            this.main = mainIn;
        }

        public String getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public int getMain() {
            return main;
        }
    }

    /**
     * 获取一个奇点
     * @param type 类型
     * @return 奇点
     */
    public static ItemStack getSingularity(String type){
        ItemStack stack = new ItemStack(EndlessItems.singularity.get());
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(NBT_TYPE, type);
        if (!TYPE.contains(type)) return EMPTY;
        for (int i = 0; i < TYPE.size(); i++){
            if (type.equals(TYPE.get(i))){
                nbt.putInt(NBT_INDEX, INDEX.get(i));
                nbt.putInt(NBT_MAIN, MAIN.get(i));
            }
        }
        tag.put(NBT_MOD, nbt);
        return stack;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = (CompoundNBT) tag.get(NBT_MOD);
        if (nbt != null){
            String type = nbt.getString(NBT_TYPE);
            return new TranslationTextComponent(this.getTranslationKey() + "_" + type);
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){
            for (int i = 0; i < TYPE.size(); i++) {
                ItemStack stack = new ItemStack(this);
                setStackData(stack, TYPE.get(i), INDEX.get(i), MAIN.get(i));
                items.add(stack);
            }
        }
    }

    /**
     * 设置奇点的nbt数据
     * @param stack 奇点
     * @param type 类型
     * @param index 颜色1
     * @param main 颜色2
     */
    private static void setStackData(ItemStack stack, String type, int index, int main){
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(NBT_TYPE, type);
        nbt.putInt(NBT_INDEX, index);
        nbt.putInt(NBT_MAIN, main);
        tag.put(NBT_MOD, nbt);
    }

    /**
     * 设置奇点nbt数据
     * @param stack 奇点
     * @param data 数据
     */
    private static void setStackData(ItemStack stack, SingularityData data){
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(NBT_TYPE, data.type);
        nbt.putInt(NBT_INDEX, data.index);
        nbt.putInt(NBT_MAIN, data.main);
        tag.put(NBT_MOD, nbt);
    }

    /**
     * 获取颜色代码
     * @param stack 物品
     * @param colorIndex 染色层
     * @return 颜色
     */
    public static int getColor(ItemStack stack, int colorIndex){
        int rgbIndex = 0;
        int rgbMain = 0;
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = (CompoundNBT) tag.get(NBT_MOD);
        if (nbt != null){
            rgbIndex = nbt.getInt(NBT_INDEX);
            rgbMain = nbt.getInt(NBT_MAIN);
        }
        return colorIndex == 1 ? rgbIndex : rgbMain;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT nbt = (CompoundNBT) tag.get(NBT_MOD);
        if (nbt != null){
            String type = nbt.getString(NBT_TYPE);
            if ("clay".equals(type)){
                tooltip.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("endless.text.itemInfo.singularity_clay"))));
            }
            if ("netherite".equals(type)){
                tooltip.add(new StringTextComponent(ColorText.makeSANIC(I18n.format("endless.text.itemInfo.singularity_netherite"))));
            }
        }
    }

}
