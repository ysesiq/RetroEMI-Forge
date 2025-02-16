package dev.emi.emi.widget;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.runtime.EmiScreenshotRecorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.io.File;
import java.util.List;

public class RecipeScreenshotButtonWidget extends RecipeButtonWidget {
	public RecipeScreenshotButtonWidget(int x, int y, EmiRecipe recipe) {
		super(x, y, 60, 0, recipe);
	}

	@Override
	public List<TooltipComponent> getTooltip(int mouseX, int mouseY) {
		return com.rewindmc.retroemi.shim.java.List.of(TooltipComponent.of(EmiPort.ordered(EmiPort.translatable("tooltip.emi.recipe_screenshot"))));
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		this.playButtonSound();

		ResourceLocation id = recipe.getId();
		String path;
		if (id == null) {
			path = "unknown-recipe";
		} else {
			path = id.getResourceDomain() + "/" + id.getResourcePath().replace("/", "_").replace(":", ".");
		}

		Minecraft client = Minecraft.getMinecraft();
		ScaledResolution scaledRes = new ScaledResolution(client, client.displayWidth, client.displayHeight);

		int recipeWidth = recipe.getDisplayWidth() + 8;
		int recipeHeight = recipe.getDisplayHeight() + 8;
		int scaledWidth = recipeWidth * scaledRes.getScaleFactor();
		int scaledHeight = recipeHeight * scaledRes.getScaleFactor();

        String fullPath = "emi/recipes/";
        if (id != null) {
            fullPath = "emi/recipes/" + id.getResourceDomain();
        }
		String screenshotsPath = "emi/recipes/" + path;

        File screenshotsRoot = new File(client.mcDataDir, "screenshots");
		File emiRecipeScreenshotsDir = new File(screenshotsRoot, fullPath);

		if (!emiRecipeScreenshotsDir.exists()) {
			emiRecipeScreenshotsDir.mkdirs();
		}

		DrawContext context = DrawContext.INSTANCE;
		EmiScreenshotRecorder.saveScreenshot(screenshotsPath, 0, scaledHeight, scaledWidth, scaledHeight,
				() -> {EmiRenderHelper.renderRecipe(recipe, EmiDrawContext.wrap(context), 0, 0, false, -1);});

		return true;
	}
}
