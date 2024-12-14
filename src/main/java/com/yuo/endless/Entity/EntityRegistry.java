package com.yuo.endless.Entity;

import com.yuo.endless.Endless;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Endless.MOD_ID);

    //箭
    public static final RegistryObject<EntityType<InfinityArrowEntity>> INFINITY_ARROW = ENTITY_TYPES.register("infinity_arrow",
            () -> EntityType.Builder.<InfinityArrowEntity>of(InfinityArrowEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5F).build("infinity_arrow"));
    public static final RegistryObject<EntityType<InfinityArrowSubEntity>> INFINITY_ARROW_SUB = ENTITY_TYPES.register("infinity_arrow_sub",
            () -> EntityType.Builder.<InfinityArrowSubEntity>of(InfinityArrowSubEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5F).build("infinity_arrow_sub"));
    public static final RegistryObject<EntityType<EndestPearlEntity>> ENDEST_PEARL = ENTITY_TYPES.register("endest_pearl",
            () -> EntityType.Builder.<EndestPearlEntity>of(EndestPearlEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5F).build("endest_pearl"));
    public static final RegistryObject<EntityType<GapingVoidEntity>> GAPING_VOID = ENTITY_TYPES.register("gaping_void",
            () -> EntityType.Builder.<GapingVoidEntity>of(GapingVoidEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5F).build("gaping_void"));
    public static final RegistryObject<EntityType<InfinityFireWorkEntity>> INFINITY_FIREWORK = ENTITY_TYPES.register("infinity_firework",
            () -> EntityType.Builder.<InfinityFireWorkEntity>of(InfinityFireWorkEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5F).build("infinity_firework"));

    public static final RegistryObject<EntityType<InfinityMobEntity>> INFINITY_MOB = ENTITY_TYPES.register("infinity_mob",
            () -> EntityType.Builder.of(InfinityMobEntity::new, MobCategory.MONSTER)
            //防火，碰撞大小（宽，高），跟踪范围，更新时间，免疫方块
            .fireImmune().sized(0.6F, 1.95F).setTrackingRange(16).updateInterval(2).immuneTo(Blocks.WITHER_ROSE,
                            Blocks.SWEET_BERRY_BUSH, Blocks.SOUL_SAND, Blocks.COBWEB).build("infinity_mob"));
}
