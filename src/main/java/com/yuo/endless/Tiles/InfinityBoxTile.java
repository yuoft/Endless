package com.yuo.endless.Tiles;

import com.google.common.collect.Lists;
import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class InfinityBoxTile extends AbsEndlessChestTile implements IRecipeHolder {
    private int burnTime; //以烧炼时间
    private int burnTimeTotal; //燃料提供的烧炼时间
    private int cookingTime; //烧炼时间
    private final int cookingTimeTotal = 160; //烧炼总时间
    private final IIntArray burnData = new IIntArray(){
        @Override
        public int get(int index) {
            switch (index){
                case 0: return InfinityBoxTile.this.burnTime;
                case 1: return InfinityBoxTile.this.burnTimeTotal;
                case 2: return InfinityBoxTile.this.cookingTime;
                default: return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index){
                case 0: InfinityBoxTile.this.burnTime = value; break;
                case 1: InfinityBoxTile.this.burnTimeTotal = value; break;
                case 2: InfinityBoxTile.this.cookingTime = value; break;
            }
        }

        @Override
        public int size() {
            return 3;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public InfinityBoxTile(){
        super(EndlessTileTypes.INFINITY_CHEST_TILE.get(), EndlessChestType.INFINITY, () -> EndlessBlocks.infinityBox.get());
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (world == null || world.isRemote) return;
        if (this.isBurning()) {
            this.burnTime--;
        }
        ItemStack burnItem = this.stackHandler.getStackInSlot(253);
        ItemStack burnFuel = this.stackHandler.getStackInSlot(254);
        if (isBurning() || !burnFuel.isEmpty() && !burnItem.isEmpty()){
            Optional<FurnaceRecipe> optional = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(burnItem), world);
            if (optional.isPresent()){
                FurnaceRecipe recipe = optional.get();
                if (!isBurning() && canSmelt(recipe)){ //增加燃烧时间
                    this.burnTime = ForgeHooks.getBurnTime(burnFuel);
                    this.burnTimeTotal = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (burnFuel.hasContainerItem())
                            this.stackHandler.setStackInSlot(254, burnFuel.getContainerItem());
                        else
                        if (!burnFuel.isEmpty()) {
                            burnFuel.shrink(1);
                            if (burnFuel.isEmpty()) {
                                this.stackHandler.setStackInSlot(254, burnFuel.getContainerItem());
                            }
                        }
                    }
                }
                if (this.isBurning() && this.canSmelt(recipe)) { //烧炼进度增加
                    this.cookingTime++;
                    if (this.cookingTime == this.cookingTimeTotal) {
                        this.cookingTime = 0;
                        this.smelt(recipe, world);
                        flag1 = true;
                    }
                } else {
                    this.cookingTime = 0;
                }
            }
        }else if (!this.isBurning() && this.cookingTime > 0) { //燃料进度回退
            this.cookingTime = MathHelper.clamp(this.cookingTime - 2, 0, this.cookingTimeTotal);
        }

        if (flag != this.isBurning()) {
            flag1 = true;
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    /**
     * 判断是否可以进行烧炼
     * @param recipeIn 配方
     * @return 是 true
     */
    private boolean canSmelt(@Nullable FurnaceRecipe recipeIn) {
        ItemStack burnItem = this.stackHandler.getStackInSlot(253);
        if (!burnItem.isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getCraftingResult(new Inventory());
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack burnResult = this.stackHandler.getStackInSlot(255);
                if (burnResult.isEmpty()) {
                    return true;
                } else if (!burnResult.isItemEqual(itemstack)) {
                    return false;
                } else if (burnResult.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && burnResult.getCount() + itemstack.getCount() <= burnResult.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return burnResult.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 进行烧炼物品消耗，产物增加
     * @param recipe 配方
     */
    private void smelt(@Nullable FurnaceRecipe recipe, World world) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack burnItem = this.stackHandler.getStackInSlot(253);
            ItemStack craftingResult = recipe.getCraftingResult(new Inventory(burnItem));
            ItemStack burnResult = this.stackHandler.getStackInSlot(255);
            if (burnResult.isEmpty()) {
                this.stackHandler.setStackInSlot(255, craftingResult.copy());
            } else if (burnResult.getItem() == craftingResult.getItem()) {
                burnResult.grow(craftingResult.getCount());
            }

            if (!world.isRemote) { //设置使用配方
                this.setRecipeUsed(recipe);
            }

            ItemStack burnFuel = this.stackHandler.getStackInSlot(254); //湿海绵烧炼
            if (burnItem.getItem() == Blocks.WET_SPONGE.asItem() && !burnFuel.isEmpty() && burnFuel.getItem() == Items.BUCKET) {
                this.stackHandler.setStackInSlot(254, new ItemStack(Items.WATER_BUCKET));
            }

            burnItem.shrink(1);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.endless.infinity_chest");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new InfinityBoxContainer(id, player, this, burnData);
    }

    @Override
    public void NbtRead(CompoundNBT nbt){
        super.NbtRead(nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.burnTimeTotal = ForgeHooks.getBurnTime(this.stackHandler.getStackInSlot(254));
        this.cookingTime = nbt.getInt("CookingTime");
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");
        for(String s : compoundnbt.keySet()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public CompoundNBT NbtWrite(CompoundNBT compound){
        CompoundNBT nbt = super.NbtWrite(compound);
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("BurnTimeTotal", this.burnTimeTotal);
        nbt.putInt("CookingTime", this.cookingTime);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        nbt.put("RecipesUsed", compoundnbt);
        return nbt;
    }

    /**
     * 清除使用配方 并生成经验
     * @param player 玩家
     */
    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.world, player.getPositionVec());
        player.unlockRecipes(list);
        this.recipes.clear();
    }

    public List<IRecipe<?>> grantStoredRecipeExperience(World world, Vector3d pos) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
            world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
        int i = MathHelper.floor((float)craftedAmount * experience);
        float f = MathHelper.frac((float)craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
        }

    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }
}
