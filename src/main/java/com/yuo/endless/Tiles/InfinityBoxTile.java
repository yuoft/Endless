package com.yuo.endless.Tiles;

import com.google.common.collect.Lists;
import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class InfinityBoxTile extends AbsEndlessChestTile implements RecipeHolder {
    private int burnTime; //以烧炼时间
    private int burnTimeTotal; //燃料提供的烧炼时间
    private int cookingTime; //烧炼时间
    private static final int cookingTimeTotal = 160; //烧炼总时间
    public final ContainerData burnData = new ContainerData(){
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
        public int getCount() {
            return 3;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public InfinityBoxTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.INFINITY_CHEST_TILE.get(), EndlessChestType.INFINITY, () -> EndlessBlocks.infinityBox.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InfinityBoxTile tile) {
        tile.chestLidController.tickLid();
        boolean flag = tile.isBurning();
        boolean flag1 = false;
        if (tile.level == null) return;
        if (tile.isBurning()) {
            tile.burnTime--;
        }
        ItemStack burnItem = tile.items.get(253);
        ItemStack burnFuel = tile.items.get(254);
        if (tile.isBurning() || !burnFuel.isEmpty() && !burnItem.isEmpty()){
            Optional<SmeltingRecipe> optional = tile.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(burnItem), level);
            if (optional.isPresent()){
                SmeltingRecipe recipe = optional.get();
                if (!tile.isBurning() && tile.canSmelt(recipe)){ //增加燃烧时间
                    tile.burnTime = ForgeHooks.getBurnTime(burnFuel, RecipeType.SMELTING);
                    tile.burnTimeTotal = tile.burnTime;
                    if (tile.isBurning()) {
                        flag1 = true;
                        if (burnFuel.hasContainerItem())
                            tile.items.set(254, burnFuel.getContainerItem());
                        else
                        if (!burnFuel.isEmpty()) {
                            burnFuel.shrink(1);
                            if (burnFuel.isEmpty()) {
                                tile.items.set(254, burnFuel.getContainerItem());
                            }
                        }
                    }
                }
                if (tile.isBurning() && tile.canSmelt(recipe)) { //烧炼进度增加
                    tile.cookingTime += 5;
                    if (tile.cookingTime == cookingTimeTotal) {
                        tile.cookingTime = 0;
                        tile.smelt(recipe, level);
                        flag1 = true;
                    }
                } else {
                    tile.cookingTime = 0;
                }
            }
        }else if (!tile.isBurning() && tile.cookingTime > 0) { //燃料进度回退
            tile.cookingTime = Mth.clamp(tile.cookingTime - 2, 0, cookingTimeTotal);
        }

        if (flag != tile.isBurning()) {
            flag1 = true;
        }

        if (flag1) {
            tile.setChanged();
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
    private boolean canSmelt(@Nullable SmeltingRecipe recipeIn) {
        ItemStack burnItem = this.items.get(253);
        if (!burnItem.isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.assemble(new SimpleContainer(burnItem));
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack burnResult = this.items.get(255);
                if (burnResult.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSame(burnResult, itemstack)) {
                    return false;
                } else if (burnResult.getCount() + itemstack.getCount() <= this.getMaxStackSize() && burnResult.getCount() + itemstack.getCount() <= burnResult.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
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
    private void smelt(@Nullable SmeltingRecipe recipe, Level world) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack burnItem = this.items.get(253);
            ItemStack craftingResult = recipe.assemble(new SimpleContainer(burnItem));
            ItemStack burnResult = this.items.get(255);
            if (burnResult.isEmpty()) {
                this.items.set(255, craftingResult.copy());
            } else if (burnResult.getItem() == craftingResult.getItem()) {
                burnResult.grow(craftingResult.getCount());
            }

            if (!world.isClientSide) { //设置使用配方
                this.setRecipeUsed(recipe);
            }

            ItemStack burnFuel = this.items.get(254); //湿海绵烧炼
            if (burnItem.getItem() == Blocks.WET_SPONGE.asItem() && !burnFuel.isEmpty() && burnFuel.getItem() == Items.BUCKET) {
                this.items.set(254, new ItemStack(Items.WATER_BUCKET));
            }

            burnItem.shrink(1);
        }
    }


    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.endless.infinity_chest");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new InfinityBoxContainer(id, inventory, this);
    }

    @Override
    public void NbtRead(CompoundTag nbt){
        super.NbtRead(nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.burnTimeTotal = ForgeHooks.getBurnTime(this.items.get(254), RecipeType.SMELTING);
        this.cookingTime = nbt.getInt("CookingTime");
        CompoundTag compoundnbt = nbt.getCompound("RecipesUsed");
        for(String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
    }

    @Override
    public void NbtWrite(CompoundTag nbt){
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("BurnTimeTotal", this.burnTimeTotal);
        nbt.putInt("CookingTime", this.cookingTime);
        CompoundTag compoundnbt = new CompoundTag();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        nbt.put("RecipesUsed", compoundnbt);
        super.NbtWrite(nbt);
    }

    /**
     * 清除使用配方 并生成经验
     * @param player 玩家
     */
    public void unlockRecipes(Player player) {
        List<Recipe<?>> list = this.grantStoredRecipeExperience(player.level, player.getUpVector(0.5f));
        player.resetRecipes(list);
        this.recipes.clear();
    }

    public List<Recipe<?>> grantStoredRecipeExperience(Level world, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
            world.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(Level world, Vec3 pos, int craftedAmount, float experience) {
        int i = Mth.floor((float)craftedAmount * experience);
        float f = Mth.frac((float)craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, j));
        }

    }

    @Override
    public void setRecipeUsed(@org.jetbrains.annotations.Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }
}
