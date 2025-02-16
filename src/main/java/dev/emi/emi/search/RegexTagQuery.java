package dev.emi.emi.search;

import dev.emi.emi.Prototype;
import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.api.stack.EmiStack;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexTagQuery extends Query {
	private final Set<Object> valid;
	
	public RegexTagQuery(String name) {
		Pattern p = null;
		try {
			p = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
		}
		catch (Exception e) {
		}
		if (p == null) {
			valid = Collections.emptySet();
		}
		else {
			final Pattern pat = p;
			
			valid = EmiTags.getTags(Prototype.class).stream().filter(t -> {
				if (EmiTags.hasTranslation(t)) {
					if (pat.matcher(EmiTags.getTagName(t).getString().toLowerCase()).find()) {
						return true;
					}
				}
				if (pat.matcher(t.id().toString()).find()) {
					return true;
				}
				return false;
			}).flatMap(t -> t.get().stream()).collect(Collectors.toSet());
		}
	}
	
	@Override
	public boolean matches(EmiStack stack) {
		return valid.contains(stack.getKey());
	}
}
