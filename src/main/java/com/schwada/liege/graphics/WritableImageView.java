package com.schwada.liege.graphics;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.nio.IntBuffer;

/**
 * Image on which a PixelBuffer for individual pixels are displayed.
 * We want to show a huge number of individual pixels as quickly
 * as possible, this is much faster than using a Canvas, only being able
 * to use one thread in its graphics context at a time.
 */
public class WritableImageView extends ImageView {

    private int width;
    private int height;
    private int[] rawInts;
    private IntBuffer buffer;
    private PixelBuffer<IntBuffer> pixelBuffer;


    public WritableImageView(int width, int height) {
        this.height = height;
        this.width = width;
        buffer = IntBuffer.allocate(width * height);
        rawInts = buffer.array();
        pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());
        setImage(new WritableImage(pixelBuffer));
    }


    /**
     * Overrides the default behaviour of a JavaFX node
     * to prevent focus and allow user
     * input on the window
     */
    @Override
    public void requestFocus() { }

    /**
     * Returns all the pixels in the writableImage
     *
     * @return pixels in the writableImage
     */
    public int[] getPixels() {
        return rawInts;
    }

    /**
     * Using system.arraycopy set all pixels from
     * given buffer into this image's buffer.
     *
     * @param rawPixels pixels to be inserted
     */
    public void setPixels(int[] rawPixels) {
        System.arraycopy(rawPixels, 0, rawInts, 0, rawPixels.length);
    }

    /**
     * Using system.arraycopy set all pixels from
     * given buffer into this image's buffer.
     *
     * @param rawPixels pixels to be inserted
     */
    public void setPixels(int srcPos, int destPos, int[] rawPixels) {
        if(destPos + rawPixels.length >= rawInts.length || destPos < 0) return;
        System.arraycopy(rawPixels, srcPos, rawInts, destPos, rawPixels.length);
    }

    /**
     * Set a single pixel of this image'sbuffer at x, y to a
     * given ARGB color.
     *
     * @param x position at the x coordinate
     * @param y position at the y coordinate
     * @param color ARGB color
     */
    public void setArgb(int x, int y, int color) {
        if(x < 0 || x >= width || y < 0 || y >= height) return;
        rawInts[(x % width) + (y * width)] = color;
    }

    /**
     * Draw all of the ARGB pixels into this image.
     */
    public void updateBuffer() {
        pixelBuffer.updateBuffer(b -> null);
    }

}
