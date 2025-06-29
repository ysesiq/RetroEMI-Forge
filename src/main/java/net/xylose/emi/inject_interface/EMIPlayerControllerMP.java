package net.xylose.emi.inject_interface;

import net.minecraft.client.network.NetHandlerPlayClient;

public interface EMIPlayerControllerMP {
    default NetHandlerPlayClient getNetClientHandler() {
        return null;
    }
}
