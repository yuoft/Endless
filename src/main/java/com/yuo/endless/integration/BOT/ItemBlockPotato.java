package com.yuo.endless.integration.BOT;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityInject;
import vazkii.botania.common.core.handler.ContributorList;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.Locale;
import java.util.regex.Pattern;

public class ItemBlockPotato extends BlockItem {
    private static final Pattern TYPOS = Pattern.compile(
            "(?!^vazkii$)" // Do not match the properly spelled version
                    + "^v[ao]{1,2}[sz]{0,2}[ak]{1,2}(i){1,2}l{0,2}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final String[] NOT_MY_NAME = {
            "Six letter word just to get me along",
            "It's a intricacy and I'm coding on my mod and I,",
            "I keep fixin', and keepin' it together",
            "People around gotta find something to play now",
            "Holding back, every mod's the same",
            "Don't wanna be a loser",
            "Listen to me, oh no, I don't break anything at all",
            "But with nothing to consider they forget my name",
            "'ame, 'ame, 'ame",
            "They call me Vaskii",
            "They call me Vazki",
            "They call me Voskii",
            "They call me Vazkki",
            "That's not my name",
            "That's not my name",
            "That's not my name",
            "That's not my name"
    };

    private static final String TAG_TICKS = "notMyNameTicks";
    public ItemBlockPotato(Block block, Properties props) {
        super(block, props);
    }

    @CapabilityInject(ItemNBTHelper.class)
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity e, int t, boolean idunno) {
        if (!world.isRemote && e instanceof PlayerEntity && e.ticksExisted % 30 == 0
                && TYPOS.matcher(stack.getDisplayName().getString()).matches()) {
            PlayerEntity player = (PlayerEntity) e;
            int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
            if (ticks < NOT_MY_NAME.length) {
                player.sendMessage(new StringTextComponent(NOT_MY_NAME[ticks]).mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
            }
        }
    }

    @CapabilityInject(ContributorList.class)
    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD && entity instanceof PlayerEntity
                && ContributorList.hasFlower(((PlayerEntity) entity).getGameProfile().getName().toLowerCase(Locale.ROOT));
    }
}
