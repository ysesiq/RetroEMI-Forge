package net.xylose.emi.mixin.minecraft.client;

import net.xylose.emi.api.EMISlot;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class SlotMixin implements EMISlot {
    //locked just MITE?
    @Final @Shadow private int slotIndex;
//    @Shadow protected boolean locked;

    @Override
    public int getSlotIndex() {
        return this.slotIndex;
    }
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    public void setUnLocked(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, CallbackInfo ci) {
//        this.locked = false;
//    }


//    @Inject(method = "setLocked", at = @At("HEAD"), cancellable = true)
//    public void setLocked(boolean locked, CallbackInfo info) {
//        if (locked) {
//            info.cancel();
//        }
//    }
}
