package com.schwada.liege.graphics;

import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Class provides a means of rasterization for PixelBuffer as it does
 * not have one. AWT Image could be used in order to leverage the
 * Graphics2D API for rasterization instead of drawing
 * shapes and text manually. Both methods are done on the CPU, however
 * the drawing algorithm could also instead be
 * moved to GPU to drastically improve performance. ( for example https://github.com/Syncleus/aparapi)
 *
 * @author Schwada
 */
public class Renderer {
    // TODO: Load all fonts and change on demand instead of hardcoding
    private Font font = Font.ARIAL_8;

    private Pane scene;

    // FIXME: Get screen size dynamically instead of being hardcoded
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    /** Resolution **/
    public static final float SCALE = 1.7f;

    public static int WIDTH = (int) (SCREEN_WIDTH / SCALE);
    public static int HEIGHT = (int) (SCREEN_HEIGHT / SCALE);

    /**  Number of buffers to draw into. Must be at least 2  **/
    private static final int BUFFER_SIZE = 5;

    /**  Stores background pixels in ARGB format.  **/
    private static final int[] EMPTY_SCREEN = new int[Renderer.WIDTH * Renderer.HEIGHT];

    /**  Stores fully drawn images.  **/
    private BlockingQueue<WritableImageView> fullBuffers = new ArrayBlockingQueue<>(BUFFER_SIZE);

    /** Stores images that can be drawn into **/
    private BlockingQueue<WritableImageView> emptyBuffers = new ArrayBlockingQueue<>(BUFFER_SIZE);

    /** Current visible buffer on the stage. **/
    private WritableImageView visibleBuffer;

    /** Current active buffer, being written into. **/
    private WritableImageView currentBuffer;


    public Renderer(Pane scene) {
        this.scene = scene;

        // fill the bg array with color black
        Arrays.fill(EMPTY_SCREEN, 0xff000000);


        // Create buffers from buffer size
        for (int i = 0; i < BUFFER_SIZE; i++) {
            WritableImageView imageView = new WritableImageView(WIDTH, HEIGHT);

            imageView.setSmooth(true);
            imageView.setFitWidth(SCREEN_WIDTH); // bind to screen size width bind
            imageView.setFitHeight(SCREEN_HEIGHT); // bind to screen size
            imageView.setFocusTraversable(false);
            emptyBuffers.add(imageView);
        }
    }


    /**
     *
     * @return
     */
    public WritableImageView getEmptyBuffer() {
        try {
            this.currentBuffer = emptyBuffers.take();
            return this.currentBuffer;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     */
    public void switchBuffers() {
        try {
            fullBuffers.add(this.currentBuffer);
            WritableImageView buffer = fullBuffers.take();

            scene.getChildren().add(buffer);

            if (visibleBuffer != null) {
                scene.getChildren().remove(visibleBuffer);
                emptyBuffers.add(visibleBuffer);
            }

            buffer.updateBuffer();
            visibleBuffer = buffer;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        this.currentBuffer.setPixels(EMPTY_SCREEN);
    }

    public void clear(WritableImageView buffer) {
        buffer.setPixels(EMPTY_SCREEN);
    }

    /**
     *
     * @param x
     * @param y
     * @param argb
     */
    public void setPixel(int x, int y, int argb) {
        this.currentBuffer.setArgb(x, y, argb);
    }

    public void setPixel(int x, int y, int argb, WritableImageView imageView) {
        imageView.setArgb(x, y, argb);
    }

    public void setPixels(int[] pixels) {
        this.currentBuffer.setPixels(pixels);
    }

    public void setPixels(int x, int y, int[] pixels, WritableImageView imageView) {
        imageView.setPixels(x,y,pixels);
    }

    public void fillRect(int offX, int offY, int w, int h, int argb) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                setPixel(x + offX, y + offY, argb);
            }
        }
    }

    /**
     * Draws a rect on screen by inserting a buffered
     * rows of rect through System.arraycopy.
     *
     * @param offX
     * @param offY
     * @param w
     * @param h
     * @param argb
     * @param imageView
     */
    public void fillRect(int offX, int offY, int w, int h, int argb, WritableImageView imageView) {
        if(offX < 0 || offX >= WIDTH || offY < 0 || offY  >= HEIGHT) return;
        int[] rect = new int[w];
        Arrays.fill(rect, argb);

        for (int i = 0; i <= h; i++) {
            // FIXME: still creates missing columns of pixels on edges of screen
            if(offX + w < WIDTH) {
                this.setPixels(0, (offY + i) * (WIDTH) + (offX), rect, imageView);
            }
        }
    }

    public void drawLine(int fromX, int fromY, int toX, int toY, int color) {
        int dy = toY-fromY;
        int dx = toX-fromX;

        double k = (double) dy / dx;
        double y = fromY;

        for( int x = fromX; x <= toX; x++ ){
            this.setPixel((int)Math.round(y), (int) x, color);
            y = y + k;
        }

    }

    public void drawString(String text, int offX, int offY, int color) {
        int offset = 0;
        for(int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i);		// -32 will make space = 0
            for(int y = 0; y < font.getFontImage().getHeight(); y++) {
                for(int x = 0; x < font.getWidths()[unicode]; x++) {
                    if(font.getPixels()[(x + font.getOffsets()[unicode]) + y * (int)font.getFontImage().getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }
    }

}
