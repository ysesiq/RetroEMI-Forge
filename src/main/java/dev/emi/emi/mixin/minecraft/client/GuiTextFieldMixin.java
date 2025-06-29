package dev.emi.emi.mixin.minecraft.client;

import net.xylose.emi.inject_interface.EMIGuiTextField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiTextField.class)
public class GuiTextFieldMixin extends Gui implements EMIGuiTextField {
    @Shadow private boolean isEnabled = true;

    @Override
    public boolean getIsEnabled() {
        return this.isEnabled;
    }
}
