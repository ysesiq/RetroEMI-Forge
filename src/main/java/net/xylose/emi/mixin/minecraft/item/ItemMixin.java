package net.xylose.emi.mixin.minecraft.item;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.data.EmiRemoveFromIndex;
import net.xylose.emi.api.EMIItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Items.class)
public class ItemMixin implements EMIItem {
    @Unique
    protected int defaultFurnaceBurnTime = 0;

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public int getFurnaceBurnTime(int iItemDamage) {
        return defaultFurnaceBurnTime;
    }

    @Override
    public Items hideFromEMI() {
        if (MixinEnvironment.getCurrentEnvironment().equals((MixinEnvironment.Side.CLIENT))) {
            for (int i = 0; i < 16; i++) {
                EmiRemoveFromIndex.removed.add(EmiStack.of(new ItemStack((Item) (Object) this, 1, i)));
            }
        }
        return (Items) (Object) this;
    }
}
