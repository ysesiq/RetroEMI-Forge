package net.xylose.emi.api;

import net.minecraft.item.ItemStack;

public interface EMIInventoryCrafting {
    default int getInventoryWidth() {
        return 0;
    }
    default ItemStack[] getStackList() {
        return null;
    }
}
