package dev.emi.emi.mixin.minecraft.client;

import dev.emi.emi.EmiPort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow private IReloadableResourceManager mcResourceManager;

    @Inject(
        method = "startGame",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;fontRenderer:Lnet/minecraft/client/gui/FontRenderer;",
            ordinal = 0
        )
    )
    private void registerReloadListeners(CallbackInfo ci) {
        EmiPort.registerReloadListeners(this.mcResourceManager);
    }
}
