package net.xylose.emi.mixin.minecraft.recipe;

import net.xylose.emi.api.EMIShapedRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ShapedRecipes.class)
public class ShapedRecipesMixin implements EMIShapedRecipes {
    @Final @Shadow public int recipeWidth;
    @Final @Shadow public int recipeHeight;
    @Final @Shadow public ItemStack[] recipeItems;

    @Unique private ItemStack[] recipeSecondaryOutputs;

    @Override
    public int getRecipeHeight() {
        return recipeHeight;
    }

    @Override
    public int getRecipeWidth() {
        return recipeWidth;
    }

    @Override
    public ItemStack[] getRecipeItems() {
        return recipeItems;
    }

    @Override
    public ItemStack[] getSecondaryOutput(IInventory inventory) {
        return this.recipeSecondaryOutputs;
    }
}
