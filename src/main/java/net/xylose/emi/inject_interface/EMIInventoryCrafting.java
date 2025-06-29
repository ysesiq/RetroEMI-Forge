package net.xylose.emi.inject_interface;

import net.minecraft.item.ItemStack;

public interface EMIInventoryCrafting {
    default int getInventoryWidth() {
        return 0;
    }
    default ItemStack[] getStackList() {
        return null;
    }
}
