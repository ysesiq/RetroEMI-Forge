package dev.emi.emi.api.recipe;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class EmiPatternCraftingRecipe extends EmiCraftingRecipe {
	protected final int unique = EmiUtil.RANDOM.nextInt();

	public EmiPatternCraftingRecipe(List<EmiIngredient> input, EmiStack output, ResourceLocation id) {
		super(input, output, id, null);
	}

	public EmiPatternCraftingRecipe(List<EmiIngredient> input, EmiStack output, ResourceLocation id, boolean shapeless) {
		super(input, output, id, shapeless, null);
	}

	public abstract SlotWidget getInputWidget(int slot, int x, int y);

	public abstract SlotWidget getOutputWidget(int x, int y);

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
		if (shapeless) {
			widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);
		}
		for (int i = 0; i < 9; i++) {
			widgets.add(getInputWidget(i, i % 3 * 18, i / 3 * 18));
		}
		widgets.add(getOutputWidget(92, 14).large(true).recipeContext(this));
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
