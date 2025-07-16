package dev.emi.emi.search;

import java.util.Set;
import java.util.stream.Collectors;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiTags;
import net.minecraft.registry.tag.TagKey;

public class TagQuery extends Query {
	private final Set<Object> valid;

	public TagQuery(String name) {
		String lowerName = name.toLowerCase();
        valid = TagKey.Type.ITEM.getAll().stream().filter(t -> {
			if (EmiTags.hasTranslation(t)) {
				if (EmiTags.getTagName(t).getString().toLowerCase().contains(lowerName)) {
					return true;
				}
			}
			if (t.id().toString().contains(lowerName)) {
				return true;
			}
			return false;
		}).flatMap(t -> t.getAll().stream()).collect(Collectors.toSet());
	}

	@Override
	public boolean matches(EmiStack stack) {
		return valid.contains(stack.getKey());
	}
}
