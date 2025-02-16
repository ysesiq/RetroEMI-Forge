package net.xylose.emi.mixin.minecraft.server;

import dev.emi.emi.EMIPostInit;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void initEMIServer(File workDir, Proxy proxy, CallbackInfo ci) {
        EMIPostInit.initEMI();
    }
}
