package net.xylose.emi.api;

import net.minecraft.util.Timer;

public interface EMIMinecraft {
    default Timer getTimer() {
        return null;
    }
}
