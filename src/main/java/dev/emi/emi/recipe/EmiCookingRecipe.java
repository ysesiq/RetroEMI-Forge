package dev.emi.emi.recipe;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public class EmiCookingRecipe implements EmiRecipe {
	private final ResourceLocation id;
	private final EmiRecipeCategory category;
	private final EmiIngredient input;
	private final EmiStack output;
	private int fuelMultiplier;
	private float xp;

	public EmiCookingRecipe(ResourceLocation id, ItemStack input, ItemStack output, EmiRecipeCategory category, int fuelMultiplier, float xp) {
		this.id = id;
		this.category = category;
		this.input = EmiStack.of(input);
		this.output = EmiStack.of(output);
		this.fuelMultiplier = fuelMultiplier;
		this.xp = xp;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return com.rewindmc.retroemi.shim.java.List.of(input);
	}

	@Override
	public List<EmiStack> getOutputs() {
		return com.rewindmc.retroemi.shim.java.List.of(output);
	}

	@Override
	public int getDisplayWidth() {
		return 82;
	}

	@Override
	public int getDisplayHeight() {
		return 38;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int duration = (200 << fuelMultiplier); //OvenTileEntity.cookTimeMultiplier
		widgets.addFillingArrow(24, 5, 50 * 200).tooltip((mx, my) -> com.rewindmc.retroemi.shim.java.List.of((TooltipComponent.of(EmiPort.ordered(EmiPort.translatable("emi.cooking.time", duration / 20))))));
		widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 24);
		widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 24, duration * 20, false, true, true);
		widgets.addSlot(input, 0, 4);
		widgets.addSlot(output, 56, 0).large(true).recipeContext(this);
		if (xp != 0)
			widgets.addText(EmiPort.ordered(EmiPort.translatable("emi.cooking.experience", xp)), 26, 28, -1, true);

	}
}
