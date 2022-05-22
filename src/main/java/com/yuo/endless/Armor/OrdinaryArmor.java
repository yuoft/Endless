package com.yuo.endless.Armor;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;

public class OrdinaryArmor extends ArmorItem {
    public OrdinaryArmor(IArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Properties().maxStackSize(1).group(ModGroup.endless));
    }
}
