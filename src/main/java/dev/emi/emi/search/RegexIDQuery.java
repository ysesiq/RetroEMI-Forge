package dev.emi.emi.search;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.Item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexIDQuery extends Query {
	private final Pattern pattern;

	public RegexIDQuery(String id) {
		Pattern p = null;
		try {
			p = Pattern.compile(id, Pattern.CASE_INSENSITIVE);
		}
		catch (Exception e) {
		}
		pattern = p;
	}

	@Override
	public boolean matches(EmiStack stack) {
		if (pattern == null) {
			return false;
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
		String id = idString + "/" + stack.getItemStack().getItemDamage();
		Matcher m = pattern.matcher(id);
		return m.find();
	}
}
