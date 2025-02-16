package net.xylose.emi.api;

public interface EMISlot {
    default int getSlotIndex() {
        return 0;
    }
}
