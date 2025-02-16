package net.xylose.emi.api;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public interface EMIItemStack {
    default boolean isItemEqual(ItemStack par1ItemStack) {
        return false;
    }
    default Items hideFromEMI() {
        return null;
    }
    default public void setEnchanted() {}
}
