package com.schwada.liege.graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/**
 *
 */
public class Font {
    public static final Font ARIAL_10 = new Font("arial10.png");
    public static final Font ARIAL_8 = new Font("arial8.png");

    private final int[] pixels;
    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    public Font(String name) {
        fontImage = new Image(getClass().getResource(name).toString());
        PixelReader reader = fontImage.getPixelReader();
        int imgWidth = (int)fontImage.getWidth();
        int imgHeight = (int)fontImage.getHeight();

        this.pixels = new int[imgWidth * imgHeight];
        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                pixels[x + y * imgWidth] = reader.getArgb(x, y);
            }
        }

        //unicode things
        int characters = 256;
        offsets = new int[characters];
        widths = new int[characters];

        int unicode = 0;
        for(int i = 0; i < imgWidth; i++) {
            if(pixels[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }
            if(pixels[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }

    public int[] getPixels() {
        return this.pixels;
    }
}