package dev.emi.emi.platform;

import dev.emi.emi.Prototype;
import dev.emi.emi.registry.EmiPluginContainer;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class EmiAgnos {
	public static EmiAgnos delegate;

	static {
		try {
			Class.forName("dev.emi.emi.platform.fabric.EmiAgnosFabric");
		} catch (Throwable t) {
		}
		try {
			Class.forName("dev.emi.emi.platform.forge.EmiAgnosForge");
		} catch (Throwable t) {
		}
	}

	public static boolean isForge() {
		return false;
	}

	protected abstract boolean isForgeAgnos();

	public static String getModName(String namespace) {
		return delegate.getModNameAgnos(namespace);
	}

	protected abstract String getModNameAgnos(String namespace);

	public static Path getConfigDirectory() {
		return delegate.getConfigDirectoryAgnos();
	}

	protected abstract Path getConfigDirectoryAgnos();

	public static boolean isDevelopmentEnvironment() {
		return delegate.isDevelopmentEnvironmentAgnos();
	}

	protected abstract boolean isDevelopmentEnvironmentAgnos();

	public static boolean isModLoaded(String id) {
		return delegate.isModLoadedAgnos(id);
	}

	protected abstract boolean isModLoadedAgnos(String id);

	public static List<String> getAllModNames(String id) {
		return delegate.getAllModNamesAgnos(id);
	}

	protected abstract List<String> getAllModNamesAgnos(String id);

	public static List<String> getAllModAuthors(String id) {
		return delegate.getAllModAuthorsAgnos(id);
	}

	protected abstract List<String> getAllModAuthorsAgnos(String id);

	public static List<EmiPluginContainer> getPlugins() {
		return delegate.getPluginsAgnos();
	}

	protected abstract List<EmiPluginContainer> getPluginsAgnos();

	public static void addBrewingRecipes(EmiRegistry registry) {
		delegate.addBrewingRecipesAgnos(registry);
	}

	protected abstract void addBrewingRecipesAgnos(EmiRegistry registry);

	public static List<TooltipComponent> getItemTooltip(ItemStack stack) {
		return delegate.getItemTooltipAgnos(stack);
	}

	protected abstract List<TooltipComponent> getItemTooltipAgnos(ItemStack stack);

	public static boolean canBatch(ItemStack stack) {
		return delegate.canBatchAgnos(stack);
	}

	protected abstract boolean canBatchAgnos(ItemStack stack);

	public static Map<Prototype, Integer> getFuelMap() {
		return delegate.getFuelMapAgnos();
	}

	protected abstract Map<Prototype, Integer> getFuelMapAgnos();
}
