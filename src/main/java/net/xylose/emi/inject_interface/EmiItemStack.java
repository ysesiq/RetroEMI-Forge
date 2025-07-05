package net.xylose.emi.inject_interface;

import net.minecraft.init.Items;

public interface EmiItemStack {
    default Items hideFromEMI() {
        return null;
    }
}
