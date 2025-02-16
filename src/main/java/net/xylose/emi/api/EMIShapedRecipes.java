package net.xylose.emi.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface EMIShapedRecipes {
    default int getRecipeHeight() {
        return 0;
    }
    default int getRecipeWidth() {
        return 0;
    }
    default ItemStack[] getRecipeItems() {
        return null;
    }
    default ItemStack[] getSecondaryOutput(IInventory inventory) {
        return null;
    }
}
