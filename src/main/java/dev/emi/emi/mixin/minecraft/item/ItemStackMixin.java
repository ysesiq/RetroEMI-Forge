package dev.emi.emi.mixin.minecraft.item;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.data.EmiRemoveFromIndex;
import net.xylose.emi.inject_interface.EmiItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;

@Mixin(ItemStack.class)
public class ItemStackMixin implements EmiItemStack {
    @Override
    public Items hideFromEMI() {
        if (MixinEnvironment.getCurrentEnvironment().equals((MixinEnvironment.Side.CLIENT))) {
            for (int i = 0; i < 16; i++) {
                EmiRemoveFromIndex.removed.add(EmiStack.of(new ItemStack((Item) (Object) this , 1, i)));
            }
        }
        return (Items) (Object) this;
    }
}
