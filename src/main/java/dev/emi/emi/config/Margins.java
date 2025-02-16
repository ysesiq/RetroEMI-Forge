package dev.emi.emi.config;

import java.util.List;

public class Margins extends IntGroup {

	public Margins(int top, int right, int bottom, int left) {
		super("emi.sidebar.margins.", com.rewindmc.retroemi.shim.java.List.of("top", "right", "bottom", "left"), com.rewindmc.retroemi.shim.java.List.of(top, right, bottom, left));
	}

	public int top() {
		return values.get(0);
	}

	public int right() {
		return values.get(1);
	}

	public int bottom() {
		return values.get(2);
	}

	public int left() {
		return values.get(3);
	}
}
