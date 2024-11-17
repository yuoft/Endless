package com.yuo.endless.Event;

import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.Armor.InfinityArmor;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.*;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.NetWork.TotemPacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
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


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPre(GuiScreenEvent.DrawScreenEvent.Pre e) {
        AvaritiaShaders.inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPost(GuiScreenEvent.DrawScreenEvent.Post e) {
        AvaritiaShaders.inventoryRender = false;
    }

    //无尽鞋子 无摔落伤害
    @SubscribeEvent
    public static void playerFall(LivingFallEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)living;
            String key = player.getGameProfile().getName()+":"+player.world.isRemote;
            if (playersWithFeet.contains(key)) {
                event.setCanceled(true);
            }
        }
    }

    //无尽装备 不受伤害
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void opArmsImmuneDamage(LivingDamageEvent event){
        LivingEntity living = event.getEntityLiving();
        Boolean hasChest = living.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == EndlessItems.infinityChest.get();
        Boolean hasLeg = living.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == EndlessItems.infinityLegs.get();
        Boolean hasHead = living.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == EndlessItems.infinityHead.get();
        Boolean hasFeet = living.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == EndlessItems.infinityFeet.get();
        float amount = event.getAmount();
        if (living instanceof PlayerEntity){ //怪物无法触发全部伤害减免
            PlayerEntity player = (PlayerEntity) living;
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
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            ItemStack legs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
            ItemStack feet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            boolean hasHead = player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == EndlessItems.infinityHead.get();
            boolean hasChest = chest.getItem() == EndlessItems.infinityChest.get();
            boolean hasLegs = legs.getItem() == EndlessItems.infinityLegs.get();
            boolean hasFeet = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == EndlessItems.infinityFeet.get();
            //防止其它模组飞行装备无法使用
            String key = player.getGameProfile().getName()+":"+player.world.isRemote;
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
                    player.abilities.allowFlying = true;
                    if (chest.getOrCreateTag().getBoolean("flag") && player.world.isRemote){
                        player.abilities.setFlySpeed(0.05f + 0.05f * Config.SERVER.infinityChestFly.get());
                    }
                }else {
                    if (!player.isCreative()) {
                        player.abilities.allowFlying = false;
                        player.abilities.isFlying = false;
                    }
                    if (player.world.isRemote)
                        player.abilities.setFlySpeed(0.05f);
                    playersWithChest.remove(key);
                }
            }else if (hasChest) {
                playersWithChest.add(key);
            }
            //legs
            if (playersWithLegs.contains(key)) {
                ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (hasLegs) {
                    if (legs.getOrCreateTag().getBoolean("flag") && attribute != null && !attribute.hasModifier(InfinityArmor.modifierWalk))
                        attribute.applyPersistentModifier(InfinityArmor.modifierWalk); //行走速度
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
                    if (feet.getOrCreateTag().getBoolean("flag") && !player.isSneaking()){
                        player.stepHeight = 1.25f; //上坡高度
                    }
                }else {
                    player.stepHeight = 0.6f;
                    playersWithFeet.remove(key);
                }
            }else if (hasFeet){
                playersWithFeet.add(key);
            }

            boolean hasSword = player.getActiveItemStack().getItem() == EndlessItems.infinitySword.get();
            if (hasSword){
                InfinitySword.clearBuff(player);
            }
        }
    }

    //无尽鞋子 增加跳跃高度
    @SubscribeEvent
    public static void jumpBoost(LivingEvent.LivingJumpEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)living;
            String key = player.getGameProfile().getName()+":"+player.world.isRemote;
            ItemStack feet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            if (playersWithFeet.contains(key) && feet.hasTag() && feet.getOrCreateTag().getBoolean("flag")) {
                player.setMotion(0, 0.42f + 0.1f * (Config.SERVER.infinityFeetJump.get() + 1), 0);
            }
        }
    }

    //玩家合成无尽装备时添加附魔
    @SubscribeEvent
    public static void opTool(PlayerEvent.ItemCraftedEvent event){
        ItemStack stack = event.getCrafting();
        if (stack.getItem().equals(EndlessItems.infinitySword.get())){
            Map<Enchantment, Integer> map = new HashMap<>();
            map.put(Enchantments.LOOTING, 10);
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
        Entity source = event.getSource().getTrueSource();
        if (!(source instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) source;

        if (entityLiving instanceof SkeletonEntity){ //炽焰之啄颅剑击杀小白掉落调零骷髅头
            ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
            if (heldItem.getItem() instanceof SkullfireSword){
                spawnDrops(Items.WITHER_SKELETON_SKULL, 1, entityLiving.world, entityLiving.getPosition(), event);
            }
        }
    }

    //无尽镐 锤形态右键获取基岩
    @SubscribeEvent
    public static void breakBedrock(PlayerInteractEvent.RightClickBlock event){
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        if (stack.getItem() instanceof InfinityPickaxe && world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)){
            if (Config.SERVER.isBreakBedrock.get() && stack.getOrCreateTag().getBoolean("hammer")){
                world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(Blocks.BEDROCK)));
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(LivingDeathEvent event) { //不能被无尽伤害外的攻击杀死
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
                player.setHealth(player.getMaxHealth());
                event.setCanceled(true);
            }
            ItemStack totem = getPlayerBagItem(player);
            if (!totem.isEmpty()){
                NetWorkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TotemPacket(totem, player));

                player.clearActivePotions();
                int damage = totem.getDamage();
                if (damage == 9){ //最后一次
                    player.setHealth(player.getMaxHealth());
                    player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 800, 1));
                    player.addPotionEffect(new EffectInstance(Effects.SPEED, 800, 1));
                    attackAOE(player, 8, 1000.0f);
                    player.sendMessage(new TranslationTextComponent("endless.text.msg.totem_break"), UUID.randomUUID());
                }else {
                    player.setHealth(10.0F);
                }
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 2600, 4));
                player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 400, 1));
                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 700, 2));
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1100, 0));
                totem.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
                event.setCanceled(true);
            }
        }
    }

    //在不利因素下，将挖掘速度恢复为正常速度
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void dignity(PlayerEvent.BreakSpeed event) {
        if (!event.getEntityLiving().getActiveItemStack().isEmpty()) {
            LivingEntity living = event.getEntityLiving();
            ItemStack held = living.getActiveItemStack();
            if (held.getItem() == EndlessItems.infinityPickaxe.get() || held.getItem() == EndlessItems.infinityShovel.get() ||
            held.getItem() == EndlessItems.infinityAxe.get() || held.getItem() == EndlessItems.infinityHoe.get()) {
                EffectInstance effect = living.getActivePotionEffect(Effects.MINING_FATIGUE); //挖掘疲劳 每级将至原本30%
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
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
    }

    /**
     * 是否持有无尽武器
     * @param player 玩家
     * @return 是
     */
    public static boolean isInfinityItem(PlayerEntity player){
        ItemStack stack = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
        return !stack.isEmpty() && (stack.getItem() == EndlessItems.infinitySword.get() || stack.getItem() == EndlessItems.infinityBow.get()
                || stack.getItem() == EndlessItems.infinityCrossBow.get());
    }

    //玩家不会被无尽伤害外攻击
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttacked(LivingAttackEvent event) { //实体受到攻击事件
        if (!(event.getEntityLiving() instanceof PlayerEntity)) { //受伤实体不是玩家
            return;
        }
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            return; //造成伤害的实体是玩家
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (isInfinite(player) && !InfinityDamageSource.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
        String key = player.getGameProfile().getName()+":"+player.world.isRemote;
        if (event.getSource().isFireDamage() && playersWithLegs.contains(key)){
            event.setCanceled(true);
        }
        if (event.getSource().isMagicDamage() && playersWithChest.contains(key)){
            event.setCanceled(true);
        }
    }

    /**
     * 判断玩家是否穿戴全套无尽装备
     * @param player 玩家
     * @return 结果
     */
    public static boolean isInfinite(PlayerEntity player) {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getSlotType() != EquipmentSlotType.Group.ARMOR) {
                continue;
            }
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof InfinityArmor)) {
                return false;
            }
        }
        return true;
    }
    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        //重置双爆属性
        PlayerEntity player = event.getPlayer();
        //首次登录时发送消息
        if (!player.getPersistentData().getBoolean("endless:login")){
            player.getPersistentData().putBoolean("endless:login", true);
            player.sendMessage(new TranslationTextComponent("endless.message.login")
                    .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("endless.message.login0")))
                            .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
        }
        //配置文件内容错误消息
        if (!Config.errorInfo.isEmpty()){
            player.sendMessage(new StringTextComponent("The following errors were found in the configuration file:\n"
                    + StringUtils.join(Config.errorInfo.toArray(), ",")).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))), UUID.randomUUID());
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
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getStack();
        if (player != null && stack.getItem() == EndlessItems.matterCluster.get() && Config.SERVER.isMergeMatterCluster.get()){
            int slot = player.inventory.getSlotFor(stack);
            if (MatterCluster.mergeMatterCluster(stack, player, slot)){
                player.inventory.removeStackFromSlot(slot);
            }
        }
    }

    //无尽镐 潜行左键删除方块
    @SubscribeEvent
    public static void removeBlock(PlayerInteractEvent.LeftClickBlock event){
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof InfinityPickaxe && !stack.getOrCreateTag().getBoolean("hammer") && !player.isCreative()){
            BlockPos pos = event.getPos();
            World world = event.getWorld();
            BlockState state = world.getBlockState(pos);
            if (state.getBlockHardness(world, pos) < 0 && player.isSneaking() && Config.SERVER.isRemoveBlock.get()){
                Item item = Item.getItemFromBlock(state.getBlock());
                if (item != Items.AIR){
                    world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ()
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
    private static void spawnDrops(Item item, int count, World world, BlockPos pos, LivingDropsEvent event){
        ItemStack stack1 = new ItemStack(item, count);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
        event.getDrops().add(itemEntity);
    }

    /**
     * 获取玩家背包中的图腾
     * @param player 玩家
     * @return 图腾
     */
    private static ItemStack getPlayerBagItem(PlayerEntity player){
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

        ItemStack mainhand = player.getHeldItemMainhand();
        if (mainhand.getItem() == EndlessItems.infinityTotem.get()){
            return mainhand;
        }
        ItemStack offhand = player.getHeldItemOffhand();
        if (offhand.getItem() == EndlessItems.infinityTotem.get()){
            return offhand;
        }
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() == EndlessItems.infinityTotem.get())
                return stack;
        }

        return ItemStack.EMPTY;
    }

    private static void attackAOE(PlayerEntity player,float range, float damage) {
        AxisAlignedBB aabb = player.getBoundingBox().grow(range);//范围
        List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, aabb);//生物列表
        DamageSource src = DamageSource.causePlayerDamage(player);//伤害类型
        for (Entity entity : toAttack) { //循环遍历
            if (entity instanceof LivingEntity){
                if (entity instanceof IMob) {
                    InfinitySword.attackEntity(entity, src, damage);
                }
            }
        }
    }
}

