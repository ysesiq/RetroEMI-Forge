package net.xylose.emi.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface EMIShapelessRecipes {
    default List getRecipeItems() {
        return null;
    }
    default ItemStack[] getSecondaryOutput(IInventory inventory) {
        return null;
    }
}
