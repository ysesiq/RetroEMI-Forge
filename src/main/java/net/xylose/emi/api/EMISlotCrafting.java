package net.xylose.emi.api;

import net.minecraft.inventory.IInventory;

public interface EMISlotCrafting {
    default IInventory getCraftMatrix() {
        return null;
    }
}
