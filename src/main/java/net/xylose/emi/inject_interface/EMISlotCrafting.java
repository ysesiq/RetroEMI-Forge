package net.xylose.emi.inject_interface;

import net.minecraft.inventory.IInventory;

public interface EMISlotCrafting {
    default IInventory getCraftMatrix() {
        return null;
    }
}
