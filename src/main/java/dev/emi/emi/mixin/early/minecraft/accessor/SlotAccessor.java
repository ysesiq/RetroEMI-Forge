package dev.emi.emi.mixin.early.minecraft.accessor;

import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {
    @Accessor("slotIndex")
    int getSlotIndex();
}
