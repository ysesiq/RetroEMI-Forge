package dev.emi.emi.api.widget;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.gui.DrawContext;

public class MultiBlockWidget extends Widget { // I doubt anything will come of this, mostly just messing about trying to make pretty kiln rendering

	protected int x;
	protected int y;
	protected RenderBlocks renderBlocks = new RenderBlocks();

	public MultiBlockWidget(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Bounds getBounds() {
		return new Bounds(x, y, 18, 18);
	}

	@Override
	public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
		var stack = draw.getMatrices();
//		Block.brick.renderBlock(renderBlocks, 10, 10, 10);
	}

}
