package net.xylose.emi.api;

import net.minecraft.util.ResourceLocation;

public interface EMIResourceLocation {
    default int compareTo(ResourceLocation that) {
        return 0;
    }
}
