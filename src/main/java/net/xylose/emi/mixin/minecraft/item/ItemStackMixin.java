package net.xylose.emi.mixin.minecraft.item;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.data.EmiRemoveFromIndex;
import net.xylose.emi.api.EMIItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public class ItemStackMixin implements EMIItemStack {
    @Shadow private Item field_151002_e;
    @Shadow int itemDamage;
    @Shadow public NBTTagCompound stackTagCompound;

//    @Override
//    public boolean isItemEqual(ItemStack par1ItemStack) {
//        return this.field_151002_e == par1ItemStack.field_151002_e && this.itemDamage == par1ItemStack.getItemDamage();
//    }

    @Override
    public Items hideFromEMI() {
        if (MixinEnvironment.getCurrentEnvironment().equals((MixinEnvironment.Side.CLIENT))) {
            for (int i = 0; i < 16; i++) {
                EmiRemoveFromIndex.removed.add(EmiStack.of(new ItemStack((Item) (Object) this , 1, i)));
            }
        }
        return (Items) (Object) this;
    }

    @Override
    public void setEnchanted() {
//        if (this.stackTagCompound == null) {
//            this.setTagCompound(new NBTTagCompound());
//        }
        if (!this.stackTagCompound.hasKey("ench")) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
    }

//    @Shadow
//    public ItemStack setTagCompound(NBTTagCompound nbtTagCompound) {
//        return null;
//    }
}
