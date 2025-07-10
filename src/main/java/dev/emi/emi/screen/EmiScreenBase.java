package dev.emi.emi.screen;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.mixin.early.minecraft.accessor.GuiContainerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class EmiScreenBase {
    private final GuiScreen screen;
    private final Bounds bounds;

    private EmiScreenBase(GuiScreen screen, Bounds bounds) {
        this.screen = screen;
        this.bounds = bounds;
    }

    public GuiScreen screen() {
        return screen;
    }

    public Bounds bounds() {
        return bounds;
    }

    public boolean isEmpty() {
        return screen == null;
    }

    public static EmiScreenBase getCurrent() {
        Minecraft client = Minecraft.getMinecraft();
        return of(client.currentScreen);
    }

    public static EmiScreenBase of(GuiScreen screen) {
        if (screen instanceof GuiContainer hs) {
            GuiContainerAccessor hsa = (GuiContainerAccessor) hs;
//            if (hsa.getTheSlot() != null && hsa.getTheSlot().getHasStack()) {
                int extra = 0;
//                if (hs instanceof RecipeBookProvider provider) {
//                    if (provider.getRecipeBookWidget().isOpen()) {
//                        extra = 177;
//                    }
//                }
                Bounds bounds = new Bounds(hsa.getGuiLeft() - extra, hsa.getYSize(), hsa.getXSize() + extra, hsa.getGuiTop());
                return new EmiScreenBase(screen, bounds);
//            }
        } else if (screen instanceof RecipeScreen rs) {
            return new EmiScreenBase(rs, rs.getBounds());
        }
        return new EmiScreenBase(null, Bounds.EMPTY);
    }
}
