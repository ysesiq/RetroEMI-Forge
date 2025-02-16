package dev.emi.emi.stack.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.api.stack.serializer.EmiIngredientSerializer;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.tag.WildcardItemTag;
import net.minecraft.util.JsonHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagEmiIngredientSerializer implements EmiIngredientSerializer<TagEmiIngredient> {
	static final Pattern STACK_REGEX = Pattern.compile("^#([\\w_\\-.:]+):([\\w_\\-.]+):([\\w_\\-./]+)(\\{.*\\})?$");

	@Override
	public String getType() {
		return "tag";
	}

	@Override
	public EmiIngredient deserialize(JsonElement element) {
		if (JsonHelper.isString(element)) {
			String s = element.getAsString();
			Matcher m = STACK_REGEX.matcher(s);
			if (m.matches()) {
				String registry = m.group(1);
				String id = m.group(3);
				return new TagEmiIngredient(toTag(registry, id), 1);
			}
		}
		else if (element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			String registry = json.get("registry").getAsString();
			String id = json.get("id").getAsString();
			long amount = JsonHelper.getLong(json, "amount", 1);
			float chance = JsonHelper.getFloat(json, "chance", 1);
			TagEmiIngredient stack = new TagEmiIngredient(toTag(registry, id), amount);
			if (chance != 1) {
				stack.setChance(chance);
			}
			return stack;
		}
		return EmiStack.EMPTY;
	}

	private TagKey<?> toTag(String registry, String id) {
		if (registry.equals("wildcard")) {
            int itemID = Integer.parseInt(id.substring(id.lastIndexOf('/') + 1));
            Item item = Item.getItemById(itemID);
			return new WildcardItemTag(item);
		}
		else {
			throw new IllegalArgumentException("Unknown registry " + registry);
		}
	}

	@Override
	public JsonElement serialize(TagEmiIngredient stack) {
		if (stack.getAmount() == 1 && stack.getChance() == 1) {
			String type = stack.key.getFlavor();
			return new JsonPrimitive("#" + type + ":" + stack.key.id());
		}
		else {
			JsonObject json = new JsonObject();
			json.addProperty("type", "tag");
			json.addProperty("registry", "retroemi:" + stack.key.getFlavor());
			json.addProperty("id", stack.key.id().getResourcePath());
			if (stack.getAmount() != 1) {
				json.addProperty("amount", stack.getAmount());
			}
			if (stack.getChance() != 1) {
				json.addProperty("chance", stack.getChance());
			}
			return json;
		}
	}
}
