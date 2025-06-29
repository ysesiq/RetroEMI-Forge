package dev.emi.emi.mixin.minecraft.client;

import net.xylose.emi.inject_interface.EMISlot;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class SlotMixin implements EMISlot {
    @Final @Shadow private int slotIndex;

    @Override
    public int getSlotIndex() {
        return this.slotIndex;
    }
}
