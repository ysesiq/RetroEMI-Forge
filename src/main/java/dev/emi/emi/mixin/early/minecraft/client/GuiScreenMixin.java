package dev.emi.emi.mixin.early.minecraft.client;

import dev.emi.emi.mixinsupport.inject_interface.EmiSearchInput;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.rewindmc.retroemi.RetroEMI;

@Mixin(value = GuiScreen.class)
public class GuiScreenMixin implements EmiSearchInput {
    @Unique private boolean emiSearchInput = false;
    @Unique private boolean emiMouseInput = false;

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    public void handleMouseInputEMI(CallbackInfo ci) {
        this.emiMouseInput = RetroEMI.handleMouseInput();
    }

    @Inject(method = "handleKeyboardInput",
        at = @At(
        value = "INVOKE",
            target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z",
            shift = At.Shift.AFTER,
            remap = false
        )
    )
    public void handleKeyboardInputEMI(CallbackInfo ci) {
        this.emiSearchInput = RetroEMI.handleKeyboardInput();
    }

    @Redirect(method = "handleKeyboardInput", at = @At(value = "INVOKE",target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", remap = false))
    private boolean redirectAllowCharInput(){
        int k = Keyboard.getEventKey();
        char c = Keyboard.getEventCharacter();
        return Keyboard.getEventKeyState() || k == 0 && Character.isDefined(c);
    }

    @Override
    public boolean getEMISearchInput() {
        return this.emiSearchInput;
    }

    @Override
    public boolean getEMIMouseInput() {
        return this.emiMouseInput;
    }
}
