package net.xylose.emi.mixin.minecraft.client;

import com.google.common.collect.Multimap;
import dev.emi.emi.EMIPostInit;
import dev.emi.emi.EmiPort;
import net.xylose.emi.api.EMIMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.Proxy;

@Mixin(Minecraft.class)
public class MinecraftMixin implements EMIMinecraft {
    @Shadow private Timer timer;
    @Shadow private IReloadableResourceManager mcResourceManager;

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Inject(
        method = "startGame",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;fontRenderer:Lnet/minecraft/client/gui/FontRenderer;",
            ordinal = 0
        )
    )
    private void registerReloadListeners(CallbackInfo ci) {
        //Added with EMI
        EmiPort.registerReloadListeners(this.mcResourceManager);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initEMIClient(Session sessionIn, int displayWidth, int displayHeight, boolean fullscreen, boolean isDemo, File dataDir, File assetsDir, File resourcePackDir, Proxy proxy, String version, Multimap twitchDetails, String assetsJsonVersion, CallbackInfo ci) {
    }

//    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiScreen;allowsImposedChat()Z"))
//    public boolean allowsImposedChat(GuiScreen guiScreen) {
//        return !EmiScreenManager.search.isFocused() && guiScreen.allowsImposedChat();
//    }
}
