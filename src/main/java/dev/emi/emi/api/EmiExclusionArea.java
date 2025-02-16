package dev.emi.emi.api;

import dev.emi.emi.screen.Bounds;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.Consumer;

public interface EmiExclusionArea<T extends GuiScreen> {

	void addExclusionArea(T screen, Consumer<Bounds> consumer);
}
