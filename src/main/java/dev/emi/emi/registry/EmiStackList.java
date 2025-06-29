package dev.emi.emi.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.emi.emi.data.EmiAlias;
import dev.emi.emi.data.EmiRemoveFromIndex;
import dev.emi.emi.data.IndexStackData;
import dev.emi.emi.runtime.EmiHidden;
import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmiStackList {
    public static List<EmiAlias.Baked> registryAliases = Lists.newArrayList();
    public static List<Predicate<EmiStack>> invalidators = Lists.newArrayList();
    public static List<EmiStack> stacks = Collections.emptyList();
    public static List<EmiStack> filteredStacks = Collections.emptyList();
    public static Object2IntMap<EmiStack> indices = new Object2IntOpenHashMap<>();

    public static void clear() {
        invalidators.clear();
        registryAliases.clear();
        stacks = Collections.emptyList();
        indices.clear();
    }

    public static void reload() {
        List<IndexGroup> groups = Lists.newArrayList();
        IndexGroup itemGroup = new IndexGroup();
        List<ItemStack> stackList = Lists.newArrayList();

        for (Object obj : Item.itemRegistry) {
            Item item = (Item) obj;
            if (item == null) continue;
            item.getSubItems(item, item.getCreativeTab(), stackList);
            for (ItemStack is : stackList) {
                itemGroup.stacks.add(EmiStack.of(is));
            }
            stackList.clear();
        }
        groups.add(itemGroup);

        IndexGroup fluidGroup = new IndexGroup();
        //	for (var fluid : LiquidDictionary.getLiquids().entrySet()) {
        //		EmiStack fs = EmiStack.of(Fluid.of(fluid.getValue()));
        //		fluidGroup.stacks.add(fs);
        //	}
        groups.add(fluidGroup);

        Set<EmiStack> added = Sets.newHashSet();

        stacks = Lists.newLinkedList();
        for (IndexGroup group : groups) {
            if (group.shouldDisplay()) {
                for (EmiStack stack : group.stacks) {
                    if (!stack.isEmpty() && !added.contains(stack)) {
                        stacks.add(stack);
                        added.add(stack.copy().comparison(Comparison.compareNbt()));
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void bake() {
        stacks.removeIf(s -> {
            try {
                if (s.isEmpty()) {
                    return true;
                }
                for (Predicate<EmiStack> invalidator : invalidators) {
                    if (invalidator.test(s)) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable t) {
                EmiLog.error("Stack threw error while baking", t);
                return true;
            }
        });

        IndexStackData ssd = EmiRemoveFromIndex.entries;
        if (!ssd.removed().isEmpty()) {
            Set<EmiStack> removed = Sets.newHashSet();
            for (EmiIngredient invalidator : ssd.removed()) {
                for (EmiStack stack : invalidator.getEmiStacks()) {
                    removed.add(stack.copy().comparison(c -> Comparison.compareNbt()));
                }
            }
            stacks.removeAll(removed);
        }

        for (IndexStackData.Added added : ssd.added()) {
            if (added.added().isEmpty()) {
                continue;
            }
            if (added.after().isEmpty()) {
                stacks.add(added.added().getEmiStacks().get(0));
            } else {
                int i = stacks.indexOf(added.after());
                if (i == -1) {
                    i = stacks.size() - 1;
                }
                stacks.add(i + 1, added.added().getEmiStacks().get(0));
            }
        }

        stacks = stacks.stream().filter(stack -> {
            String name = "Unknown";
            String id = "unknown";
            try {
                if (stack.isEmpty()) {
                    return false;
                }
                name = stack.toString();
                id = stack.getId().toString();
                if (name != null && stack.getKey() != null && stack.getName() != null && stack.getTooltip() != null) {
                    return true;
                }
                EmiLog.warn("Hiding stack " + name + " with id " + id + " from index due to returning dangerous values");
                return false;
            } catch (Throwable t) {
                EmiLog.warn("Hiding stack " + name + " with id " + id + " from index due to throwing errors");
                t.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
        for (int i = 0; i < stacks.size(); i++) {
            indices.put(stacks.get(i), i);
        }
        bakeFiltered();
    }

    public static void bakeFiltered() {
        filteredStacks = stacks.stream().filter(s -> !EmiHidden.isHidden(s)).collect(Collectors.toList());
    }

    public static class IndexGroup {
        public List<EmiStack> stacks = Lists.newArrayList();
        public Set<IndexGroup> suppressedBy = Sets.newHashSet();

        public boolean shouldDisplay() {
            for (IndexGroup suppressor : suppressedBy) {
                if (suppressor.shouldDisplay()) {
                    return false;
                }
            }
            return true;
        }
    }
}
