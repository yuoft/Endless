package com.yuo.endless.Entity;

import com.yuo.endless.Config;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;

public class InfinityMobEntity extends Zombie {

    private static final Random rand = new Random();
    private final ServerBossEvent bossInfo = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossBarColor.RED, BossBarOverlay.PROGRESS)).setDarkenScreen(true);


    public InfinityMobEntity(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
        this.setPersistenceRequired();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(4, new MobGoal(this));
        this.goalSelector.addGoal(9, new MobTPGoal(this));
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Monster.class)); //被攻击后呼叫所有怪物帮助
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Animal.class, false)); //与所有生物敌对
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false)); //与所有生物敌对
    }

    //属性
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 16.0D)
                .add(Attributes.MAX_HEALTH, 1024.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ARMOR, 2.0d);
    }

    @Override
    public void setPersistenceRequired() {
        super.setPersistenceRequired();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB))
            super.makeStuckInBlock(pState, pMotionMultiplier);
    }

    //被攻击时
    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        //攻击者为玩家且是无尽伤害
        if (InfinityDamageSource.isInfinity(source) && entity instanceof Player) {
            amount *= 0.1f;
        } else { //伤害最高10点
            amount *= 0.01f;
            amount = amount > 10 ? 10 : amount;
            amount = Math.max(amount, 0.5f); //最低0.5
        }
        super.actuallyHurt(source, amount);
    }

    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        if (Config.SERVER.mobHpInfo.get())
         this.bossInfo.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        if (Config.SERVER.mobHpInfo.get())
            this.bossInfo.removePlayer(pPlayer);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return super.doHurtTarget(pEntity);
    }

    //是否可以装备物品
    @Override
    public boolean equipItemIfPossible(ItemStack pStack) {
        return true;
    }

    //生成


    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType type, @org.jetbrains.annotations.Nullable SpawnGroupData groupData, @org.jetbrains.annotations.Nullable CompoundTag tag) {
        SpawnGroupData pSpawnData = super.finalizeSpawn(level, difficulty, type, groupData, tag);
        float f = difficulty.getSpecialMultiplier();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * f);
        if (pSpawnData == null) {
            pSpawnData = new ZombieGroupData(getSpawnAsBabyOdds(level.getRandom()), true);
        }

        if (pSpawnData instanceof ZombieGroupData zombie$zombiegroupdata) {
            if (zombie$zombiegroupdata.isBaby) {
                this.setBaby(true);
                if (zombie$zombiegroupdata.canSpawnJockey) {
                    if ((double)level.getRandom().nextFloat() < 0.05) {
                        List<Chicken> list = level.getEntitiesOfClass(Chicken.class, this.getBoundingBox().inflate(5.0, 3.0, 5.0), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
                        if (!list.isEmpty()) {
                            Chicken chicken = list.get(0);
                            chicken.setChickenJockey(true);
                            this.startRiding(chicken);
                        }
                    } else if ((double)level.getRandom().nextFloat() < 0.05) {
                        Chicken chicken1 = EntityType.CHICKEN.create(this.level);
                        chicken1.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        chicken1.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null, null);
                        chicken1.setChickenJockey(true);
                        this.startRiding(chicken1);
                        level.addFreshEntity(chicken1);
                    }
                }
            }

            this.setCanBreakDoors(this.supportsBreakDoorGoal() && this.random.nextFloat() < f * 0.3F);//破门概率
            this.populateDefaultEquipmentSlots(difficulty); //装备
            this.populateDefaultEquipmentEnchantments(difficulty); //附魔
        }

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        this.handleAttributes(f);
        return pSpawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        //添加装备
        if (rand.nextFloat() < 0.25F * (difficulty.getDifficulty() == Difficulty.HARD ? 2 : 1)) {
            int i = rand.nextInt(2);
            float f = this.level.getDifficulty() == Difficulty.HARD ? 0.05F : 0.15F;
            if (rand.nextFloat() < 0.105F) {
                ++i;
            }
            if (rand.nextFloat() < 0.085F) {
                ++i;
            }
            if (rand.nextFloat() < 0.075F) {
                ++i;
            }
            boolean flag = true;

            for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
                if (equipmentslottype.getType() == Type.ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(equipmentslottype);
                    if (!flag && rand.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.isEmpty()) {
                        Item item = getArmorByChance(equipmentslottype, i); //装备物品获取
                        if (item != null) {
                            this.setItemSlot(equipmentslottype, new ItemStack(item));
                        }
                    }
                }
            }
        }
        //添加武器
        if (rand.nextFloat() < (this.level.getDifficulty() == Difficulty.HARD ? 0.7F : 0.3F)) {
            int i = rand.nextInt(10);
            if (i == 0) {
                if (rand.nextFloat() < 0.0001f)
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EndlessItems.infinitySword.get()));
                else
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EndlessItems.crystalMatrixSword.get()));
            } else if (i < 3) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else if (i < 6) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            } else if (i < 8) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
                this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            }
        }
    }

    @Nullable
    public static Item getArmorByChance(EquipmentSlot slotIn, int chance) {
        switch (slotIn) {
            case HEAD:
                if (chance == 0) {
                    return Items.IRON_HELMET;
                } else if (chance == 1) {
                    return Items.DIAMOND_HELMET;
                } else if (chance == 2) {
                    return EndlessItems.crystalMatrixHead.get();
                } else if (chance == 3) {
                    return EndlessItems.neutroniumHead.get();
                } else if (chance == 4  && rand.nextFloat() < 0.001f) {
                    return EndlessItems.infinityHead.get();
                }
            case CHEST:
                if (chance == 2) {
                    return EndlessItems.crystalMatrixChest.get();
                } else if (chance == 3) {
                    return EndlessItems.neutroniumChest.get();
                } else if (chance == 4 && rand.nextFloat() < 0.001f) {
                    return EndlessItems.infinityChest.get();
                } else if (chance == 0) {
                    return Items.IRON_CHESTPLATE;
                } else if (chance == 1) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (chance == 2) {
                    return EndlessItems.crystalMatrixLegs.get();
                } else if (chance == 3) {
                    return EndlessItems.neutroniumLegs.get();
                } else if (chance == 4 && rand.nextFloat() < 0.001f) {
                    return EndlessItems.infinityLegs.get();
                } else if (chance == 0) {
                    return Items.IRON_LEGGINGS;
                } else if (chance == 1) {
                    return Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (chance == 2) {
                    return EndlessItems.crystalMatrixFeet.get();
                } else if (chance == 3) {
                    return EndlessItems.neutroniumFeet.get();
                } else if (chance == 4 && rand.nextFloat() < 0.001f) {
                    return EndlessItems.infinityFeet.get();
                } else if (chance == 0) {
                    return Items.IRON_BOOTS;
                } else if (chance == 1) {
                    return Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 0));
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    //淹死
    //变溺尸
    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean isUnderWaterConverting() {
        return false;
    }

    @Override
    public boolean canBreakDoors() {
        return true;
    }

    //可以在水下呼吸
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    //经验值
    @Override
    protected int getExperienceReward(Player pPlayer) {
        this.xpReward *= 10;
        return super.getExperienceReward(pPlayer);
    }

    //盔甲掉落概率
    @Override
    protected float getEquipmentDropChance(EquipmentSlot pSlot) {
        return 0.085f;
    }

    //掉落物
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(source, looting, pRecentlyHit);
        if (source.getDirectEntity() instanceof Player && pRecentlyHit){
            ItemStack stack = new ItemStack(Items.DIAMOND, rand.nextInt( 8, 16 + looting * 3));
            ItemStack stack0 = new ItemStack(Items.EMERALD, rand.nextInt(  4 + looting, 32 + looting * 2));
            ItemStack stack1 = new ItemStack(Items.GOLD_INGOT, rand.nextInt(  6 + looting, 20 + looting * 2));
            ItemStack stack2 = new ItemStack(Items.DIAMOND_BLOCK, rand.nextInt( 1, 3 + looting));
            ItemStack stack3 = new ItemStack(Items.EMERALD_BLOCK, rand.nextInt( 2, 4 + looting));
            ItemStack stack4 = new ItemStack(Items.GOLD_BLOCK, rand.nextInt(  3, 5 + looting));
            ItemStack stack5 = new ItemStack(EndlessItems.diamondLattice.get(), rand.nextInt(  1, 3 + looting));
            ItemStack stack6 = new ItemStack(EndlessItems.neutroniumNugget.get(), rand.nextInt(  1, 5 + looting));
            ItemStack stack7 = new ItemStack(EndlessItems.infinityCatalyst.get(), 1);
            this.spawnAtLocation(stack);
            this.spawnAtLocation(stack0);
            this.spawnAtLocation(stack1);
            this.spawnAtLocation(stack2);
            this.spawnAtLocation(stack3);
            this.spawnAtLocation(stack4);
            if (looting > 0){
                if (rand.nextFloat() < 0.1f)
                    this.spawnAtLocation(stack5);
                if (rand.nextFloat() < 0.05f)
                    this.spawnAtLocation(stack6);
                if (rand.nextFloat() < 0.001f)
                    this.spawnAtLocation(stack7);
            }
        }
    }
}
