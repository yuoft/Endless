package com.yuo.endless.integration.BOT;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileExposedSimpleInventory;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class InfinityPotatoTile extends TileExposedSimpleInventory implements ITickableTileEntity, INameable {
    private static final String TAG_NAME = "name";
    private static final int JUMP_EVENT = 0;
    public int jumpTicks = 0;
    public ITextComponent name = new StringTextComponent("");
    private int nextDoIt = 0;
    private final LazyValue<int[]> slots = new LazyValue<>(() -> IntStream.range(0, getSizeInventory()).toArray());

    public InfinityPotatoTile() {
        super(EndlessTileTypes.INFINITY_POTATO_TILE.get());
    }

    private void jump() {
        if (this.jumpTicks == 0) {
            if (this.world != null) {
                this.world.addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 0, 20);
            }
        }

    }

    public void interact(PlayerEntity player, Hand hand, ItemStack stack, Direction side) {
        int index = side.getIndex();
        boolean flag = stack.isEmpty(); //取放物品不触发
        if (index >= 0) {
            ItemStack stackAt = getItemHandler().getStackInSlot(index);
            if (!stackAt.isEmpty() && stack.isEmpty()) {
                player.setHeldItem(hand, stackAt);
                getItemHandler().setInventorySlotContents(index, ItemStack.EMPTY);
            } else if (!stack.isEmpty()) {
                ItemStack copy = stack.split(1);

                if (stack.isEmpty()) {
                    player.setHeldItem(hand, stackAt);
                } else if (!stackAt.isEmpty()) {
                    player.inventory.placeItemBackInInventory(player.world, stackAt);
                }

                getItemHandler().setInventorySlotContents(index, copy);
            }
        }

        if (world != null && !world.isRemote) {
            jump();
            if (flag && player.isSneaking())
                addEffects(world, pos, player);
            if (name.getString().toLowerCase(Locale.ROOT).trim().endsWith("shia labeouf") && nextDoIt == 0) {
                nextDoIt = 40;
                world.playSound(null, pos, ModSounds.doit, SoundCategory.BLOCKS, 1F, 1F);
            }

            for (int i = 0; i < inventorySize(); i++) {
                ItemStack stackAt = getItemHandler().getStackInSlot(i);
                if (!stackAt.isEmpty() && stackAt.getItem() == EndlessBlocks.infinityPotato.get().asItem()) {
                    player.sendMessage(new StringTextComponent("Don't talk to me or my son ever again."), Util.DUMMY_UUID);
                    return;
                }
            }

            player.addStat(ModStats.TINY_POTATOES_PETTED);
            PlayerHelper.grantCriterion((ServerPlayerEntity) player, prefix("main/tiny_potato_pet"), "code_triggered");
        }

    }

    /**
     * 给与周围所有生物3分钟2级所有正面BUFF
     * 补满玩家饥饿值
     * @param world 世界
     * @param pos 土豆坐标
     * @param player 右键的玩家
     */
    private void addEffects(World world, BlockPos pos, PlayerEntity player){
        double radius = 10.5;
        int time = 3600;
        int lv = 1;
        AxisAlignedBB bb = new AxisAlignedBB(pos.add(-radius, -2, -radius), pos.add(radius, 2, radius));
        List<LivingEntity> entityList = world.getEntitiesWithinAABB(LivingEntity.class, bb);
        for (LivingEntity living : entityList) {
            double sq = living.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
            if (sq < radius) {
                Iterator<Entry<RegistryKey<Effect>, Effect>> iterator = Registry.EFFECTS.getEntries().iterator();
                boolean b = iterator.hasNext();
                while (b) {
                    Entry<RegistryKey<Effect>, Effect> entry = iterator.next();
                    Effect effect = entry.getValue();
                    if (effect.isBeneficial())
                        living.addPotionEffect(new EffectInstance(effect, time, lv));
                }
            }
        }

        player.getFoodStats().addStats(20, 30.0F);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getItemHandler().isItemValidForSlot(index, stack);
    }

    @Override
    public int count(Item item) {
        return getItemHandler().count(item);
    }

    @Override
    public boolean hasAny(Set<Item> set) {
        return getItemHandler().hasAny(set);
    }

    @Override
    public boolean receiveClientEvent(int id, int param) {
        if (id == JUMP_EVENT) {
            jumpTicks = param;
            return true;
        } else {
            return super.receiveClientEvent(id, param);
        }
    }

    @Override
    public void tick() {
        if (jumpTicks > 0) {
            jumpTicks--;
        }

        if (world != null && !world.isRemote) {
            if (world.rand.nextInt(100) == 0) {
                jump();
            }
            if (nextDoIt > 0) {
                nextDoIt--;
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isRemote) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
    }

    @Override
    public void writePacketNBT(CompoundNBT cmp) {
        super.writePacketNBT(cmp);
        cmp.putString(TAG_NAME, ITextComponent.Serializer.toJson(name));
    }

    @Override
    public void readPacketNBT(CompoundNBT cmp) {
        super.readPacketNBT(cmp);
        name = ITextComponent.Serializer.getComponentFromJson(cmp.getString(TAG_NAME));
    }

    @Override
    protected Inventory createItemHandler() {
        return new Inventory(6);
    }

    @Nonnull
    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent(EndlessBlocks.infinityPotato.get().getTranslationKey());
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return name.getString().isEmpty() ? null : name;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? getCustomName() : getName();
    }


    @Override
    public int[] getSlotsForFace(Direction direction) {
        return slots.getValue();
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        if (isItemValidForSlot(index, stack)) {
            // Vanilla hoppers do not check the inventory's stack limit, so do so here.
            // We don't have to check anything else like stackability because the hopper logic will do it
            ItemStack existing = getStackInSlot(index);
            return existing.getCount() + stack.getCount() <= getInventoryStackLimit();
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return getItemHandler().getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return getItemHandler().getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getItemHandler().decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getItemHandler().removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        getItemHandler().setInventorySlotContents(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return getItemHandler().isUsableByPlayer(player);
    }

    @Override
    public void clear() {
        getItemHandler().clear();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new SidedInvWrapper(this, side)));
    }
}
