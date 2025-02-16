package com.rewindmc.retroemi;

import net.minecraft.item.ItemStack;

public class ItemStacks {

	public static final ItemStack EMPTY = null;

	public static boolean isEmpty(ItemStack stack) {
		return stack == null || stack.stackSize == 0 || stack.getItem().delegate == null;
	}

}
