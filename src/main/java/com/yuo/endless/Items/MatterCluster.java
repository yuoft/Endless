package com.yuo.endless.Items;

import com.yuo.endless.Config.Config;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//物质团
public class MatterCluster extends Item  {

    public static String MAIN_NBT = "matterCluster";

    public MatterCluster() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1));
    }

    /**
     *将物品map添加到物质团中 并返回一个物质团
     * @param map 物品map
     */
    public static ItemStack setMap(Map<ItemStack, Integer> map){
        if (map.size() > 64) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(ItemRegistry.matterCluster.get());
        setItemTag(stack, map);
        return  stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!stack.getOrCreateTag().contains("matterCluster")){
            return ;
        }
        ListNBT matterCluster = (ListNBT) stack.getOrCreateTag().get("matterCluster");
        if (matterCluster != null){
            //物品种类数量信息
            if (isMaxSize(stack)){
                tooltip.add(new StringTextComponent(matterCluster.size() + "/64" + new TranslationTextComponent("endless.text.itemInfo.matter_cluster2").getString()).mergeStyle(TextFormatting.RED));
            }else tooltip.add(new StringTextComponent(matterCluster.size() + "/64" + new TranslationTextComponent("endless.text.itemInfo.matter_cluster2").getString()));
            tooltip.add(new StringTextComponent(""));

            if (Screen.hasShiftDown()) { //在物品上按下shift键
                for (INBT inbt : matterCluster) {
                    CompoundNBT nbt = (CompoundNBT) inbt;
                    ItemStack read = ItemStack.read(nbt);
                    int count = nbt.getInt("count");

                    tooltip.add(new StringTextComponent(read.getItem().getRarity(read).color + read.getDisplayName().getString() + TextFormatting.GRAY + " x " + count));
                }
            } else {
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.matter_cluster"));
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.matter_cluster1"));
            }
        }
    }

    /**
     * 设置物品tag 清除之前数据后添加
     * @param stack 物质团
     * @param map 物品map
     */
    public static void setItemTag(ItemStack stack, Map<ItemStack, Integer> map){
        ListNBT listNBT = new ListNBT();

        for (ItemStack key : map.keySet()) { //遍历所有键
            CompoundNBT nbt = new CompoundNBT();
            key.write(nbt);
            nbt.putInt("count", map.get(key));
            listNBT.add(nbt);
        }
        CompoundNBT orCreateTag = stack.getOrCreateTag();
        if (orCreateTag.contains(MAIN_NBT)){
            orCreateTag.remove(MAIN_NBT);
        }
        orCreateTag.put(MAIN_NBT, listNBT);
    }

    /**
     * 获取物品tag 获取物质团的所有物品
     * @param stack 物质团
     * @return 物品map
     */
    public static Map<ItemStack, Integer> getItemTag(ItemStack stack){
        CompoundNBT orCreateTag = stack.getOrCreateTag();
        Map<ItemStack, Integer> data = new HashMap<>();
        if (orCreateTag.contains(MAIN_NBT)){
            ListNBT matterCluster = (ListNBT) orCreateTag.get(MAIN_NBT);
            if (matterCluster != null){
                for (INBT inbt : matterCluster) {
                    CompoundNBT nbt = (CompoundNBT) inbt;
                    data.put(ItemStack.read(nbt), nbt.getInt("count"));
                }
            }
        }
        return data;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote){
            //生成物质团类所有物品
            Map<ItemStack, Integer> map = getItemTag(stack);
            if (map.size() > 0){
                for (ItemStack key : map.keySet()) {
                    Integer integer = map.get(key);
                    int size = key.getMaxStackSize();
                    int i = integer % size; //不足一组部分
                    int j = integer / size; //几组
                    addEntityItem(worldIn, new ItemStack(key.getItem(), i), playerIn);
                    for (int m = 0; m < j; m++){
                        addEntityItem(worldIn, new ItemStack(key.getItem(), size), playerIn);
                    }
                }
            }

        }
        stack.shrink(1);
        return ActionResult.resultSuccess(stack);
    }

    private static void addEntityItem(World world, ItemStack stack, PlayerEntity player){
        world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), stack));
    }

    /**
     * 将物质团2的物品追加到1中
     * @param stack 物质团1
     * @param itemStack 物质团2
     * @return 是否有剩余物品 有剩余表示物质团1无法完全合并2
     */
    public static boolean addItem(ItemStack stack, ItemStack itemStack){
        Map<ItemStack, Integer> map = getItemTag(stack);
        Map<ItemStack, Integer> map1 = getItemTag(itemStack);
        //合并相同项
        for (Map.Entry<ItemStack, Integer> entry : map.entrySet()) {
            Iterator<Map.Entry<ItemStack, Integer>> iterator = map1.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<ItemStack, Integer> next = iterator.next();
                if (entry.getKey().isItemEqual(next.getKey())){ //将2中相同物品转移到1
                    if (entry.getValue() + next.getValue() <= Config.SERVER.matterClusterMaxCount.get()){
                        entry.setValue(entry.getValue() + next.getValue());
                    }else {
                        Integer maxCount = Config.SERVER.matterClusterMaxCount.get();
                        int count = maxCount - entry.getValue(); //2 的余量
                        entry.setValue(maxCount);
                        next.setValue(count);
                    }
                    iterator.remove();
                }
            }
        }
        //添加新项
        if (map1.size() > 0){
            for (Map.Entry<ItemStack, Integer> entry : map1.entrySet()) {
                if (map.size() < 64){ //物质团1未满
                    map.put(entry.getKey(), entry.getValue());
                }
            }
        }
        //重新添加修改后的数据
        setItemTag(stack, map);
        if (map1.size() == 0){ //2以空，则清除nbt
            itemStack.getOrCreateTag().remove(MAIN_NBT);
        }else setItemTag(itemStack, map1);
        return map1.size() > 0;
    }

    /**
     * 判断一个物质团存储的物品是否以满
     * @param stack 物质团
     * @return 满 true
     */
    public static boolean isMaxSize(ItemStack stack){
        Map<ItemStack, Integer> map = getItemTag(stack);
        if (map.size() == 0) return false;
        return map.size() >= 64;
    }

    /**
     * 判断物质团是否为空
     * @param stack 物质团
     * @return 空：true
     */
    public static boolean isEmpty(ItemStack stack){
        if (!stack.getOrCreateTag().contains(MAIN_NBT)) return true;
        Map<ItemStack, Integer> itemTag = getItemTag(stack);
        return itemTag.size() == 0;
    }

    /**
     * 获取玩家身上未满的物资团
     * @param player 玩家
     * @return 物质团 无则返回EMPTY
     */
    public static ItemStack getMatterCluster(PlayerEntity player, int slot){
        PlayerInventory inventory = player.inventory;
        for (int i = 0; i < inventory.getSizeInventory(); i++){
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() == ItemRegistry.matterCluster.get() && !isMaxSize(stack)){
                if (slot == i) continue;
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 合并物质团
     * @param stack 需要合并的拾取物质团
     * @param player 玩家
     * @return 合并后是否有剩余， 无：true
     */
    public static boolean mergeMatterCluster(ItemStack stack, PlayerEntity player){
        while (true){
            boolean empty = isEmpty(stack);
            ItemStack matterCluster = getMatterCluster(player, -1);
            if (empty || matterCluster.isEmpty()) break; //当拾取物质团已无物品 或 玩家身上无未满物质团时 退出循环
            if (!addItem(matterCluster, stack)) break; //完全合并时退出循环
        }
        return isEmpty(stack);
    }

    /**
     * 合并物质团
     * @param stack 要合并的
     * @param player 玩家
     * @param slot 槽位id
     * @return 是否有剩余
     */
    public static boolean mergeMatterCluster(ItemStack stack, PlayerEntity player, int slot){
        while (true){
            boolean empty = isEmpty(stack);
            ItemStack matterCluster = getMatterCluster(player, slot);
            if (empty || matterCluster.isEmpty()) break;
            if (!addItem(matterCluster, stack)) break;
        }
        return isEmpty(stack);
    }
}
