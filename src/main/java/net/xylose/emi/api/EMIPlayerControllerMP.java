package net.xylose.emi.api;

import net.minecraft.client.network.NetHandlerPlayClient;

public interface EMIPlayerControllerMP {
    default NetHandlerPlayClient getNetClientHandler() {
        return null;
    }
}
