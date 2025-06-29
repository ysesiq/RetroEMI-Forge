package dev.emi.emi.mixin.minecraft.server;

import net.xylose.emi.inject_interface.EMIPlayerControllerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin implements EMIPlayerControllerMP {
    @Mutable @Final @Shadow private final NetHandlerPlayClient netClientHandler;

    public PlayerControllerMPMixin(NetHandlerPlayClient netClientHandler) {
        this.netClientHandler = netClientHandler;
    }

    public NetHandlerPlayClient getNetClientHandler() {
        return this.netClientHandler;
    }
}
