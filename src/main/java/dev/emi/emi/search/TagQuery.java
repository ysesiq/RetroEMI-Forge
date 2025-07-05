package dev.emi.emi.search;

import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.tag.TagKey;

import java.util.Set;
import java.util.stream.Collectors;

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
