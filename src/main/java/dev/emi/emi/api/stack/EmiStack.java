package dev.emi.emi.api.stack;

import com.google.common.collect.Lists;
import dev.emi.emi.Prototype;
import dev.emi.emi.registry.EmiComparisonDefaults;
import dev.emi.emi.screen.tooltip.RemainderTooltipComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import com.rewindmc.retroemi.ItemStacks;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * An abstract representation of a resource in EMI.
 * Can be an item, a fluid, or something else.
 */
public abstract class EmiStack implements EmiIngredient {
	public static final EmiStack EMPTY = new EmptyEmiStack();
	private EmiStack remainder = EMPTY;
	protected Comparison comparison = Comparison.DEFAULT_COMPARISON;
	protected long amount = 1;
	protected float chance = 1;

    @Override
	public List<EmiStack> getEmiStacks() {
		return Collections.singletonList(this);
	}

	public EmiStack getRemainder() {
		return remainder;
	}

	public EmiStack setRemainder(EmiStack stack) {
		if (stack == this) {
			stack = stack.copy();
		}
		remainder = stack;
		return this;
	}

	public EmiStack comparison(Function<Comparison, Comparison> comparison) {
		this.comparison = comparison.apply(this.comparison);
		return this;
	}

	public EmiStack comparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}

	public abstract EmiStack copy();

	public abstract boolean isEmpty();

	public long getAmount() {
		return amount;
	}

	public EmiStack setAmount(long amount) {
		this.amount = amount;
		return this;
	}

	public float getChance() {
		return chance;
	}

	public EmiStack setChance(float chance) {
		this.chance = chance;
		return this;
	}

	public abstract NBTTagCompound getNbt();

	public boolean hasNbt() {
		return getNbt() != null;
	}

	public abstract Object getKey();

	@SuppressWarnings("unchecked")
	public <T> @Nullable T getKeyOfType(Class<T> clazz) {
		Object o = getKey();
		if (clazz.isAssignableFrom(o.getClass())) {
			return (T) o;
		}
		return null;
	}

	public abstract ResourceLocation getId();

	public ItemStack getItemStack() {
		return ItemStacks.EMPTY;
	}

	public boolean isEqual(EmiStack stack) {
		if (stack.getClass() != getClass()) {
			return false;
		}
		var ka = getKey();
		var kb = stack.getKey();
		if (ka instanceof Prototype p) {
			ka = p.getItem();
		}
		if (kb instanceof Prototype p) {
			kb = p.getItem();
		}
		if (!Objects.equals(ka, kb)) {
			return false;
		}
		Comparison a = comparison == Comparison.DEFAULT_COMPARISON ? EmiComparisonDefaults.get(getKey()) : comparison;
		Comparison b = stack.comparison == Comparison.DEFAULT_COMPARISON ? EmiComparisonDefaults.get(stack.getKey()) : stack.comparison;
		if (a == b) {
			return a.compare(this, stack);
		}
		else {
			return a.compare(this, stack) && b.compare(this, stack);
		}
	}

	public boolean isEqual(EmiStack stack, Comparison comparison) {
		if (stack.getClass() != getClass()) {
			return false;
		}
		var ka = getKey();
		var kb = stack.getKey();
		if (ka instanceof Prototype p) {
			ka = p.getItem();
		}
		if (kb instanceof Prototype p) {
			kb = p.getItem();
		}
		if (!Objects.equals(ka, kb)) {
			return false;
		}
		return comparison.compare(this, stack);
	}

	public abstract List<Text> getTooltipText();

	public List<TooltipComponent> getTooltip() {
		List<TooltipComponent> list = Lists.newArrayList();
		if (!getRemainder().isEmpty()) {
			list.add(new RemainderTooltipComponent(this));
		}
		return list;
	}

	public abstract Text getName();

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EmiStack stack) {
			return this.isEqual(stack);
		}
		else if (obj instanceof EmiIngredient stack) {
			return EmiIngredient.areEqual(this, stack);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode() ^ (getKey() == null ? 0 : getKey().hashCode());
	}

	@Override
	public String toString() {
		String s = "" + getKey();
		NBTTagCompound nbt = getNbt();
		if (nbt != null) {
			s += nbt;
		}
		return s + " x" + getAmount();
	}

	public static EmiStack of(Prototype proto) {
		return new ItemEmiStack(proto.toStack());
	}

	public static EmiStack of(ItemStack stack) {
		if (ItemStacks.isEmpty(stack)) {
			return EmiStack.EMPTY;
		}
		return new ItemEmiStack(stack);
	}

	public static EmiStack of(ItemStack stack, long amount) {
		if (ItemStacks.isEmpty(stack)) {
			return EmiStack.EMPTY;
		}
		return new ItemEmiStack(stack, amount);
	}

	public static EmiStack of(Item item) {
		if (item == null) {
			return EMPTY;
		}
		return of(new ItemStack(item), 1);
	}

	public static EmiStack of(Item item, long amount) {
		if (item == null) {
			return EMPTY;
		}
		return of(new ItemStack(item), amount);
	}

	public static EmiStack of(DyeItem i) {
		return of(i.toStack());
	}

	public static EmiStack of(Block block) {
		if (block == null) {
			return EMPTY;
		}
		return of(new ItemStack(block), 1);
	}

	public static EmiStack of(Block block, long amount) {
		if (block == null) {
			return EMPTY;
		}
		return of(new ItemStack(block), amount);
	}

	static abstract class Entry<T> {
	}
}
