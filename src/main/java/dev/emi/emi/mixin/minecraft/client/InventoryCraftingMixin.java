package dev.emi.emi.mixin.minecraft.client;

import net.xylose.emi.api.EMIInventoryCrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryCrafting.class)
public abstract class InventoryCraftingMixin implements EMIInventoryCrafting, IInventory {
    @Shadow private int inventoryWidth;
    @Shadow private ItemStack[] stackList;

    @Override
    public int getInventoryWidth() {
        return this.inventoryWidth;
    }

    @Override
    public ItemStack[] getStackList() {
        return this.stackList;
    }
}
