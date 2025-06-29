package dev.emi.emi;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.data.EmiRemoveFromIndex;
import dev.emi.emi.data.EmiTagExclusionsLoader;
import dev.emi.emi.data.RecipeDefaultLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.InputStream;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Multiversion quarantine, to avoid excessive git pain
 */
public final class EmiPort {

	public static MutableText literal(String s) {
		return Text.literal(s);
	}

	public static MutableText literal(String s, Formatting formatting) {
		return Text.literal(s).formatted(formatting);
	}

	public static MutableText literal(String s, Formatting... formatting) {
		return Text.literal(s).formatted(formatting);
	}

	public static MutableText literal(String s, Style style) {
		return Text.literal(s).setStyle(style);
	}

	public static MutableText translatable(String s) {
		return Text.translatable(s);
	}

	public static MutableText translatable(String s, Formatting formatting) {
		return Text.translatable(s).formatted(formatting);
	}

	public static MutableText translatable(String s, Object... objects) {
		return Text.translatable(s, objects);
	}

	public static MutableText append(MutableText text, Text appended) {
		return text.append(appended);
	}

	public static OrderedText ordered(Text text) {
		return text.asOrderedText();
	}


	public static InputStream getInputStream(IResource resource) {
		try {
			return resource.getInputStream();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static int getGuiScale(Minecraft client) {
		return new ScaledResolution(client, client.displayWidth, client.displayHeight).getScaleFactor();
	}

	public static void setPositionTexShader() {
		glEnable(GL_TEXTURE_2D);
	}

	public static void setPositionColorTexShader() {
		glEnable(GL_TEXTURE_2D);
	}

    public static FMLControlledNamespacedRegistry<Item> getItemRegistry() {
        return GameData.getItemRegistry();
    }

	public static ButtonWidget newButton(int x, int y, int w, int h, Text name, ButtonWidget.PressAction action) {
		return ButtonWidget.builder(name, action).position(x, y).size(w, h).build();
	}

	public static ItemStack getOutput(IRecipe recipe) {
		return recipe.getRecipeOutput();
	}

	public static void focus(TextFieldWidget widget, boolean focused) {
		widget.setFocused(focused);
	}

	public static void registerReloadListeners(IReloadableResourceManager manager) {
		manager.registerReloadListener(new RecipeDefaultLoader());
		manager.registerReloadListener(new EmiRemoveFromIndex());
		manager.registerReloadListener(new EmiTagExclusionsLoader());
	}

    public static Comparison compareStrict() {
        return Comparison.compareComponents();
    }
}
