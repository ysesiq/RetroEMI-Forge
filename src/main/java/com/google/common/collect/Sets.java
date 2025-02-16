package com.google.common.collect;

import java.util.*;

public class Sets {

	public static <T> HashSet<T> newHashSet() {
		return new HashSet<>();
	}

	public static <T> HashSet<T> newHashSet(Iterable<T> i) {
		return newHashSet(i.iterator());
	}

	public static <T> HashSet<T> newHashSet(Iterator<T> i) {
		var li = new HashSet<T>();
		while (i.hasNext()) {
			li.add(i.next());
		}
		return li;
	}

	public static <T> LinkedHashSet<T> newLinkedHashSet() {
		return new LinkedHashSet<>();
	}

	@SafeVarargs
	public static <T> LinkedHashSet<T> newLinkedHashSet(T... t) {
		return new LinkedHashSet<>(Arrays.asList(t));
	}

	public static <T> HashSet<T> newLinkedHashSet(Iterable<T> i) {
		return newLinkedHashSet(i.iterator());
	}

	public static <T> LinkedHashSet<T> newLinkedHashSet(Iterator<T> i) {
		var li = new LinkedHashSet<T>();
		while (i.hasNext()) {
			li.add(i.next());
		}
		return li;
	}

	public static <T> Set<T> newIdentityHashSet() {
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}

}
