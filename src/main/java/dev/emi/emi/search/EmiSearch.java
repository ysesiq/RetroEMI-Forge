package dev.emi.emi.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.bom.BoM;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.data.EmiData;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.runtime.EmiReloadLog;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;
import com.rewindmc.retroemi.RetroEMI;
import net.minecraft.client.search.SuffixArray;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmiSearch {
	public static final Pattern TOKENS = Pattern.compile("(-?[@#]?\\/(\\\\.|[^\\\\\\/])+\\/|[^\\s]+)");
	private static volatile SearchWorker currentWorker = null;
	public static volatile Thread searchThread = null;
	public static volatile List<? extends EmiIngredient> stacks = EmiStackList.stacks;
	public static volatile CompiledQuery compiledQuery;
	public static Set<EmiStack> bakedStacks;
	public static SuffixArray<EmiStack> names, tooltips, mods, aliases, ids;

	public static <EmiAlias> void bake() {
		SuffixArray<EmiStack> names = new SuffixArray<EmiStack>();
		SuffixArray<EmiStack> tooltips = new SuffixArray<EmiStack>();
		SuffixArray<EmiStack> mods = new SuffixArray<EmiStack>();
		SuffixArray<EmiStack> aliases = new SuffixArray<EmiStack>();
		SuffixArray<EmiStack> ids = new SuffixArray<EmiStack>();
		Set<EmiStack> bakedStacks = Sets.newIdentityHashSet();
		boolean old = EmiConfig.appendItemModId;
		EmiConfig.appendItemModId = false;
		for (EmiStack stack : EmiStackList.stacks) {
			try {
				EmiStack strictStack = stack;
				bakedStacks.add(stack);
				Text name = NameQuery.getText(stack);
				if (name != null) {
					names.add(strictStack, name.getString().toLowerCase());
				}
				List<Text> tooltip = stack.getTooltipText();
				if (tooltip != null) {
					for (int i = 1; i < tooltip.size(); i++) {
						Text text = tooltip.get(i);
						if (text != null) {
							tooltips.add(strictStack, text.getString().toLowerCase());
						}
					}
				}
				ResourceLocation id = stack.getId();
				EmiRecipe recipe = BoM.getRecipe(stack);
				if (id != null) {
					mods.add(stack, GameData.findModOwner(GameData.itemRegistry.getNameForObject(stack.getItemStack())).getName().toLowerCase().replace(" ", ""));
					names.add(stack, id.toString());
				}
				//TODO search recipe id
				if (recipe != null) {
					names.add(stack, recipe.getId().toString());
				}
				String idString = "";
				if (Item.itemRegistry.getIDForObject(stack.getItemStack().getItem()) < 10) {
					idString = "000" + Item.itemRegistry.getIDForObject(stack.getItemStack().getItem());
				} else if (Item.itemRegistry.getIDForObject(stack.getItemStack().getItem()) < 100) {
					idString = "00" + Item.itemRegistry.getIDForObject(stack.getItemStack().getItem());
				} else if (Item.itemRegistry.getIDForObject(stack.getItemStack().getItem()) < 1000) {
					idString = "0" + Item.itemRegistry.getIDForObject(stack.getItemStack().getItem());
				} else {
					idString = String.valueOf(Item.itemRegistry.getIDForObject(stack.getItemStack().getItem()));
				}
				ids.add(stack, idString + "/" + stack.getItemStack().getItemDamage());
			} catch (Exception e) {
				EmiLog.error("EMI caught an exception while baking search for " + stack, e);
			}
		}
		for (Supplier<dev.emi.emi.data.EmiAlias> supplier : EmiData.aliases) {
			dev.emi.emi.data.EmiAlias alias = supplier.get();
			for (String key : alias.keys()) {
				if (!StringTranslate.getInstance().containsTranslateKey(key)) {
					EmiReloadLog.warn("Untranslated alias " + key);
				}
				String text = RetroEMI.translate(key).toLowerCase();
				for (EmiIngredient ing : alias.stacks()) {
					for (EmiStack stack : ing.getEmiStacks()) {
						aliases.add(stack.copy().comparison(Comparison.compareNbt()), text);
					}
				}
			}
		}
		EmiConfig.appendItemModId = old;
		names.build();
		tooltips.build();
		mods.build();
		aliases.build();
		ids.build();
		EmiSearch.names = names;
		EmiSearch.tooltips = tooltips;
		EmiSearch.mods = mods;
		EmiSearch.aliases = aliases;
		EmiSearch.bakedStacks = bakedStacks;
		EmiSearch.ids = ids;
	}

	public static void update() {
		search(EmiScreenManager.search.getText());
	}

	public static void search(String query) {
		synchronized (EmiSearch.class) {
			SearchWorker worker = new SearchWorker(query, EmiScreenManager.getSearchSource());
			currentWorker = worker;

			searchThread = new Thread(worker);
			searchThread.setDaemon(true);
			searchThread.start();
		}
	}

	public static void apply(SearchWorker worker, List<? extends EmiIngredient> stacks) {
		synchronized (EmiSearch.class) {
			if (worker == currentWorker) {
				EmiSearch.stacks = stacks;
				currentWorker = null;
				searchThread = null;
			}
		}
	}

	public static class CompiledQuery {
		public final Query fullQuery;

		public CompiledQuery(String query) {
			List<Query> full = Lists.newArrayList();
			List<Query> queries = Lists.newArrayList();
			Matcher matcher = TOKENS.matcher(query);
			while (matcher.find()) {
				String q = matcher.group();
				if (q.equals("|")) {
					if (!queries.isEmpty()) {
						full.add(new LogicalAndQuery(queries));
						queries = Lists.newArrayList();
					}
					continue;
				}
				boolean negated = q.startsWith("-");
				if (negated) {
					q = q.substring(1);
				}
				if (q.isEmpty()) {
					continue;
				}
				QueryType type = QueryType.fromString(q);
				Function<String, Query> constructor = type.queryConstructor;
				Function<String, Query> regexConstructor = type.regexQueryConstructor;
				if (type == QueryType.DEFAULT) {
					List<Function<String, Query>> constructors = Lists.newArrayList();
					List<Function<String, Query>> regexConstructors = Lists.newArrayList();
					constructors.add(constructor);
					regexConstructors.add(regexConstructor);

					if (EmiConfig.searchTooltipByDefault) {
						constructors.add(QueryType.TOOLTIP.queryConstructor);
						regexConstructors.add(QueryType.TOOLTIP.regexQueryConstructor);
					}
					if (EmiConfig.searchModNameByDefault) {
						constructors.add(QueryType.MOD.queryConstructor);
						regexConstructors.add(QueryType.MOD.regexQueryConstructor);
					}
					if (EmiConfig.searchTagsByDefault) {
						constructors.add(QueryType.TAG.queryConstructor);
						regexConstructors.add(QueryType.TAG.regexQueryConstructor);
					}
					if (EmiConfig.searchIdByDefault) {
						constructors.add(QueryType.ITEM_ID.queryConstructor);
						regexConstructors.add(QueryType.ITEM_ID.regexQueryConstructor);
					}
					// TODO add config
					constructors.add(AliasQuery::new);
					if (constructors.size() > 1) {
						constructor = name -> new LogicalOrQuery(constructors.stream().map(c -> c.apply(name)).collect(Collectors.toList()));
						regexConstructor = name -> new LogicalOrQuery(regexConstructors.stream().map(c -> c.apply(name)).collect(Collectors.toList()));
					}
				}
				addQuery(q.substring(type.prefix.length()), negated, queries, constructor, regexConstructor);
			}
			if (!queries.isEmpty()) {
				full.add(new LogicalAndQuery(queries));
			}
			if (!full.isEmpty()) {
				fullQuery = new LogicalOrQuery(full);
			}
			else {
				fullQuery = null;
			}
		}

		public boolean isEmpty() {
			return fullQuery == null;
		}

		public boolean test(EmiStack stack) {
			if (fullQuery == null) {
				return true;
			} else if (EmiSearch.bakedStacks.contains(stack)) {
				return fullQuery.matches(stack);
			} else {
				return fullQuery.matchesUnbaked(stack);
			}
		}

		private static void addQuery(String s, boolean negated, List<Query> queries, Function<String, Query> normal, Function<String, Query> regex) {
			Query q;
			if (s.length() > 1 && s.startsWith("/") && s.endsWith("/")) {
				q = regex.apply(s.substring(1, s.length() - 1));
			}
			else {
				q = normal.apply(s);
			}
			q.negated = negated;
			queries.add(q);
		}
	}

	private static class SearchWorker implements Runnable {
		private final String query;
		private final List<? extends EmiIngredient> source;

		public SearchWorker(String query, List<? extends EmiIngredient> source) {
			this.query = query;
			this.source = source;
		}

		@Override
		public void run() {
			try {
				CompiledQuery compiled = new CompiledQuery(query);
				compiledQuery = compiled;
				if (compiled.isEmpty()) {
					apply(this, source);
					return;
				}
				List<EmiIngredient> stacks = Lists.newArrayList();
				int processed = 0;
				for (EmiIngredient stack : source) {
					if (processed++ >= 1024) {
						processed = 0;
						if (this != currentWorker) {
							return;
						}
					}
					List<EmiStack> ess = stack.getEmiStacks();
					if (ess.size() == 1) {
						EmiStack es = ess.get(0);
						if (compiled.test(es)) {
							stacks.add(stack);
						}
					}
				}
				apply(this, Lists.newArrayList(stacks));
			} catch (Exception e) {
				EmiLog.error("Error when attempting to search:");
				e.printStackTrace();
			}
		}
	}
}
