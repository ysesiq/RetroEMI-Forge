package dev.emi.emi.util;

import java.util.Map;
import java.util.Objects;

public final class InheritanceMap<V> {
    private final Map<Class<?>, V> map;

    public InheritanceMap(Map<Class<?>, V> map) {
        this.map = map;
    }

    public Map<Class<?>, V> map() {
        return map;
    }

    public Class<?> getKey(Class<?> clazz) {
        Class<?> w = clazz;
        while (w != null) {
            if (map.containsKey(w)) {
                return w;
            }
            if (w == Object.class) {
                break;
            }
            w = w.getSuperclass();
        }
        for (Class<?> i : clazz.getInterfaces()) {
            if (map.containsKey(i)) {
                return i;
            }
        }
        return null;
    }

    public V get(Class<?> clazz) {
        clazz = getKey(clazz);
        if (clazz != null) {
            return map.get(clazz);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InheritanceMap<?> that = (InheritanceMap<?>) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "InheritanceMap{map=" + map + '}';
    }
}
