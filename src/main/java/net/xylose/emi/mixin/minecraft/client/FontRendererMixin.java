package net.xylose.emi.mixin.minecraft.client;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.Style;

import java.util.Random;

@Mixin(value = FontRenderer.class, priority = 2000, remap = false)
public abstract class FontRendererMixin {
    @Shadow private int textColor;
    @Shadow private float alpha;
    @Shadow private boolean randomStyle;
    @Shadow private boolean boldStyle;
    @Shadow private boolean strikethroughStyle;
    @Shadow private boolean underlineStyle;
    @Shadow private boolean italicStyle;
    @Shadow protected float posX;
    @Shadow protected float posY;
    @Shadow protected abstract float renderCharAtPos(int par1, char par2, boolean par3);
    @Shadow protected void setColor(float r, float g, float b, float a) {}
    @Shadow protected void doDraw(float f) {}
    @Shadow private float red;
    @Shadow private float blue;
    @Shadow private float green;
    @Shadow public Random fontRandom;
    @Shadow protected int[] charWidth;
    @Shadow private boolean unicodeFlag;
    @Shadow private int[] colorCode;

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 256))
    private int modifyChanceTableSize(int val) {
        return Short.MAX_VALUE;
    }

    /**
     * Hook for EMI
     */
    @Unique
    public int applyCustomFormatCodes(String str, boolean shadow, int i) {
        if (str.charAt(i + 1) == 'x') {
            int next = str.indexOf(String.valueOf('\u00a7') + "x", i + 1);
            if (next != -1) {
                String s = str.substring(i + 1, next);
                int color = Integer.parseInt(s.replace(String.valueOf('\u00a7'), "").substring(1), 16);
                if (shadow) {
                    color = (color & 16579836) >> 2 | color & -16777216;
                }
                this.textColor = color;
                GL11.glColor4f((color >> 16) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, this.alpha);
                i += s.length() + 1;
            }
        }
        return i;
    }

//    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
//    private void applyCustomFormatCodes(String par1Str, boolean par2, CallbackInfo ci, int var3, char var4, int var6, EnumChatFormatting enum_chat_formatting) {
//        var3 = this.applyCustomFormatCodes(par1Str, par2, var3);
//    }

