package dev.emi.emi.api.recipe;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EmiRecipeManager {

	List<EmiRecipeCategory> getCategories();

	List<EmiIngredient> getWorkstations(EmiRecipeCategory category);

	List<EmiRecipe> getRecipes();

	List<EmiRecipe> getRecipes(EmiRecipeCategory category);

	@Nullable EmiRecipe getRecipe(ResourceLocation id);

	List<EmiRecipe> getRecipesByInput(EmiStack stack);

	List<EmiRecipe> getRecipesByOutput(EmiStack stack);
}
