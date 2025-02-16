package net.xylose.emi.mixin.minecraft.server;

import dev.emi.emi.network.EmiNetwork;
import dev.emi.emi.network.PingS2CPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {

    @Shadow @Final private MinecraftServer mcServer;

    @Inject(method = "playerLoggedIn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER))
    private void loggedInEMIPack(EntityPlayerMP par1EntityPlayerMP, CallbackInfo ci) {
        EmiNetwork.sendToClient(par1EntityPlayerMP, new PingS2CPacket(this.mcServer.isDedicatedServer() || (this.mcServer instanceof IntegratedServer integratedServer && integratedServer.getPublic())));
    }
}
