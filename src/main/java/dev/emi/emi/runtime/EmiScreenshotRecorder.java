package dev.emi.emi.runtime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import dev.emi.emi.EmiPort;
import dev.emi.emi.config.EmiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;

public class EmiScreenshotRecorder {
    private static final String SCREENSHOTS_DIRNAME = "screenshots";

    /**
     * Saves a screenshot to the game's `screenshots` directory, doing the appropriate setup so that anything rendered in renderer will be captured
     * and saved.
     * <p>
     * <b>Note:</b> the path can have <code>/</code> characters, indicating subdirectories. Java handles these correctly on Windows. The path should
     * <b>not</b> contain the <code>.png</code> extension, as that will be added after checking for duplicates. If a file with this path already
     * exists, then path will be suffixed with a <code>_#</code>, before adding the <code>.png</code> extension, where <code>#</code> represents an
     * increasing number to avoid conflicts.
     * <p>
     * <b>Note 2:</b> The width and height parameters are reflected in the viewport when rendering. But the EMI-config
     * <code>ui.recipe-screenshot-scale</code> value causes the resulting image to be scaled.
     *
     * @param path     the path to save the screenshot to, without extension.
     * @param width    the width of the screenshot, not counting EMI-config scale.
     * @param height   the height of the screenshot, not counting EMI-config scale.
     * @param renderer a function to render the things being screenshotted.
     */
    public static void saveScreenshot(String path, int x, int y, int width, int height, Runnable renderer) {
        Minecraft client = Minecraft.getMinecraft();
        int scale;
        if (EmiConfig.recipeScreenshotScale < 1) {
            scale = EmiPort.getGuiScale(client);
        } else {
            scale = EmiConfig.recipeScreenshotScale;
        }

        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_TRANSFORM_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();

        try {
            GL11.glViewport(0, 0, width * scale, height * scale);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, height, 0, 1000.0D, 3000.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

            renderer.run();

            int bufferSize = width * height;
            IntBuffer pixelBuffer = BufferUtils.createIntBuffer(bufferSize);
            int[] pixelValues = new int[bufferSize];

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glReadPixels(x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            pixelBuffer.get(pixelValues);

            saveScreenshotInner(client.mcDataDir, path, pixelValues, width, height, message -> client.ingameGUI.getChatGUI().printChatMessage((IChatComponent) message));
        } finally {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            client.entityRenderer.updateRenderer();
        }
    }

    private static void saveScreenshotInner(File gameDirectory, String suggestedPath, int[] pixels, int width, int height, Consumer<Text> messageReceiver) {
        int[] flipped = new int[pixels.length];
        for (int row = 0; row < height; row++) {
            System.arraycopy(pixels, row * width, flipped, (height - 1 - row) * width, width);
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, flipped, 0, width);

        File screenshotDir = new File(gameDirectory, "screenshots");
        screenshotDir.mkdir();

        String filename = getScreenshotFilename(screenshotDir, suggestedPath);
        File file = new File(screenshotDir, filename);

        try {
            ImageIO.write(image, "PNG", file);
            Text text = EmiPort.literal(filename,
                    Style.EMPTY.withUnderline(true)
//                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath()))
            );
            messageReceiver.accept(EmiPort.translatable("screenshot.success", text));
        } catch (IOException e) {
            EmiLog.error("Failed to write screenshot");
            e.printStackTrace();
            messageReceiver.accept(EmiPort.translatable("screenshot.failure", e.getMessage()));
        }
    }

    private static String getScreenshotFilename(File directory, String path) {
        int i = 1;
        while ((new File(directory, path + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
            ++i;
        }
        return path + (i == 1 ? "" : "_" + i) + ".png";
    }
}

