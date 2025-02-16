package dev.emi.emi.search;

import com.google.common.collect.Sets;
import dev.emi.emi.api.stack.EmiStack;

import java.util.Collections;
import java.util.Set;

public class ItemIDQuery extends Query {
	private final Set<Object> valid;

	public ItemIDQuery(String id) {
		valid = Collections.singleton(Sets.newHashSet(EmiSearch.ids.findAll(id)));
	}

	@Override
	public boolean matches(EmiStack stack) {
		return valid.contains(stack);
	}
}
