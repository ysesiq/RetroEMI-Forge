package net.xylose.emi.inject_interface;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public interface EMIItemStack {
    default Items hideFromEMI() {
        return null;
    }
}
