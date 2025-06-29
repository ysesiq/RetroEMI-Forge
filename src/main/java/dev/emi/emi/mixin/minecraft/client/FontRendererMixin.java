package dev.emi.emi.mixin.minecraft.client;

import com.rewindmc.retroemi.REMIMixinHooks;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.Style;

import java.util.Random;

@Mixin(value = FontRenderer.class, priority = 2000)
public abstract class FontRendererMixin {
//    @Shadow protected int[] charWidth;
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void modifyChanceTableSize(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4, CallbackInfo ci) {
//        this.charWidth = new int[32767];
//    }

    @ModifyVariable(
        method = "renderStringAtPos",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        ordinal = 0 /*i*/
    )
    private int customFontColor(int original, String text, boolean shadow) {
        return REMIMixinHooks.applyCustomFormatCodes((FontRenderer) (Object) this, text, shadow, original);
    }
}
