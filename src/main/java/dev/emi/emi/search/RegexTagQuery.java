package dev.emi.emi.search;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiTags;
import net.minecraft.tag.TagKey;

public class RegexTagQuery extends Query {
	private final Set<Object> valid;

	public RegexTagQuery(String name) {
		Pattern p = null;
		try {
			p = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
		} catch (Exception e) {
		}
		if (p == null) {
			valid = Collections.emptySet();
		} else {
            final Pattern pat = p;
            valid = TagKey.Type.ITEM.getAll().stream().filter(t -> {
                if (EmiTags.hasTranslation(t)) {
                    if (pat.matcher(EmiTags.getTagName(t).getString().toLowerCase()).find()) {
                        return true;
                    }
                }
                if (pat.matcher(t.id().toString()).find()) {
                    return true;
                }
                return false;
//            }).map(t -> TagKey.Type.ITEM).collect(Collectors.toSet()), TagKey.Type.BLOCK.getAll().stream().filter(t -> {
//					if (pat.matcher(t.id().toString()).find()) {
//						return true;
//					}
//					return false;
            }).map(TagKey::getAll).flatMap(v -> v.stream()).collect(Collectors.toSet());
        }
	}

	@Override
	public boolean matches(EmiStack stack) {
		return valid.contains(stack.getKey());
	}
}
