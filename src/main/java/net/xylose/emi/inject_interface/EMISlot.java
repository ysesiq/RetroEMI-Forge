package net.xylose.emi.inject_interface;

public interface EMISlot {
    default int getSlotIndex() {
        return 0;
    }
}
