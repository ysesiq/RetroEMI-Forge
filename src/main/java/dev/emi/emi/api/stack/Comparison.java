package dev.emi.emi.api.stack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Comparison {
	public static final Comparison DEFAULT_COMPARISON = Comparison.of((a, b) -> getEffectiveMeta(a.getItemStack()) == getEffectiveMeta(b.getItemStack()));
	private static final Comparison COMPARE_NBT = Comparison.of((a, b) -> {
		if (!DEFAULT_COMPARISON.compare(a, b)) {
			return false;
		}
		NBTTagCompound an = a.getNbt();
		NBTTagCompound bn = b.getNbt();
		if (an == null || bn == null) {
			return an == bn;
		}
		else {
			return an.equals(bn);
		}
	});
	public static final Comparison LAX = Comparison.of((a, b) -> true);

	private static int getEffectiveMeta(ItemStack is) {
		if (is == null) {
			return 0;
		}
		if (!is.getItem().getHasSubtypes() || is.isItemStackDamageable()) {
			return 0;
		}
		return is.getItemDamage();
	}


	private final Predicate predicate;

	private Comparison(Predicate comparator) {
		this.predicate = comparator;
	}

	public static Comparison of(Predicate comparator) {
		return new Comparison(comparator);
	}

	public static Comparison compareNbt() {
		return COMPARE_NBT;
	}

	public boolean compare(EmiStack a, EmiStack b) {
		return predicate.test(a, b);
	}

	public static interface Predicate {
		public boolean test(EmiStack a, EmiStack b);
	}

	static class Builder {
	}
}
