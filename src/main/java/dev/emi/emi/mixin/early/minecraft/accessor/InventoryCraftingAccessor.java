package dev.emi.emi.mixin.early.minecraft.accessor;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryCrafting.class)
public interface InventoryCraftingAccessor {
    @Accessor("inventoryWidth")
    int getInventoryWidth();
    @Accessor("stackList")
    ItemStack[] getStackList();
}
