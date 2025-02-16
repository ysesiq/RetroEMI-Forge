package dev.emi.emi.data;

import dev.emi.emi.registry.EmiTags;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class EmiTagExclusionsLoader implements EmiResourceReloadListener, IResourceManagerReloadListener {
	private static final ResourceLocation ID = new ResourceLocation("emi", "tag_exclusions");

	@Override
	public ResourceLocation getEmiId() {
		return ID;
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		TagExclusions exclusions = new TagExclusions();
//		for (ResourceLocation id : EmiPort.findResources(manager, "tag/exclusions", i -> i.endsWith(".json"))) {
//			if (!id.getResourceDomain().equals("emi")) {
//				continue;
//			}
//			try {
//				for (EmiResource resource : manager.getAllResources(id)) {
//					InputStreamReader reader = new InputStreamReader(EmiPort.getInputStream(resource));
//					JsonObject json = JsonHelper.deserialize(GSON, reader, JsonObject.class);
//					try {
//						if (JsonHelper.getBoolean(json, "replace", false)) {
//							exclusions.clear();
//						}
//						for (String key : json.entrySet().stream().map(Map.Entry::getKey).toList()) {
//							ResourceLocation type = new ResourceLocation(key);
//							if (JsonHelper.hasArray(json, key)) {
//								JsonArray arr = JsonHelper.getArray(json, key);
//								for (JsonElement el : arr) {
//									ResourceLocation eid = new ResourceLocation(el.getAsString());
//									if (key.equals("exclusions")) {
//										exclusions.add(eid);
//									}
//									else {
//										exclusions.add(type, eid);
//									}
//								}
//							}
//						}
//					}
//					catch (Exception e) {
//						EmiLog.error("Error loading tag exclusions");
//						e.printStackTrace();
//					}
//				}
//			}
//			catch (Exception e) {
//				EmiLog.error("Error loading tag exclusions");
//				e.printStackTrace();
//			}
//		}
		EmiTags.exclusions = exclusions;
	}

}
