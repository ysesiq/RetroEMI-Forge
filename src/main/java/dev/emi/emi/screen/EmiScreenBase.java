package dev.emi.emi.screen;//package dev.emi.emi.screen;
//import dev.emi.emi.api.widget.Bounds;
//import net.minecraft.GuiScreen;
//import net.minecraft.Minecraft;
//
//public class EmiScreenBase {
//    private final GuiScreen screen;
//    private final Bounds bounds;
//    private EmiScreenBase(GuiScreen screen, Bounds bounds) {
//        this.screen = screen;
//        this.bounds = bounds;
//    }
//    public GuiScreen screen() {
//        return screen;
//    }
//    public Bounds bounds() {
//        return bounds;
//    }
//
//    public static EmiScreenBase getCurrent() {
//        Minecraft client = Minecraft.getMinecraft();
//        GuiScreen screen = client.currentScreen;
//        if (screen instanceof HandledScreen hs) {
//            HandledScreenAccessor hsa = (HandledScreenAccessor) hs;
//            ScreenHandler sh = hs.getScreenHandler();
//            if (sh.slots != null && !sh.slots.isEmpty()) {
//                int extra = 0;
//                if (hs instanceof RecipeBookProvider provider) {
//                    if (provider.getRecipeBookWidget().isOpen()) {
//                        extra = 177;
//                    }
//                }
//                Bounds bounds = new Bounds(hsa.getX() - extra, hsa.getY(), hsa.getBackgroundWidth() + extra, hsa.getBackgroundHeight());
//                return new EmiScreenBase(screen, bounds);
//            }
//        } else if (screen instanceof RecipeScreen rs) {
//            return new EmiScreenBase(rs, rs.getBounds());
//        }
//        return null;
//    }
//}
