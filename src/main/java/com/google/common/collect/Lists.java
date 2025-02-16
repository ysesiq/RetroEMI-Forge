package com.google.common.collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Lists {

	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<>();
	}

	public static <T> ArrayList<T> newArrayList(Iterable<T> i) {
		return newArrayList(i.iterator());
	}

	public static <T> ArrayList<T> newArrayList(Iterator<T> i) {
		var li = new ArrayList<T>();
		while (i.hasNext()) {
			li.add(i.next());
		}
		return li;
	}

	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... a) {
		return newArrayList(Arrays.asList(a));
	}

	public static <T> LinkedList<T> newLinkedList() {
		return new LinkedList<>();
	}

}
