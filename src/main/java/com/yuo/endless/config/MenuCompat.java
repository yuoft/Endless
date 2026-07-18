package com.yuo.endless.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class MenuCompat {
    public MenuCompat() {

    }

    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(Component.literal("Endless"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = root.entryBuilder();
        maidConfig(root, entryBuilder);
        return root;
    }

    private static void maidConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory base = root.getOrCreateCategory(Component.translatable("config.endless.comment.base"));
//        ConfigCategory sNumber = root.getOrCreateCategory(Component.translatable("config.endless.comment.singularity_number"));
        ConfigCategory blackList = root.getOrCreateCategory(Component.translatable("config.endless.comment.black_list"));
        ConfigCategory sCustom = root.getOrCreateCategory(Component.translatable("config.endless.comment.singularity_custom"));

        BooleanToggleBuilder isKeepStone = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isKeepStone"), ModConfig.SERVER.isKeepStone.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.isKeepStone.desc"));
        base.addEntry(isKeepStone.setSaveConsumer(ModConfig.SERVER.isKeepStone::set).build());
        BooleanToggleBuilder isBreakBedrock = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isBreakBedrock"), ModConfig.SERVER.isBreakBedrock.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.isBreakBedrock.desc"));
        base.addEntry(isBreakBedrock.setSaveConsumer(ModConfig.SERVER.isBreakBedrock::set).build());
        BooleanToggleBuilder isMergeMatterCluster = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isMergeMatterCluster"), ModConfig.SERVER.isMergeMatterCluster.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.isMergeMatterCluster.desc"));
        base.addEntry(isMergeMatterCluster.setSaveConsumer(ModConfig.SERVER.isMergeMatterCluster::set).build());
        BooleanToggleBuilder isSwordAttackAnimal = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isSwordAttackAnimal"), ModConfig.SERVER.isSwordAttackAnimal.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.isSwordAttackAnimal.desc"));
        base.addEntry(isSwordAttackAnimal.setSaveConsumer(ModConfig.SERVER.isSwordAttackAnimal::set).build());
        BooleanToggleBuilder isAxeChangeGrassBlock = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isAxeChangeGrassBlock"), ModConfig.SERVER.isAxeChangeGrassBlock.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.isAxeChangeGrassBlock.desc"));
        base.addEntry(isAxeChangeGrassBlock.setSaveConsumer(ModConfig.SERVER.isAxeChangeGrassBlock::set).build());
        BooleanToggleBuilder isRemoveBlock = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isRemoveBlock"), ModConfig.SERVER.isRemoveBlock.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.isRemoveBlock.desc"));
        base.addEntry(isRemoveBlock.setSaveConsumer(ModConfig.SERVER.isRemoveBlock::set).build());
        BooleanToggleBuilder isBreakDECrystal = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isBreakDECrystal"), ModConfig.SERVER.isBreakDECrystal.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.isBreakDECrystal.desc"));
        base.addEntry(isBreakDECrystal.setSaveConsumer(ModConfig.SERVER.isBreakDECrystal::set).build());
        BooleanToggleBuilder isCraftTable = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isCraftTable"), ModConfig.SERVER.isCraftTable.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.isCraftTable.desc"));
        base.addEntry(isCraftTable.setSaveConsumer(ModConfig.SERVER.isCraftTable::set).build());
        BooleanToggleBuilder isArrowLightning = entryBuilder.startBooleanToggle(Component.translatable("config.endless.isArrowLightning"), ModConfig.SERVER.isArrowLightning.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.isArrowLightning.desc"));
        base.addEntry(isArrowLightning.setSaveConsumer(ModConfig.SERVER.isArrowLightning::set).build());
        BooleanToggleBuilder mobSpawn = entryBuilder.startBooleanToggle(Component.translatable("config.endless.mobSpawn"), ModConfig.SERVER.mobSpawn.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.mobSpawn.desc"));
        base.addEntry(mobSpawn.setSaveConsumer(ModConfig.SERVER.mobSpawn::set).build());
        BooleanToggleBuilder mobHpInfo = entryBuilder.startBooleanToggle(Component.translatable("config.endless.mobHpInfo"), ModConfig.SERVER.mobHpInfo.get()).setDefaultValue(true).setTooltip(Component.translatable("config.endless.mobHpInfo.desc"));
        base.addEntry(mobHpInfo.setSaveConsumer(ModConfig.SERVER.mobHpInfo::set).build());
        BooleanToggleBuilder swordKill = entryBuilder.startBooleanToggle(Component.translatable("config.endless.swordKill"), ModConfig.SERVER.swordKill.get()).setDefaultValue(false).setTooltip(Component.translatable("config.endless.swordKill.desc"));
        base.addEntry(swordKill.setSaveConsumer(ModConfig.SERVER.swordKill::set).build());

        IntFieldBuilder swordRangeDamage = entryBuilder.startIntField(Component.translatable("config.endless.swordRangeDamage"), ModConfig.SERVER.swordRangeDamage.get()).setMin(10).setMax(1000000).setDefaultValue(10000).setTooltip(Component.translatable("config.endless.swordRangeDamage.desc"));
//        base.addEntry(swordRangeDamage.setSaveConsumer((i) -> ModConfig.SERVER.swordRangeDamage.set(i)).build());
        base.addEntry(swordRangeDamage.setSaveConsumer(ModConfig.SERVER.swordRangeDamage::set).build());
        IntSliderBuilder swordAttackRange = entryBuilder.startIntSlider(Component.translatable("config.endless.swordAttackRange"), ModConfig.SERVER.swordAttackRange.get(), 8, 128).setDefaultValue(32).setTooltip(Component.translatable("config.endless.swordAttackRange.desc"));
        base.addEntry(swordAttackRange.setSaveConsumer(ModConfig.SERVER.swordAttackRange::set).build());
        IntFieldBuilder subArrowDamage = entryBuilder.startIntField(Component.translatable("config.endless.subArrowDamage"), ModConfig.SERVER.subArrowDamage.get()).setMin(10).setMax(100000).setDefaultValue(10000).setTooltip(Component.translatable("config.endless.subArrowDamage.desc"));
        base.addEntry(subArrowDamage.setSaveConsumer(ModConfig.SERVER.subArrowDamage::set).build());
        IntSliderBuilder noArrowDamage = entryBuilder.startIntSlider(Component.translatable("config.endless.noArrowDamage"), ModConfig.SERVER.noArrowDamage.get(), 5, 1000).setDefaultValue(10).setTooltip(Component.translatable("config.endless.noArrowDamage.desc"));
        base.addEntry(noArrowDamage.setSaveConsumer(ModConfig.SERVER.noArrowDamage::set).build());
        IntSliderBuilder axeChainDistance = entryBuilder.startIntSlider(Component.translatable("config.endless.axeChainDistance"), ModConfig.SERVER.axeChainDistance.get(), 16, 1024).setDefaultValue(32).setTooltip(Component.translatable("config.endless.axeChainDistance.desc"));
        base.addEntry(axeChainDistance.setSaveConsumer(ModConfig.SERVER.axeChainDistance::set).build());
        IntFieldBuilder matterClusterMaxCount = entryBuilder.startIntField(Component.translatable("config.endless.matterClusterMaxCount"), ModConfig.SERVER.matterClusterMaxCount.get()).setMin(256).setMax(10240).setDefaultValue(2048).setTooltip(Component.translatable("config.endless.matterClusterMaxCount.desc"));
        base.addEntry(matterClusterMaxCount.setSaveConsumer(ModConfig.SERVER.matterClusterMaxCount::set).build());
        IntSliderBuilder matterClusterMaxTerm = entryBuilder.startIntSlider(Component.translatable("config.endless.matterClusterMaxTerm"), ModConfig.SERVER.matterClusterMaxTerm.get(), 8, 128).setDefaultValue(16).setTooltip(Component.translatable("config.endless.matterClusterMaxTerm.desc"));
        base.addEntry(matterClusterMaxTerm.setSaveConsumer(ModConfig.SERVER.matterClusterMaxTerm::set).build());
        IntFieldBuilder endestPearlEndDamage = entryBuilder.startIntField(Component.translatable("config.endless.endestPearlEndDamage"), ModConfig.SERVER.endestPearlEndDamage.get()).setMin(100).setMax(10000).setDefaultValue(1000).setTooltip(Component.translatable("config.endless.endestPearlEndDamage.desc"));
        base.addEntry(endestPearlEndDamage.setSaveConsumer(ModConfig.SERVER.endestPearlEndDamage::set).build());
        IntSliderBuilder endestPearlOneDamage = entryBuilder.startIntSlider(Component.translatable("config.endless.endestPearlOneDamage"), ModConfig.SERVER.endestPearlOneDamage.get(), 1, 100).setDefaultValue(5).setTooltip(Component.translatable("config.endless.endestPearlOneDamage.desc"));
        base.addEntry(endestPearlOneDamage.setSaveConsumer(ModConfig.SERVER.endestPearlOneDamage::set).build());
        IntSliderBuilder endestPearlSuckRange = entryBuilder.startIntSlider(Component.translatable("config.endless.endestPearlSuckRange"), ModConfig.SERVER.endestPearlSuckRange.get(), 10, 64).setDefaultValue(20).setTooltip(Component.translatable("config.endless.endestPearlSuckRange.desc"));
        base.addEntry(endestPearlSuckRange.setSaveConsumer(ModConfig.SERVER.endestPearlSuckRange::set).build());
        IntSliderBuilder infinityArmorBearDamage = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityArmorBearDamage"), ModConfig.SERVER.infinityArmorBearDamage.get(), 4, 100).setDefaultValue(10).setTooltip(Component.translatable("config.endless.infinityArmorBearDamage.desc"));
        base.addEntry(infinityArmorBearDamage.setSaveConsumer(ModConfig.SERVER.infinityArmorBearDamage::set).build());
        IntSliderBuilder infinityBearDamage = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityBearDamage"), ModConfig.SERVER.infinityBearDamage.get(), 1, 50).setDefaultValue(4).setTooltip(Component.translatable("config.endless.infinityBearDamage.desc"));
        base.addEntry(infinityBearDamage.setSaveConsumer(ModConfig.SERVER.infinityBearDamage::set).build());
        IntFieldBuilder infinityFireworkDamage = entryBuilder.startIntField(Component.translatable("config.endless.infinityFireworkDamage"), ModConfig.SERVER.infinityFireworkDamage.get()).setMin(5).setMax(10000).setDefaultValue(100).setTooltip(Component.translatable("config.endless.infinityFireworkDamage.desc"));
        base.addEntry(infinityFireworkDamage.setSaveConsumer(ModConfig.SERVER.infinityFireworkDamage::set).build());
        IntSliderBuilder infinityBucketRange = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityBucketRange"), ModConfig.SERVER.infinityBucketRange.get(), 0, 32).setDefaultValue(7).setTooltip(Component.translatable("config.endless.infinityBucketRange.desc"));
        base.addEntry(infinityBucketRange.setSaveConsumer(ModConfig.SERVER.infinityBucketRange::set).build());
        IntSliderBuilder infinityChestFly = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityChestFly"), ModConfig.SERVER.infinityChestFly.get(), 1, 10).setDefaultValue(3).setTooltip(Component.translatable("config.endless.infinityChestFly.desc"));
        base.addEntry(infinityChestFly.setSaveConsumer(ModConfig.SERVER.infinityChestFly::set).build());
        IntSliderBuilder infinityLegsWalk = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityLegsWalk"), ModConfig.SERVER.infinityLegsWalk.get(), 1, 10).setDefaultValue(3).setTooltip(Component.translatable("config.endless.infinityLegsWalk.desc"));
        base.addEntry(infinityLegsWalk.setSaveConsumer(ModConfig.SERVER.infinityLegsWalk::set).build());
        IntSliderBuilder infinityFeetJump = entryBuilder.startIntSlider(Component.translatable("config.endless.infinityFeetJump"), ModConfig.SERVER.infinityFeetJump.get(), 1, 10).setDefaultValue(3).setTooltip(Component.translatable("config.endless.infinityFeetJump.desc"));
        base.addEntry(infinityFeetJump.setSaveConsumer(ModConfig.SERVER.infinityFeetJump::set).build());
        IntSliderBuilder mobWeigh = entryBuilder.startIntSlider(Component.translatable("config.endless.mobWeigh"), ModConfig.SERVER.mobWeigh.get(), 0, 5).setDefaultValue(1).setTooltip(Component.translatable("config.endless.mobWeigh.desc"));
        base.addEntry(mobWeigh.setSaveConsumer(ModConfig.SERVER.mobWeigh::set).build());
        DoubleFieldBuilder foodTime = entryBuilder.startDoubleField(Component.translatable("config.endless.foodTime"), ModConfig.SERVER.foodTime.get()).setDefaultValue(1d).setMin(0.1d).setMax(5d).setTooltip(Component.translatable("config.endless.foodTime.desc"));
        base.addEntry(foodTime.setSaveConsumer(ModConfig.SERVER.foodTime::set).build());

        /* 奇点数量配置
        IntFieldBuilder singularityCoal = entryBuilder.startIntField(Component.translatable("config.endless.singularityCoal"), ModConfig.SERVER.singularityCoal.get()).setDefaultValue(450).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityCoal.desc"));
        sNumber.addEntry(singularityCoal.setSaveConsumer(ModConfig.SERVER.singularityCoal::set).build());
        IntFieldBuilder singularityClay = entryBuilder.startIntField(Component.translatable("config.endless.singularityClay"), ModConfig.SERVER.singularityClay.get()).setDefaultValue(400).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityClay.desc"));
        sNumber.addEntry(singularityClay.setSaveConsumer(ModConfig.SERVER.singularityClay::set).build());
        IntFieldBuilder singularityIron = entryBuilder.startIntField(Component.translatable("config.endless.singularityIron"), ModConfig.SERVER.singularityIron.get()).setDefaultValue(300).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityIron.desc"));
        sNumber.addEntry(singularityIron.setSaveConsumer(ModConfig.SERVER.singularityIron::set).build());
        IntFieldBuilder singularityGold = entryBuilder.startIntField(Component.translatable("config.endless.singularityGold"), ModConfig.SERVER.singularityGold.get()).setDefaultValue(350).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityGold.desc"));
        sNumber.addEntry(singularityGold.setSaveConsumer(ModConfig.SERVER.singularityGold::set).build());
        IntFieldBuilder singularityDiamond = entryBuilder.startIntField(Component.translatable("config.endless.singularityDiamond"), ModConfig.SERVER.singularityDiamond.get()).setDefaultValue(250).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityDiamond.desc"));
        sNumber.addEntry(singularityDiamond.setSaveConsumer(ModConfig.SERVER.singularityDiamond::set).build());
        IntFieldBuilder singularityEmerald = entryBuilder.startIntField(Component.translatable("config.endless.singularityEmerald"), ModConfig.SERVER.singularityEmerald.get()).setDefaultValue(200).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityEmerald.desc"));
        sNumber.addEntry(singularityEmerald.setSaveConsumer(ModConfig.SERVER.singularityEmerald::set).build());
        IntFieldBuilder singularityNetherite = entryBuilder.startIntField(Component.translatable("config.endless.singularityNetherite"), ModConfig.SERVER.singularityNetherite.get()).setDefaultValue(150).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityNetherite.desc"));
        sNumber.addEntry(singularityNetherite.setSaveConsumer(ModConfig.SERVER.singularityNetherite::set).build());
        IntFieldBuilder singularityLapis = entryBuilder.startIntField(Component.translatable("config.endless.singularityLapis"), ModConfig.SERVER.singularityLapis.get()).setDefaultValue(400).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityLapis.desc"));
        sNumber.addEntry(singularityLapis.setSaveConsumer(ModConfig.SERVER.singularityLapis::set).build());
        IntFieldBuilder singularityQuartz = entryBuilder.startIntField(Component.translatable("config.endless.singularityQuartz"), ModConfig.SERVER.singularityQuartz.get()).setDefaultValue(500).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityQuartz.desc"));
        sNumber.addEntry(singularityQuartz.setSaveConsumer(ModConfig.SERVER.singularityQuartz::set).build());
        IntFieldBuilder singularityRedstone = entryBuilder.startIntField(Component.translatable("config.endless.singularityRedstone"), ModConfig.SERVER.singularityRedstone.get()).setDefaultValue(400).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityRedstone.desc"));
        sNumber.addEntry(singularityRedstone.setSaveConsumer(ModConfig.SERVER.singularityRedstone::set).build());

        if (EndlessUtils.isIAF){
            IntFieldBuilder singularitySilver = entryBuilder.startIntField(Component.translatable("config.endless.singularitySilver"), ModConfig.SERVER.singularitySilver.get()).setDefaultValue(200).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularitySilver.desc"));
            sNumber.addEntry(singularitySilver.setSaveConsumer(ModConfig.SERVER.singularitySilver::set).build());
        }
        IntFieldBuilder singularityCopper = entryBuilder.startIntField(Component.translatable("config.endless.singularityCopper"), ModConfig.SERVER.singularityCopper.get()).setDefaultValue(375).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityCopper.desc"));
        sNumber.addEntry(singularityCopper.setSaveConsumer(ModConfig.SERVER.singularityCopper::set).build());
        if (EndlessUtils.isSpaceArms){
            IntFieldBuilder singularityRuby = entryBuilder.startIntField(Component.translatable("config.endless.singularityRuby"), ModConfig.SERVER.singularityRuby.get()).setDefaultValue(250).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityRuby.desc"));
            sNumber.addEntry(singularityRuby.setSaveConsumer(ModConfig.SERVER.singularityRuby::set).build());
            IntFieldBuilder singularityDragon = entryBuilder.startIntField(Component.translatable("config.endless.singularityDragon"), ModConfig.SERVER.singularityDragon.get()).setDefaultValue(100).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityDragon.desc"));
            sNumber.addEntry(singularityDragon.setSaveConsumer(ModConfig.SERVER.singularityDragon::set).build());
            IntFieldBuilder singularitySpace = entryBuilder.startIntField(Component.translatable("config.endless.singularitySpace"), ModConfig.SERVER.singularitySpace.get()).setDefaultValue(50).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularitySpace.desc"));
            sNumber.addEntry(singularitySpace.setSaveConsumer(ModConfig.SERVER.singularitySpace::set).build());
            IntFieldBuilder singularityXray = entryBuilder.startIntField(Component.translatable("config.endless.singularityXray"), ModConfig.SERVER.singularityXray.get()).setDefaultValue(150).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityXray.desc"));
            sNumber.addEntry(singularityXray.setSaveConsumer(ModConfig.SERVER.singularityXray::set).build());
            IntFieldBuilder singularityUltra = entryBuilder.startIntField(Component.translatable("config.endless.singularityUltra"), ModConfig.SERVER.singularityUltra.get()).setDefaultValue(80).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityUltra.desc"));
            sNumber.addEntry(singularityUltra.setSaveConsumer(ModConfig.SERVER.singularityUltra::set).build());
        }
        if (EndlessUtils.isCreate){
            IntFieldBuilder singularityZinc = entryBuilder.startIntField(Component.translatable("config.endless.singularityZinc"), ModConfig.SERVER.singularityZinc.get()).setDefaultValue(300).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityZinc.desc"));
            sNumber.addEntry(singularityZinc.setSaveConsumer(ModConfig.SERVER.singularityZinc::set).build());
        }
        if (EndlessUtils.isThermal){
            IntFieldBuilder singularityNickel = entryBuilder.startIntField(Component.translatable("config.endless.singularityNickel"), ModConfig.SERVER.singularityNickel.get()).setDefaultValue(400).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityNickel.desc"));
            sNumber.addEntry(singularityNickel.setSaveConsumer(ModConfig.SERVER.singularityNickel::set).build());
            IntFieldBuilder singularityLead = entryBuilder.startIntField(Component.translatable("config.endless.singularityLead"), ModConfig.SERVER.singularityLead.get()).setDefaultValue(300).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityLead.desc"));
            sNumber.addEntry(singularityLead.setSaveConsumer(ModConfig.SERVER.singularityLead::set).build());
            IntFieldBuilder singularityTin = entryBuilder.startIntField(Component.translatable("config.endless.singularityTin"), ModConfig.SERVER.singularityTin.get()).setDefaultValue(400).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityTin.desc"));
            sNumber.addEntry(singularityTin.setSaveConsumer(ModConfig.SERVER.singularityTin::set).build());
        }
        if (EndlessUtils.isDE){
            IntFieldBuilder singularityDragonIum = entryBuilder.startIntField(Component.translatable("config.endless.singularityDragonIum"), ModConfig.SERVER.singularityDragonIum.get()).setDefaultValue(80).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityDragonIum.desc"));
            sNumber.addEntry(singularityDragonIum.setSaveConsumer(ModConfig.SERVER.singularityDragonIum::set).build());
            IntFieldBuilder singularityAwakenDragon = entryBuilder.startIntField(Component.translatable("config.endless.singularityAwakenDragon"), ModConfig.SERVER.singularityAwakenDragon.get()).setDefaultValue(10).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityAwakenDragon.desc"));
            sNumber.addEntry(singularityAwakenDragon.setSaveConsumer(ModConfig.SERVER.singularityAwakenDragon::set).build());
        }
        if (EndlessUtils.isBOT){
            IntFieldBuilder singularityMana = entryBuilder.startIntField(Component.translatable("config.endless.singularityMana"), ModConfig.SERVER.singularityMana.get()).setDefaultValue(200).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityMana.desc"));
            sNumber.addEntry(singularityMana.setSaveConsumer(ModConfig.SERVER.singularityMana::set).build());
            IntFieldBuilder singularityTara = entryBuilder.startIntField(Component.translatable("config.endless.singularityTara"), ModConfig.SERVER.singularityTara.get()).setDefaultValue(100).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityTara.desc"));
            sNumber.addEntry(singularityTara.setSaveConsumer(ModConfig.SERVER.singularityTara::set).build());
            IntFieldBuilder singularityElementIum = entryBuilder.startIntField(Component.translatable("config.endless.singularityElementIum"), ModConfig.SERVER.singularityElementIum.get()).setDefaultValue(50).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityElementIum.desc"));
            sNumber.addEntry(singularityElementIum.setSaveConsumer(ModConfig.SERVER.singularityElementIum::set).build());
        }
        if (EndlessUtils.isPE){
            IntFieldBuilder singularityDarkMatter = entryBuilder.startIntField(Component.translatable("config.endless.singularityDarkMatter"), ModConfig.SERVER.singularityDarkMatter.get()).setDefaultValue(150).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityDarkMatter.desc"));
            sNumber.addEntry(singularityDarkMatter.setSaveConsumer(ModConfig.SERVER.singularityDarkMatter::set).build());
            IntFieldBuilder singularityRedMatter = entryBuilder.startIntField(Component.translatable("config.endless.singularityRedMatter"), ModConfig.SERVER.singularityRedMatter.get()).setDefaultValue(1100).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityRedMatter.desc"));
            sNumber.addEntry(singularityRedMatter.setSaveConsumer(ModConfig.SERVER.singularityRedMatter::set).build());
        }
        if (EndlessUtils.isTC3){
            IntFieldBuilder singularityCobalt = entryBuilder.startIntField(Component.translatable("config.endless.singularityCobalt"), ModConfig.SERVER.singularityCobalt.get()).setDefaultValue(150).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityCobalt.desc"));
            sNumber.addEntry(singularityCobalt.setSaveConsumer(ModConfig.SERVER.singularityCobalt::set).build());
            IntFieldBuilder singularityManyullyn = entryBuilder.startIntField(Component.translatable("config.endless.singularityManyullyn"), ModConfig.SERVER.singularityManyullyn.get()).setDefaultValue(100).setMin(10).setMax(100000).setTooltip(Component.translatable("config.endless.singularityManyullyn.desc"));
            sNumber.addEntry(singularityManyullyn.setSaveConsumer(ModConfig.SERVER.singularityManyullyn::set).build());
        }

        IntSliderBuilder modRatioRate = entryBuilder.startIntSlider(Component.translatable("config.endless.modRatioRate"), ModConfig.SERVER.modRatioRate.get(), 5, 100).setDefaultValue(10000).setTooltip(Component.translatable("config.endless.modRatioRate.desc"));
        sNumber.addEntry(modRatioRate.setSaveConsumer(ModConfig.SERVER.modRatioRate::set).build());
        IntFieldBuilder modRatioCount = entryBuilder.startIntField(Component.translatable("config.endless.modRatioCount"), ModConfig.SERVER.modRatioCount.get()).setDefaultValue(2000).setMin(500).setMax(5000).setTooltip(Component.translatable("config.endless.modRatioCount.desc"));
        sNumber.addEntry(modRatioCount.setSaveConsumer(ModConfig.SERVER.modRatioCount::set).build());
        */

        StringListBuilder pickaxeBlackList = entryBuilder.startStrList(Component.translatable("config.endless.pickaxeBlackList"), ModConfig.SERVER.pickaxeBlackList.get()).setDefaultValue(ModConfig.SERVER.pickaxeBlackList.getDefault()).setTooltip(Component.translatable("config.endless.pickaxeBlackList.desc"));
        blackList.addEntry(pickaxeBlackList.setSaveConsumer((s) -> {
            ModConfig.SERVER.pickaxeBlackList.set(s);
            ModConfig.loadConfig();
        }).build());
        StringListBuilder axeBlackList = entryBuilder.startStrList(Component.translatable("config.endless.axeBlackList"), ModConfig.SERVER.axeBlackList.get()).setDefaultValue(ModConfig.SERVER.axeBlackList.getDefault()).setTooltip(Component.translatable("config.endless.axeBlackList.desc"));
        blackList.addEntry(axeBlackList.setSaveConsumer((s) -> {
            ModConfig.SERVER.axeBlackList.set(s);
            ModConfig.loadConfig();
        }).build());
        StringListBuilder shovelBlackList = entryBuilder.startStrList(Component.translatable("config.endless.shovelBlackList"), ModConfig.SERVER.shovelBlackList.get()).setDefaultValue(ModConfig.SERVER.shovelBlackList.getDefault()).setTooltip(Component.translatable("config.endless.shovelBlackList.desc"));
        blackList.addEntry(shovelBlackList.setSaveConsumer((s) -> {
            ModConfig.SERVER.shovelBlackList.set(s);
            ModConfig.loadConfig();
        }).build());
        StringListBuilder hoeBlackList = entryBuilder.startStrList(Component.translatable("config.endless.hoeBlackList"), ModConfig.SERVER.hoeBlackList.get()).setDefaultValue(ModConfig.SERVER.hoeBlackList.getDefault()).setTooltip(Component.translatable("config.endless.hoeBlackList.desc"));
        blackList.addEntry(hoeBlackList.setSaveConsumer((s) -> {
            ModConfig.SERVER.hoeBlackList.set(s);
            ModConfig.loadConfig();
        }).build());
        StringListBuilder singularityCustomList = entryBuilder.startStrList(Component.translatable("config.endless.singularityCustomList"), ModConfig.SERVER.singularityCustomList.get()).setDefaultValue(ModConfig.SERVER.singularityCustomList.getDefault()).setTooltip(Component.translatable("config.endless.singularityCustomList.desc"));
        sCustom.addEntry(singularityCustomList.setSaveConsumer((s) -> {
            ModConfig.SERVER.singularityCustomList.set(s);
            ModConfig.loadConfig();
        }).build());

    }

    /**
     * 注册可视化配置
     */
    @SuppressWarnings("removal")
    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigBuilder().setParentScreen(parent).build()));
    }
}
