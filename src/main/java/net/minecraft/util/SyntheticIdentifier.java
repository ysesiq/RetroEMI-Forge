package net.minecraft.util;

import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.mixin.minecraft.accessor.ShapedRecipesAccessor;
import net.minecraft.nbt.StringNbtReader;
import dev.emi.emi.mixin.minecraft.accessor.ShapelessRecipesAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SyntheticIdentifier extends ResourceLocation {

	public SyntheticIdentifier(Object o) {
		super(generateId(o));
	}

	public SyntheticIdentifier(Object o, String tail) {
		super(generateId(o) + tail);
	}

	private static String generateId(Object o) {
		if (o == null) {
			return "null:null";
		} else if (o instanceof ShapedRecipes sr) {
			return "shaped:/" + ((ShapedRecipesAccessor) sr).getRecipeWidth() + "x" + ((ShapedRecipesAccessor) sr).getRecipeHeight() + "/" + describeFlat(((ShapedRecipesAccessor) sr).getRecipeItems()) + "/" +
					describe(sr.getRecipeOutput());
		} else if (o instanceof ShapelessRecipes sr) {
			return "shapeless:/" + describeFlat(((ShapelessRecipesAccessor)sr).getRecipeItems()) + "/" + describe(sr.getRecipeOutput());
		} else if (o instanceof EmiCraftingRecipe cr) {
			return "crafting:/" + describeFlat(cr.getInputs()) + "/" + describe(cr.getOutputs());
		}
		return "unknown:/" + describe(o);
	}

	public static String describeFlat(List<?> li) {
		return describeFlat(li.stream());
	}

	public static String describeFlat(Object[] items) {
		return describeFlat(Arrays.stream(items));
	}

	public static String describeFlat(Stream<?> stream) {
		return stream.map(SyntheticIdentifier::describe).collect(Collectors.joining("/"));
	}

	public static String describe(Object o) {
		if (o == null) {
			return "null";
		} else if (o instanceof EmiStack es) {
			return describe(es.getItemStack());
		} else if (o instanceof EmiIngredient ei) {
			return ei.getEmiStacks().stream().map(SyntheticIdentifier::describe).collect(Collectors.joining("/", "[", "]"));
		} else if (o instanceof ItemStack is) {
            try {
                return Item.getIdFromItem(is.getItem()) + "." + is.getItemDamage() + (is.hasTagCompound() ? StringNbtReader.encode(is.getTagCompound()) : "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (o instanceof Block) {
			return describe(new ItemStack((Block) o));
		} else if (o instanceof String) {
			return (String) o;
		} else if (o instanceof List<?> l) {
			return l.stream().map(SyntheticIdentifier::describe).collect(Collectors.joining("/", "[", "]"));
		} else if (o instanceof Object[]) {
			Object[] arr = (Object[]) o;
			return Arrays.stream(arr).map(SyntheticIdentifier::describe).collect(Collectors.joining("/", "[", "]"));
//		} else if (o instanceof Prototype p) {
//			return p.item() == null ? "0.0" : Item.getIdFromItem(p.item()) + "." + p.meta();
		} else {
			return o.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(o));
		}
	}
}
