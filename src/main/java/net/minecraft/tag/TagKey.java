package net.minecraft.tag;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface TagKey<T> {
	ResourceLocation id();
	List<T> get();
	String getFlavor();
}
