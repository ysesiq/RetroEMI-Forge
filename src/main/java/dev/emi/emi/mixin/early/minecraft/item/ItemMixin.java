package dev.emi.emi.mixin.early.minecraft.item;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackConvertible;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Items.class)
public class ItemMixin implements EmiStackConvertible {
    @Override
    public EmiStack emi() {
        return EmiStack.of((Item) (Object) this);
    }

    @Override
    public EmiStack emi(long amount) {
        return EmiStack.of((Item) (Object) this, amount);
    }
}