//    private void applyCustomFormatCodes(String par1Str, boolean par2, CallbackInfo ci) {
//        if (Style.EMPTY != null)
//            ci.cancel();
//        for (int var3 = 0; var3 < par1Str.length(); ++var3) {
//            char var4 = par1Str.charAt(var3);
//            int var6;
//
//            if (var4 == 167 && var3 + 1 < par1Str.length()) {
//                EnumChatFormatting var12 = EnumChatFormatting.getByChar(par1Str.toLowerCase().charAt(var3 + 1));
//
//                if (var12 == null) {
//                    var12 = EnumChatFormatting.WHITE;
//                }
//
//                if (var12.isColor()) {
//                    this.randomStyle = false;
//                    this.boldStyle = false;
//                    this.strikethroughStyle = false;
//                    this.underlineStyle = false;
//                    this.italicStyle = false;
//
//                    if (Minecraft.getMinecraft().gameSettings.anaglyph) {
//                        var6 = par2 ? var12.rgb_anaglyph_shadow : var12.rgb_anaglyph;
//                    } else {
//                        var6 = par2 ? var12.rgb_shadow : var12.rgb;
//                    }
//
//                    this.textColor = var6;
//                    GL11.glColor4f((float) (var6 >> 16) / 255.0F, (float) (var6 >> 8 & 255) / 255.0F, (float) (var6 & 255) / 255.0F, this.alpha);
//                    var3 = applyCustomFormatCodes(par1Str, par2, var3);
//                } else if (var12 == EnumChatFormatting.OBFUSCATED) {
//                    this.randomStyle = true;
//                } else if (var12 == EnumChatFormatting.BOLD) {
//                    this.boldStyle = true;
//                } else if (var12 == EnumChatFormatting.STRIKETHROUGH) {
//                    this.strikethroughStyle = true;
//                } else if (var12 == EnumChatFormatting.UNDERLINE) {
//                    this.underlineStyle = true;
//                } else if (var12 == EnumChatFormatting.ITALIC) {
//                    this.italicStyle = true;
//                } else if (var12 == EnumChatFormatting.RESET) {
//                    this.randomStyle = false;
//                    this.boldStyle = false;
//                    this.strikethroughStyle = false;
//                    this.underlineStyle = false;
//                    this.italicStyle = false;
//                    GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
//                }
//
//                ++var3;
//            } else {
//                int var5 = ChatAllowedCharacters.allowedCharacters.indexOf(var4);
//
//                if (this.randomStyle && var5 > 0) {
//                    do {
//                        var6 = this.fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
//                    }
//                    while (this.charWidth[var5 + 32] != this.charWidth[var6 + 32]);
//
//                    var5 = var6;
//                }
//
//                float var7 = this.unicodeFlag ? 0.5F : 1.0F;
//                boolean var8 = (var5 <= 0 || this.unicodeFlag) && par2;
//
//                if (var8) {
//                    this.posX -= var7;
//                    this.posY -= var7;
//                }
//
//                float var9 = this.renderCharAtPos(var5, var4, this.italicStyle);
//
//                if (var8) {
//                    this.posX += var7;
//                    this.posY += var7;
//                }
//
//                if (this.boldStyle) {
//                    this.posX += var7;
//
//                    if (var8) {
//                        this.posX -= var7;
//                        this.posY -= var7;
//                    }
//
//                    this.renderCharAtPos(var5, var4, this.italicStyle);
//                    this.posX -= var7;
//
//                    if (var8) {
//                        this.posX += var7;
//                        this.posY += var7;
//                    }
//
//                    ++var9;
//                }
//
//                Tessellator var10;
//
//                if (this.strikethroughStyle) {
//                    var10 = Tessellator.instance;
//                    GL11.glDisable(GL11.GL_TEXTURE_2D);
//                    var10.startDrawingQuads();
//                    var10.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
//                    var10.addVertex((double) (this.posX + var9), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
//                    var10.addVertex((double) (this.posX + var9), (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
//                    var10.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
//                    var10.draw();
//                    GL11.glEnable(GL11.GL_TEXTURE_2D);
//                }
//
//                if (this.underlineStyle) {
//                    var10 = Tessellator.instance;
//                    GL11.glDisable(GL11.GL_TEXTURE_2D);
//                    var10.startDrawingQuads();
//                    int var11 = this.underlineStyle ? -1 : 0;
//                    var10.addVertex((double) (this.posX + (float) var11), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
//                    var10.addVertex((double) (this.posX + var9), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
//                    var10.addVertex((double) (this.posX + var9), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
//                    var10.addVertex((double) (this.posX + (float) var11), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
//                    var10.draw();
//                    GL11.glEnable(GL11.GL_TEXTURE_2D);
//                }
//
//                this.posX += (float) ((int) var9);
//            }
//        }
//    }

    @Inject(method = "renderStringAtPos", at = @At("HEAD"), cancellable = true)
    private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_, CallbackInfo ci) {
        if (Style.EMPTY != null)
            ci.cancel();
        for (int i = 0; i < p_78255_1_.length(); ++i) {
            char c0 = p_78255_1_.charAt(i);
            int j;
            int k;

            if (c0 == 167 && i + 1 < p_78255_1_.length()) {
                j = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));

                if (j < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (j < 0 || j > 15) {
                        j = 15;
                    }

                    if (p_78255_2_) {
                        j += 16;
                    }

                    k = this.colorCode[j];
                    this.textColor = k;
                    setColor((float) (k >> 16) / 255.0F, (float) (k >> 8 & 255) / 255.0F, (float) (k & 255) / 255.0F, this.alpha);
                    i = applyCustomFormatCodes(p_78255_1_, p_78255_2_, i);
                } else if (j == 16) {
                    this.randomStyle = true;
                } else if (j == 17) {
                    this.boldStyle = true;
                } else if (j == 18) {
                    this.strikethroughStyle = true;
                } else if (j == 19) {
                    this.underlineStyle = true;
                } else if (j == 20) {
                    this.italicStyle = true;
                } else if (j == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1) {
                    do {
                        k = this.fontRandom.nextInt(this.charWidth.length);
                    }
                    while (this.charWidth[j] != this.charWidth[k]);

                    j = k;
                }

                float f1 = this.unicodeFlag ? 0.5F : 1.0F;
                boolean flag1 = (c0 == 0 || j == -1 || this.unicodeFlag) && p_78255_2_;

                if (flag1) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderCharAtPos(j, c0, this.italicStyle);

                if (flag1) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;

                    if (flag1) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderCharAtPos(j, c0, this.italicStyle);
                    this.posX -= f1;

                    if (flag1) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }

                doDraw(f);
            }
        }
    }
}
