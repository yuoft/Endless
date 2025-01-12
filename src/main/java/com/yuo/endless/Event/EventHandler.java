package com.yuo.endless.Event;

import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.Armor.InfinityArmor;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.*;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.NetWork.TotemPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = Endless.MOD_ID)
public class EventHandler {
    public static List<String> playersWithHead = new ArrayList<>();
    public static List<String> playersWithChest = new ArrayList<>();
    public static List<String> playersWithLegs = new ArrayList<>();
    public static List<String> playersWithFeet = new ArrayList<>();

    //无尽鞋子 无摔落伤害
    @SubscribeEvent
    public static void playerFall(LivingFallEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Player player) {
            String key = player.getGameProfile().getName() + ":" + player.level.isClientSide;
            if (playersWithFeet.contains(key)) {
                event.setCanceled(true);
            }
        }
    }

    //无尽装备 不受伤害
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void opArmsImmuneDamage(LivingDamageEvent event){
        LivingEntity living = event.getEntityLiving();
        Boolean hasChest = living.getItemBySlot(EquipmentSlot.CHEST).getItem() == EndlessItems.infinityChest.get();
        Boolean hasLeg = living.getItemBySlot(EquipmentSlot.LEGS).getItem() == EndlessItems.infinityLegs.get();
        Boolean hasHead = living.getItemBySlot(EquipmentSlot.HEAD).getItem() == EndlessItems.infinityHead.get();
        Boolean hasFeet = living.getItemBySlot(EquipmentSlot.FEET).getItem() == EndlessItems.infinityFeet.get();
        float amount = event.getAmount();
        if (living instanceof Player player){ //怪物无法触发全部伤害减免
            if (!InfinityDamageSource.isInfinity(event.getSource())){ //非无尽伤害才进行减免
                if (isInfinite(player)){
                    event.setAmount(0.0f);
                    event.setCanceled(true);
                }else if (hasChest || hasFeet || hasHead || hasLeg){
                    event.setAmount(amount * 0.001f);
                }
            }
        }else if (hasChest || hasFeet || hasHead || hasLeg){ //怪物也可触发减伤
            event.setAmount(amount * 0.001f); //减伤99.9%
        }
    }
    //无尽胸甲 飞行 护腿 行走速度增加
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void updatePlayerAbilityStatus(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Player player) {
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            boolean hasHead = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EndlessItems.infinityHead.get();
            boolean hasChest = chest.getItem() == EndlessItems.infinityChest.get();
            boolean hasLegs = legs.getItem() == EndlessItems.infinityLegs.get();
            boolean hasFeet = player.getItemBySlot(EquipmentSlot.FEET).getItem() == EndlessItems.infinityFeet.get();
            //防止其它模组飞行装备无法使用
            String key = player.getGameProfile().getName() + ":" + player.level.isClientSide;
            //head
            if (playersWithHead.contains(key)){
                if (hasHead){

                }else {
                    playersWithHead.remove(key);
                }
            }else if (hasHead){
                playersWithHead.add(key);
            }
            //chest
            if (playersWithChest.contains(key)) {
                if (hasChest) {
                    if (!player.getAbilities().mayfly)
                        player.getAbilities().mayfly = true;
                    if (chest.getOrCreateTag().getBoolean("flag") && player.level.isClientSide){
                        player.getAbilities().setFlyingSpeed(0.05f + 0.05f * Config.SERVER.infinityChestFly.get());
                    }
                }else {
                    if (player.getAbilities().mayfly && !player.isCreative() && !player.isSpectator()) {
                        player.getAbilities().mayfly = false;
                        if (player.getAbilities().flying)
                            player.getAbilities().flying = false;
                    }
                    if (player.level.isClientSide)
                        player.getAbilities().setFlyingSpeed(0.05f);
                    playersWithChest.remove(key);
                }
            }else if (hasChest) {
                playersWithChest.add(key);
            }
            //legs
            if (playersWithLegs.contains(key)) {
                AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (hasLegs) {
                    if (legs.getOrCreateTag().getBoolean("flag") && attribute != null && !attribute.hasModifier(InfinityArmor.modifierWalk))
                        attribute.addPermanentModifier(InfinityArmor.modifierWalk); //行走速度
                }else {
                    if (attribute != null && attribute.hasModifier(InfinityArmor.modifierWalk))
                        attribute.removeModifier(InfinityArmor.modifierWalk);
                    playersWithLegs.remove(key);
                }
            } else if (hasLegs) {
                playersWithLegs.add(key);
            }
            //feet
            if (playersWithFeet.contains(key)){
                if (hasFeet){
                    if (feet.getOrCreateTag().getBoolean("flag") && !player.isCrouching()){
                        player.maxUpStep = 1.25f; //上坡高度
                    }
                }else {
                    player.maxUpStep = 0.6f;
                    playersWithFeet.remove(key);
                }
            }else if (hasFeet){
                playersWithFeet.add(key);
            }

            boolean hasSword = player.getUseItem().getItem() == EndlessItems.infinitySword.get();
            if (hasSword){
                InfinitySword.clearBuff(player);
            }
        }
    }

    //无尽鞋子 增加跳跃高度
    @SubscribeEvent
    public static void jumpBoost(LivingEvent.LivingJumpEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Player player) {
            String key = player.getGameProfile().getName() + ":" + player.level.isClientSide;
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            if (playersWithFeet.contains(key) && feet.hasTag() && feet.getOrCreateTag().getBoolean("flag")) {
                player.setDeltaMovement(0, 0.42f + 0.1f * (Config.SERVER.infinityFeetJump.get() + 1), 0);
            }
        }
    }

    //玩家合成无尽装备时添加附魔
    @SubscribeEvent
    public static void opTool(PlayerEvent.ItemCraftedEvent event){
        ItemStack stack = event.getCrafting();
        if (stack.getItem().equals(EndlessItems.infinitySword.get())){
            Map<Enchantment, Integer> map = new HashMap<>();
            map.put(Enchantments.MOB_LOOTING, 10);
            EnchantmentHelper.setEnchantments( map, stack);
        }
        if (isInfinityItem(stack.getItem())){
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
        }
    }

    //原版掉落修改
    @SubscribeEvent
    public static void dropsItem(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        Entity source = event.getSource().getDirectEntity();
        if (!(source instanceof Player player)) return;

        if (entityLiving instanceof AbstractSkeleton){ //炽焰之啄颅剑击杀小白掉落调零骷髅头
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.getItem() instanceof SkullfireSword){
                spawnDrops(Items.WITHER_SKELETON_SKULL, 1, entityLiving.level, entityLiving.getOnPos(), event);
            }
        }
    }

    //无尽镐 锤形态右键获取基岩
    @SubscribeEvent
    public static void breakBedrock(PlayerInteractEvent.RightClickBlock event){
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level world = event.getWorld();
        Player player = event.getPlayer();
        if (stack.getItem() instanceof InfinityPickaxe && world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)){
            if (Config.SERVER.isBreakBedrock.get() && stack.getOrCreateTag().getBoolean("hammer")){
                world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), new ItemStack(Blocks.BEDROCK)));
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(LivingDeathEvent event) { //不能被无尽伤害外的攻击杀死
        if (event.getEntityLiving() instanceof Player player) {
            if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
                player.setHealth(player.getMaxHealth());
                event.setCanceled(true);
            }
            ItemStack totem = getPlayerBagItem(player);
            if (!totem.isEmpty()){
                NetWorkHandler.INSTANCE.sendToServer(new TotemPacket(totem, player));
                if (player instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                    CriteriaTriggers.USED_TOTEM.trigger(serverplayer, totem);
                }
                player.removeAllEffects();
                int damage = totem.getDamageValue();
                if (damage == 9){ //最后一次
                    player.setHealth(player.getMaxHealth());
                    player.addEffect(new MobEffectInstance(MobEffects.JUMP, 800, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 1));
                    attackAOE(player, 8, 1000.0f);
                    player.sendMessage(new TranslatableComponent("endless.text.msg.totem_break"), UUID.randomUUID());
                }else {
                    player.setHealth(10.0F);
                }
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2600, 4));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 700, 2));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1100, 0));
                totem.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                event.setCanceled(true);
            }
        }
    }

    //在不利因素下，将挖掘速度恢复为正常速度
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void dignity(PlayerEvent.BreakSpeed event) {
        if (!event.getEntityLiving().getUseItem().isEmpty()) {
            LivingEntity living = event.getEntityLiving();
            ItemStack held = living.getUseItem();
            if (held.getItem() == EndlessItems.infinityPickaxe.get() || held.getItem() == EndlessItems.infinityShovel.get() ||
            held.getItem() == EndlessItems.infinityAxe.get() || held.getItem() == EndlessItems.infinityHoe.get()) {
                MobEffectInstance effect = living.getEffect(MobEffects.DIG_SLOWDOWN); //挖掘疲劳 每级将至原本30%
                if (effect != null){
                    int amplifier = effect.getAmplifier();
                    if (amplifier >= 0){
                        event.setNewSpeed((float) (event.getOriginalSpeed() / (Math.pow(0.3, Math.min(amplifier + 1, 4)))));
                    }
                }
                if (!living.isOnGround() || living.isInWater()) { //未站立状态,在水中 为原本20%
                    event.setNewSpeed(event.getOriginalSpeed() * 5);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGetHurt(LivingHurtEvent event) { //不能伤害无尽套玩家
        if (!(event.getEntityLiving() instanceof Player player)) {
            return;
        }
        if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
    }

    /**
     * 是否持有无尽武器
     * @param player 玩家
     * @return 是
     */
    public static boolean isInfinityItem(Player player){
        ItemStack stack = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
        return !stack.isEmpty() && (stack.getItem() == EndlessItems.infinitySword.get() || stack.getItem() == EndlessItems.infinityBow.get()
                || stack.getItem() == EndlessItems.infinityCrossBow.get());
    }

    //玩家不会被无尽伤害外攻击
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttacked(LivingAttackEvent event) { //实体受到攻击事件
        if (!(event.getEntityLiving() instanceof Player player)) { //受伤实体不是玩家
            return;
        }
        if (event.getSource().getDirectEntity() instanceof Player) {
            return; //造成伤害的实体是玩家
        }
        if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
        String key = player.getGameProfile().getName() + ":" + player.level.isClientSide;
        if (event.getSource().isFire() && playersWithLegs.contains(key)){
            event.setCanceled(true);
        }
        if (event.getSource().isMagic() && playersWithChest.contains(key)){
            event.setCanceled(true);
        }
    }

    /**
     * 判断玩家是否穿戴全套无尽装备
     * @param player 玩家
     * @return 结果
     */
    public static boolean isInfinite(LivingEntity player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                continue;
            }
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof InfinityArmor)) {
                return false;
            }
        }
        return true;
    }
    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getPlayer();
        //首次登录时发送消息
        if (!player.getPersistentData().getBoolean("endless:login")){
            player.getPersistentData().putBoolean("endless:login", true);
            player.sendMessage(new TranslatableComponent("endless.message.login")
                    .setStyle(Style.EMPTY.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(new TranslatableComponent("endless.message.login0")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
        }
        //配置文件内容错误消息
        if (!Config.errorInfo.isEmpty()){
            player.sendMessage(new TextComponent("The following errors were found in the configuration file:\n"
                    + StringUtils.join(Config.errorInfo.toArray(), ",")).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), UUID.randomUUID());
        }
    }

    //恒星燃料
    @SubscribeEvent
    public static void starFuel(FurnaceFuelBurnTimeEvent event){
        Item item = event.getItemStack().getItem();
        if (item == EndlessItems.starFuel.get()){
            event.setBurnTime(Integer.MAX_VALUE);
        }
    }

    //物质团合并
    @SubscribeEvent
    public static void matterClusterAdd(PlayerEvent.ItemPickupEvent event){
        Player player = event.getPlayer();
        ItemStack stack = event.getStack();
        if (player != null && stack.getItem() == EndlessItems.matterCluster.get() && Config.SERVER.isMergeMatterCluster.get()){
            int slot = player.getInventory().findSlotMatchingItem(stack);
            if (MatterCluster.mergeMatterCluster(stack, player, slot)){
                player.getInventory().removeItemNoUpdate(slot);
            }
        }
    }

    //无尽镐 潜行左键删除方块
    @SubscribeEvent
    public static void removeBlock(PlayerInteractEvent.LeftClickBlock event){
        Player player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof InfinityPickaxe && !stack.getOrCreateTag().getBoolean("hammer") && !player.isCreative()){
            BlockPos pos = event.getPos();
            Level world = event.getWorld();
            BlockState state = world.getBlockState(pos);
            if (state.getDestroySpeed(world, pos) < 0 && player.isCrouching() && Config.SERVER.isRemoveBlock.get()){
                Item item = Item.byBlock(state.getBlock());
                if (item != Items.AIR){
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ()
                    , new ItemStack(item)));
                }
                world.destroyBlock(pos, true);
            }
        }
    }

    /**
     * 物品是否属于无尽物品
     * @param item 物品
     * @return 是 true
     */
    public static boolean isInfinityItem(Item item){
        return item instanceof InfinityAxe || item instanceof InfinityBow || item instanceof InfinityCrossBow
                || item instanceof InfinityArrow || item instanceof InfinityHoe || item instanceof InfinityPickaxe ||
                item instanceof InfinityShovel || item instanceof InfinitySword || item instanceof InfinityArmor;
    }

    /**
     * 添加额外掉落
     * @param item 需要掉落的物品
     * @param count 数量
     * @param world 世界
     * @param pos 坐标
     * @param event 事件
     */
    private static void spawnDrops(Item item, int count, Level world, BlockPos pos, LivingDropsEvent event){
        ItemStack stack1 = new ItemStack(item, count);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
        event.getDrops().add(itemEntity);
    }

    /**
     * 获取玩家背包中的图腾
     * @param player 玩家
     * @return 图腾
     */
    private static ItemStack getPlayerBagItem(Player player){
        if (Endless.isCurios){
            final ItemStack[] stack = new ItemStack[1];
            LazyOptional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
            curiosHandler.ifPresent(handler -> {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();
                for (String s : curios.keySet()) {
                    ICurioStacksHandler stacksHandler = curios.get(s);
                    if ("body".equals(s)){ //只查找胸饰栏
                        IDynamicStackHandler stacks = stacksHandler.getStacks();
                        int slots = stacks.getSlots();
                        for (int i = 0; i < slots; i++){
                            ItemStack itemStack = stacks.getStackInSlot(i);
                            if (itemStack.getItem() instanceof InfinityTotem){
                                stack[0] = itemStack;
                                break;
                            }
                        }
                    }
                }
            });
            if (stack[0] != null){
                return stack[0].isEmpty() ? ItemStack.EMPTY : stack[0];
            }
        }

        ItemStack mainhand = player.getMainHandItem();
        if (mainhand.getItem() == EndlessItems.infinityTotem.get()){
            return mainhand;
        }
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() == EndlessItems.infinityTotem.get()){
            return offhand;
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == EndlessItems.infinityTotem.get())
                return stack;
        }

        return ItemStack.EMPTY;
    }

    private static void attackAOE(Player player, float range, float damage) {
        AABB aabb = player.getBoundingBox().inflate(range);//范围
        List<Entity> toAttack = player.getLevel().getEntities(player, aabb);//生物列表
        DamageSource src = DamageSource.playerAttack(player);//伤害类型
        for (Entity entity : toAttack) { //循环遍历
            if (entity instanceof LivingEntity){
                if (entity instanceof Mob) {
                    InfinitySword.attackEntity(entity, src, damage);
                }
            }
        }
    }
}

