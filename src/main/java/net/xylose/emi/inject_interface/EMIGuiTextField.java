package net.xylose.emi.inject_interface;

public interface EMIGuiTextField {
    default boolean getIsEnabled() {
        return false;
    }
}
