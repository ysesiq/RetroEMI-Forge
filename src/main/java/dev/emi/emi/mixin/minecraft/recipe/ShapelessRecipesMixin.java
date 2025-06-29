package dev.emi.emi.mixin.minecraft.recipe;

import net.xylose.emi.inject_interface.EMIShapelessRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(ShapelessRecipes.class)
public class ShapelessRecipesMixin implements EMIShapelessRecipes {
    @Mutable @Final @Shadow private final List recipeItems;
    @Unique private ItemStack[] recipeSecondaryOutputs;

    public ShapelessRecipesMixin(List recipeItems) {
        this.recipeItems = recipeItems;
    }

    @Override
    public List getRecipeItems() {
        return recipeItems;
    }

    @Override
    public ItemStack[] getSecondaryOutput(IInventory inventory) {
        return this.recipeSecondaryOutputs;
    }
}
