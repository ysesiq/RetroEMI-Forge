package dev.emi.emi.data;

import com.google.gson.*;
import dev.emi.emi.EmiPort;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.runtime.EmiLog;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonHelper;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class RecipeDefaultLoader implements EmiResourceReloadListener, IResourceManagerReloadListener {
	public static final ResourceLocation ID = new ResourceLocation("emi:recipe_defaults");
	private static final Gson GSON = new Gson();
//	public static List<ResourceLocation> addedDefaults = new ArrayList<>();
//	public static List<ResourceLocation> resolutions = new ArrayList<>();
//	public static List<ResourceLocation> tags = new ArrayList<>();

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		RecipeDefaults defaults = new RecipeDefaults();
		try {
			for (IResource resource : (List<IResource>) manager.getAllResources(new ResourceLocation("emi", "recipe/defaults/defaults.json"))) {
				InputStreamReader reader = new InputStreamReader(EmiPort.getInputStream(resource));
				JsonObject json = JsonHelper.deserialize(GSON, reader, JsonObject.class);
				loadDefaults(defaults, json);
			}
		}
		catch (Exception e) {
			EmiLog.error("Error loading recipe default file");
			e.printStackTrace();
		}
		BoM.setDefaults(defaults);
	}

	public static void loadDefaults(RecipeDefaults defaults, JsonObject json) {
		if (JsonHelper.getBoolean(json, "replace", false)) {
			defaults.clear();
		}
		JsonArray disabled = JsonHelper.getArray(json, "disabled", new JsonArray());
		for (JsonElement el : disabled) {
			ResourceLocation id = new ResourceLocation(el.getAsString());
			defaults.remove(id);
		}
		JsonArray added = JsonHelper.getArray(json, "added", new JsonArray());
		if (JsonHelper.hasArray(json, "recipes")) {
			added.addAll(JsonHelper.getArray(json, "recipes"));
		}
		for (JsonElement el : added) {
			ResourceLocation id = new ResourceLocation(el.getAsString());
			defaults.add(id);
		}
		JsonObject resolutions = JsonHelper.getObject(json, "resolutions", new JsonObject());
		for (String key : resolutions.keySet()) {
			ResourceLocation id = new ResourceLocation(key);
			if (JsonHelper.hasArray(resolutions, key)) {
				defaults.add(id, JsonHelper.getArray(resolutions, key));
			}
		}
		JsonObject addedTags = JsonHelper.getObject(json, "tags", new JsonObject());
		for (String key : addedTags.keySet()) {
			defaults.addTag(new JsonPrimitive(key), addedTags.get(key));
		}
	}

	@Override
	public ResourceLocation getEmiId() {
		return ID;
	}
}
