package com.google.common.collect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class Iterables {

	@Deprecated
	public static <T> Iterable<T> concat(Iterable<T> iter) {
		return iter;
	}
	
	@SafeVarargs
	public static <T> Iterable<T> concat(Iterable<? extends T>... iter) {
		return () -> {
			return (Iterator<T>) Arrays.stream(iter)
					.flatMap(i -> StreamSupport.stream(i.spliterator(), false))
					.iterator();
		};
	}

}
