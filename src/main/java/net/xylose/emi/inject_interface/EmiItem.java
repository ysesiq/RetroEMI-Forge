package net.xylose.emi.inject_interface;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public interface EmiItem {
    default boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }
    default int getFurnaceBurnTime(int iItemDamage) {
        return 0;
    }
    default Items hideFromEMI() {
        return null;
    }
}
