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
import java.util.*;

//物质团
public class MatterCluster extends Item  {

    public static String MAIN_NBT = "matterCluster";

    public MatterCluster() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1));
    }

    /**
     *将物品map添加到物质团中 并返回物质团列表（数量较多时）
     * @param map 总物品map
     */
    public static List<ItemStack> createMatterCluster(Map<ItemStack, Integer> map){
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < Math.ceil(map.size() / (Config.SERVER.matterClusterMaxTerm.get() * 1.0d)); i++){ //物品组数量影响物质团数量 64 -- 1
            if (map.isEmpty()) return list;
            Map<ItemStack, Integer> spawnMap = spawnMap(map);
            int mapCount = getMaxCountFromMap(spawnMap);
            int maxCount = Config.SERVER.matterClusterMaxCount.get();
            for (int j = 0; j < Math.ceil(mapCount * 1.0d / maxCount); j++){ //数量数量限制 超过则新建物质团
                if (spawnMap.isEmpty()) return list;
                Map<ItemStack, Integer> newMap = spawnNewMap(spawnMap, maxCount);
                ItemStack stack = new ItemStack(ItemRegistry.matterCluster.get());
                setItemTag(stack, newMap);
                list.add(stack);
            }
        }
        return  list;
    }

    /**
     * 生成一个不超过单个物品数量限制的map
     * @param map 不超过64项的map
     * @param maxCount 限制数量
     * @return 满足数量限制的map
     */
    private static Map<ItemStack, Integer> spawnNewMap(Map<ItemStack, Integer> map, int maxCount){
        Map<ItemStack, Integer> countMap = new HashMap<>();
        Iterator<Map.Entry<ItemStack, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<ItemStack, Integer> entry = iterator.next();
            if (entry.getValue() > maxCount){
                int count = entry.getValue() - maxCount; //剩余数量
                countMap.put(entry.getKey(), maxCount);
                entry.setValue(count);
            }else {
                countMap.put(entry.getKey(), entry.getValue());
                iterator.remove();
            }
        }
        return countMap;
    }

    /**
     * 获取map中数量的最大数量值
     * @param map map
     * @return 最大值
     */
    private static int getMaxCountFromMap(Map<ItemStack, Integer> map){
        int max = 0;
        for (Map.Entry<ItemStack, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max){
                max = entry.getValue();
            }
        }
        return max;
    }

    /**
     * 生成一个满足数量的临时map 并清理掉总map的64项
     * @param map 总map
     * @return 临时含64项的map
     */
    private static Map<ItemStack, Integer> spawnMap(Map<ItemStack, Integer> map){
        Map<ItemStack, Integer> map1 = new HashMap<>();
        int num = 0;
        Iterator<Map.Entry<ItemStack, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<ItemStack, Integer> next = iterator.next();
            if (num <= Config.SERVER.matterClusterMaxTerm.get()){
                map1.put(next.getKey(), next.getValue());
                iterator.remove();
                num++;
            }else break;
        }
        return map1;
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
                tooltip.add(new StringTextComponent(matterCluster.size() + "/" + Config.SERVER.matterClusterMaxTerm.get() + new TranslationTextComponent("endless.text.itemInfo.matter_cluster2").getString()).mergeStyle(TextFormatting.RED));
            }else tooltip.add(new StringTextComponent(matterCluster.size() + "/" + Config.SERVER.matterClusterMaxTerm.get() + new TranslationTextComponent("endless.text.itemInfo.matter_cluster2").getString()));
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
        Integer maxCount = Config.SERVER.matterClusterMaxCount.get();
        //合并相同项
        for (Map.Entry<ItemStack, Integer> entry : map.entrySet()) {
            Iterator<Map.Entry<ItemStack, Integer>> iterator = map1.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<ItemStack, Integer> next = iterator.next();
                if (entry.getKey().isItemEqual(next.getKey())){ //将2中相同物品转移到1
                    if (entry.getValue() + next.getValue() <= maxCount){
                        entry.setValue(entry.getValue() + next.getValue());
                        iterator.remove(); //完全合并时删除2中的项
                    }else {
                        int count = next.getValue() + entry.getValue() - maxCount; //2 的余量
                        entry.setValue(maxCount);
                        next.setValue(count);
                    }
                }
            }
        }
        //添加新项
        if (map1.size() > 0){
            Iterator<Map.Entry<ItemStack, Integer>> iterator = map1.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<ItemStack, Integer> entry = iterator.next();
                if (map.size() < Config.SERVER.matterClusterMaxTerm.get()){ //物质团1未满
                    ItemStack key = entry.getKey();
                    if (map.containsKey(key)){
                        map.put(new ItemStack(key.getItem(), key.getCount() + 1), entry.getValue());
                    }else map.put(key, entry.getValue());
                    iterator.remove();
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
        return map.size() >= Config.SERVER.matterClusterMaxTerm.get();
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
