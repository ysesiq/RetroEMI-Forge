package net.minecraft.text;

import net.minecraft.util.Formatting;

public class Style {

	public static final Style EMPTY = new Style("");

	private final String formats;

	public Style(String formats) {
		this.formats = formats;
	}

	public Style withUnderline(boolean underline) {
		if (underline) return new Style(formats + Formatting.UNDERLINE);
		return this;
	}

	public Style withColor(int color) {
		return new Style(formats+"§x"+(Integer.toHexString(color|0xFF000000).substring(2).replace("", "§"))+"x");
	}

	public Style withFormatting(Formatting f) {
		return new Style(formats+f);
	}

	@Override
	public String toString() {
		return formats;
	}

}
