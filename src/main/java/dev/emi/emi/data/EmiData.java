package dev.emi.emi.data;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.MITEEmiRecipeCategories;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SyntheticIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Helper class for things that are done via JSON in modern versions, as seen on the <a href="https://github.com/emilyploszaj/emi/wiki">official EMI wiki page.</a>
 */
public class EmiData {
	public static Map<String, EmiRecipeCategoryProperties> categoryPriorities = new HashMap<>();
	public static List<Supplier<EmiAlias>> aliases = new ArrayList<>();
	public static List<Predicate<EmiRecipe>> recipeFilters = Lists.newArrayList();

	public static void init(/*Consumer<EmiResourceReloadListener> register*/) {
//		register.accept(new RecipeDefaultLoader());
//		register.accept(new EmiTagExclusionsLoader());
//		register.accept(new EmiRemoveFromIndex());
		defaultAliases();
	}

	public static void addAliases(List<EmiIngredient> ingredients, String aliasName) {
		addAliases(ingredients, com.rewindmc.retroemi.shim.java.List.of(aliasName));
	}

	public static void addAliases(EmiIngredient ingredient, List<String> aliasName) {
		addAliases(com.rewindmc.retroemi.shim.java.List.of(ingredient), aliasName);
	}

	public static void addAliases(EmiIngredient ingredient, String aliasName) {
		addAliases(com.rewindmc.retroemi.shim.java.List.of(ingredient), com.rewindmc.retroemi.shim.java.List.of(aliasName));
	}

	/**
	 * Adds a search alias for an item, allowing for items to show from multiple different search results.
	 *
	 * @param ingredients List of items that will have an alias added
	 * @param aliasName   List of strings that, when searched, will show the item
	 * @see #defaultAliases()
	 */
	public static void addAliases(List<EmiIngredient> ingredients, List<String> aliasName) {
		aliases.add(() -> new EmiAlias(ingredients, aliasName));
	}

	/**
	 * Hides recipe from EMI only, does not disable recipe.
	 *
	 * @param id Resource location for recipe. Generally uses {@link SyntheticIdentifier}
	 */
	public static void hideRecipe(ResourceLocation id) {
		Predicate<EmiRecipe> predicates = r -> {
			String rid = r.getId() == null ? "null" : r.getId().toString();
			return rid.equals(id.toString());
		};
		recipeFilters.add(predicates);
	}

	/** Sets the render order of a category. Should be run after plugin is done initializing.
	 * @param order		Display order, lower first. Default is 0, Crafting (first in order) is -1000.
	 * @param category	Category that is having its priority changed.
	 * */
	public static void setCategoryOrder(int order, EmiRecipeCategory category) {
		EmiRecipeCategoryProperties props = new EmiRecipeCategoryProperties();
		props.order = order;
		categoryPriorities.put(category.getId().toString(), props);
	}

	private static void defaultAliases() {
		List<EmiIngredient> craftingTables = com.rewindmc.retroemi.shim.java.List.of(EmiStack.of(Blocks.crafting_table));
		EmiData.addAliases(craftingTables, com.rewindmc.retroemi.shim.java.List.of("alias.emi.crafting_table", "alias.emi.workbench"));

		EmiData.addAliases(EmiStack.of(Items.stick), "alias.emi.stick");
		EmiData.addAliases(EmiStack.of(Blocks.tnt), "alias.emi.tnt");
	}

	//todo finish adding all recipe categories
	public static void defaultCategoryOrder() {
		EmiData.setCategoryOrder(-1000, VanillaEmiRecipeCategories.CRAFTING);
		EmiData.setCategoryOrder(100, VanillaEmiRecipeCategories.FUEL); //Should fuel show time in oven?
//		EmiData.setCategoryOrder(120, MITEEmiRecipeCategories.FOOD);
	}
}
