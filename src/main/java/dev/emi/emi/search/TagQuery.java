package dev.emi.emi.search;

import dev.emi.emi.Prototype;
import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.api.stack.EmiStack;

import java.util.Set;
import java.util.stream.Collectors;

public class TagQuery extends Query {
	private final Set<Object> valid;
	
	public TagQuery(String name) {
		String lowerName = name.toLowerCase();
		valid = EmiTags.getTags(Prototype.class).stream().filter(t -> {
			if (EmiTags.hasTranslation(t)) {
				if (EmiTags.getTagName(t).getString().toLowerCase().contains(lowerName)) {
					return true;
				}
			}
			if (t.id().toString().contains(lowerName)) {
				return true;
			}
			return false;
		}).flatMap(t -> t.get().stream()).collect(Collectors.toSet());
	}
	
	@Override
	public boolean matches(EmiStack stack) {
		return valid.contains(stack.getKey());
	}
}
