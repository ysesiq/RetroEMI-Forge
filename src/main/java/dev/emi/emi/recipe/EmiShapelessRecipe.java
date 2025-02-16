package dev.emi.emi.recipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.xylose.emi.api.EMIShapelessRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import com.rewindmc.retroemi.RetroEMI;
import net.minecraft.util.SyntheticIdentifier;

import java.util.List;
import java.util.stream.Collectors;

public class EmiShapelessRecipe extends EmiCraftingRecipe {

	private final ShapelessRecipes shapeless_recipe;

	public EmiShapelessRecipe(EMIShapelessRecipes recipe, ShapelessRecipes shapelessRecipes) {
		super(((List<ItemStack>) recipe.getRecipeItems()).stream().map(RetroEMI::wildcardIngredient).collect(Collectors.toList()),
				EmiStack.of(EmiPort.getOutput((IRecipe) recipe)), new SyntheticIdentifier(recipe), recipe.getSecondaryOutput(null));
        EmiShapedRecipe.setRemainders(input, (IRecipe) recipe);
		this.shapeless_recipe = shapelessRecipes;
	}

	@Override
	public boolean canFit(int width, int height) {
		return input.size() <= width * height;
	}

}
