package dev.emi.emi.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.Prototype;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.data.TagExclusions;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.runtime.EmiReloadLog;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.stack.TagEmiIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;
import net.minecraft.tag.TagKey;
import net.minecraft.tag.WildcardItemTag;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class EmiTags {
	public static final ResourceLocation HIDDEN_FROM_RECIPE_VIEWERS = new ResourceLocation("c", "hidden_from_recipe_viewers");
	private static final Map<TagKey<?>, ResourceLocation> MODELED_TAGS = Maps.newHashMap();
	private static final Map<Set<?>, List<TagKey<?>>> CACHED_TAGS = Maps.newHashMap();
	private static final Map<TagKey<?>, List<?>> TAG_VALUES = Maps.newHashMap();
	private static final List<TagKey<Prototype>> SORTED_ITEM_TAGS = Lists.newArrayList();
	public static final List<TagKey<?>> TAGS = Lists.newArrayList();
	public static TagExclusions exclusions = new TagExclusions();

	@SuppressWarnings("unchecked")
	public static <T> List<EmiStack> getValues(TagKey<T> key) {
		List<T> values = (List<T>) TAG_VALUES.get(key);
		if (values == null) values = key.get();
		if (key instanceof WildcardItemTag) {
			return values.stream().map(t -> EmiStack.of((Prototype) t)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> EmiIngredient getIngredient(Class<T> clazz, List<EmiStack> stacks, long amount) {
		Map<T, EmiStack> map = Maps.newHashMap();
		for (EmiStack stack : stacks) {
			if (!stack.isEmpty()) {
				map.put((T) stack.getKey(), stack);
			}
		}
		if (map.size() == 0) {
			return EmiStack.EMPTY;
		} else if (map.size() == 1) {
			return map.values().stream().collect(Collectors.toList()).get(0).copy().setAmount(amount);
		}
		List<TagKey<T>> keys = (List<TagKey<T>>) (List) CACHED_TAGS.get(map.keySet());

		if (keys != null) {
			for (TagKey<T> key : keys) {
				List<T> values = (List<T>) TAG_VALUES.get(key);
				map.keySet().removeAll(values);
			}
		} else {
			keys = Lists.newArrayList();
			Set<T> original = new HashSet<>(map.keySet());
			for (TagKey<T> key : getTags(clazz)) {
				List<T> values = (List<T>) TAG_VALUES.get(key);
				if (values.size() < 2) {
					continue;
				}
				if (map.keySet().containsAll(values)) {
					map.keySet().removeAll(values);
					keys.add(key);
				}
				if (map.isEmpty()) {
					break;
				}
			}
			CACHED_TAGS.put((Set) original, (List) keys);
		}

		if (keys == null || keys.isEmpty()) {
			return new ListEmiIngredient(stacks.stream().collect(Collectors.toList()), amount);
		} else if (map.isEmpty()) {
			if (keys.size() == 1) {
				return new TagEmiIngredient(keys.get(0), amount);
			} else {
				return new ListEmiIngredient(keys.stream().map(k -> new TagEmiIngredient(k, 1)).collect(Collectors.toList()), amount);
			}
		} else {
			return new ListEmiIngredient(com.rewindmc.retroemi.shim.java.List.of(map.values().stream().map(i -> i.copy().setAmount(1)).collect(Collectors.toList()),
					keys.stream().map(k -> new TagEmiIngredient(k, 1)).collect(Collectors.toList()))
				.stream().flatMap(a -> a.stream()).collect(Collectors.toList()), amount);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> List<TagKey<T>> getTags(Class<T> clazz) {
		return (List<TagKey<T>>) (clazz == Prototype.class ? SORTED_ITEM_TAGS : Collections.emptyList());
	}

	public static Text getTagName(TagKey<?> key) {
		String s = getTagTranslationKey(key);
		if (s == null) {
			return EmiPort.literal("#" + key.id());
		} else {
			return EmiPort.translatable(s);
		}
	}

	public static boolean hasTranslation(TagKey<?> key) {
		return getTagTranslationKey(key) != null;
	}

	private static @Nullable String getTagTranslationKey(TagKey<?> key) {

		return translatePrefix(key.id());
	}

	private static @Nullable String translatePrefix(ResourceLocation id) {
		String s = EmiUtil.translateId("tag.", id);
		if (StringTranslate.getInstance().containsTranslateKey(s)) {
			return s;
		}
		return null;
	}

	public static @Nullable ResourceLocation getCustomModel(TagKey<?> key) {
		return EmiTags.MODELED_TAGS.get(key);
	}

	public static boolean hasCustomModel(TagKey<?> key) {
		return getCustomModel(key) != null;
	}

	public static void reload() {
		TAGS.clear();
		SORTED_ITEM_TAGS.clear();
		TAG_VALUES.clear();
		CACHED_TAGS.clear();
		reloadTags();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void reloadTags() {
		List<TagKey<Prototype>> itemTags = new ArrayList<>();
		var li = new ArrayList<ItemStack>();
        for (Object obj : Item.itemRegistry) {
            Item item = (Item) obj;
			if (item == null) continue;
			li.clear();
            item.getSubItems(item, item.getCreativeTab(), li);
			if (li.size() > 1) {
				itemTags.add(new WildcardItemTag(item));
			}
		}
		logUntranslatedTags(itemTags);

		itemTags = consolodateTags(itemTags);
		for (var key : itemTags) {
			TAG_VALUES.put(key, key.get());
		}
		var tags = itemTags.stream().collect(java.util.stream.Collectors.toList());
		EmiTags.TAGS.addAll(tags.stream().sorted(Comparator.comparing(Object::toString)).collect(java.util.stream.Collectors.toList()));
		itemTags = itemTags.stream()
			.sorted((a, b) -> Long.compare(b.get().size(), b.get().size()))
			.collect(Collectors.toList());
		EmiTags.SORTED_ITEM_TAGS.addAll(itemTags);
	}

	private static void logUntranslatedTags(List<? extends TagKey<?>> tags) {
		if (EmiConfig.logUntranslatedTags) {
			List<String> untranslated = Lists.newArrayList();
			for (TagKey<?> tag : tags) {
				if (!hasTranslation(tag)) {
					EmiLog.warn("WARNING Untranslated tag: " + EmiUtil.translateId("tag.", tag.id()));
					untranslated.add(tag.id().toString());
				}
			}
			if (!untranslated.isEmpty()) {
				for (String tag : untranslated.stream().sorted().collect(Collectors.toList())) {
					EmiReloadLog.warn("Untranslated tag " + tag);
				}
				EmiReloadLog.info(" Tag warning can be disabled in the config, EMI docs describe how to add a translation or exclude tags.");
			}
		}
	}

	private static <T> List<TagKey<T>> consolodateTags(List<TagKey<T>> tags) {
		Map<Set<T>, TagKey<T>> map = Maps.newHashMap();
		for (int i = 0; i < tags.size(); i++) {
			TagKey<T> key = tags.get(i);
			Set<T> values = new HashSet<>(key.get());
			TagKey<T> original = map.get(values);
			if (original != null) {
				map.put(values, betterTag(key, original));
			} else {
				map.put(values, key);
			}
		}
		return map.values().stream().collect(Collectors.toList());
	}

	private static<T> TagKey<T> betterTag(TagKey<T> a, TagKey<T> b) {
		if (hasTranslation(a) != hasTranslation(b)) {
			return hasTranslation(a) ? a : b;
		}
		if (hasCustomModel(a) != hasCustomModel(b)) {
			return hasCustomModel(a) ? a : b;
		}
		String an = a.id().getResourceDomain();
		String bn = b.id().getResourceDomain();
		if (!an.equals(bn)) {
			if (an.equals("wildcard")) {
				return a;
			}
			if (bn.equals("wildcard")) {
				return b;
			}
			if (an.equals("minecraft")) {
				return a;
			} else if (bn.equals("minecraft")) {
				return b;
			} else if (an.equals("c")) {
				return a;
			} else if (bn.equals("c")) {
				return b;
			} else if (an.equals("fabric")) {
				return EmiAgnos.isModLoaded("fabric") ? b : a;
			} else if (bn.equals("fabric")) {
				return EmiAgnos.isModLoaded("fabric") ? a : b;
			}
		}
		return a.id().toString().length() <= b.id().toString().length() ? a : b;
	}
}
