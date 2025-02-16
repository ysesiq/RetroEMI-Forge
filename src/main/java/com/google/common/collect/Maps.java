package com.google.common.collect;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maps {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}

	public static <K, V> Map<K, V> newHashMap(Map<K, V> map) {
		return new HashMap<>(map);
	}

	public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
		return new IdentityHashMap<>();
	}

}
