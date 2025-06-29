package net.xylose.emi.inject_interface;

import net.minecraft.inventory.Slot;

public interface EMIGuiContainerCreative {
    default Slot getTheSlot() {
        return null;
    }

    default int getGuiLeft() {
        return 0;
    }

    default int getGuiTop() {
        return 0;
    }

    default int getxSize() {
        return 0;
    }

    default int getySize() {
        return 0;
    }
}
