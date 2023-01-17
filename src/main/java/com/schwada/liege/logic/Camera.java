package com.schwada.liege.logic;

/**
 *
 * @author Schwada
 */
public class Camera {

    private int scale = 3;
    private int zoomVelocity = 1;

    private float offX, offY;
    private float velocity = 500;
    // private final float maxVelocity = 1000;

    private boolean updated = true;
    private float lastOffX, lastOffY;
    private float lastScale;

    public static final int MAX_OFFX = 550;
    public static final int MAX_OFFY = 350;

    public static final int MAX_SCROLLZ = 3;
    public static final int MIN_SCROLLZ = 20;


    public Camera() {

    }


    public void update(Input input, float delta) {

        if (input.isKey("D") && this.offX <= MAX_OFFX) this.offX += delta * (velocity / scale);
        if (input.isKey("A") && this.offX >= -MAX_OFFX) this.offX -= delta * (velocity / scale);
        if (input.isKey("W") && this.offY >= -MAX_OFFY) this.offY -= delta * (velocity / scale);
        if (input.isKey("S") && this.offY <= MAX_OFFY) this.offY += delta * (velocity / scale);


        float worldMouseBeforeZoomX = this.screenToWorldX((int)input.getMouseX());
        float worldMouseBeforeZoomY = this.screenToWorldY((int)input.getMouseY());


        if (input.getMouseZoomTime() != 0 && input.getMouseZoom() < scale && scale > MAX_SCROLLZ) scale -= zoomVelocity;
        if (input.getMouseZoomTime() != 0 && input.getMouseZoom() > scale && scale < MIN_SCROLLZ) scale += zoomVelocity;
        if(input.getMouseZoomTime() > 0) input.setMouseZoomTime(0); // fixme so that this isnt necessary

        float worldMouseAfterZoomX = this.screenToWorldX((int)input.getMouseX());
        float worldMouseAfterZoomY = this.screenToWorldY((int)input.getMouseY());

        this.offX += (worldMouseBeforeZoomX - worldMouseAfterZoomX);
        this.offY += (worldMouseBeforeZoomY - worldMouseAfterZoomY);


        // todo dirty flag for render instead
        if(updated) {
            this.updated = false;
        }

        if (this.scale != this.lastScale) {
            this.lastScale = scale;
            this.updated = true;
        }
        if(this.offX != this.lastOffX) {
            this.lastOffX = offX;
            this.updated = true;
        }
        if(this.offY != this.lastOffY) {
            this.lastOffY = offY;
            this.updated = true;
        }


    }

    public float getScale() {
        return scale;
    }

    public float getOffX() {
        return offX;
    }

    public void setOffX(float offX) {
        this.offX = offX;
    }

    public float getOffY() {
        return offY;
    }

    public void setOffY(float offY) {
        this.offY = offY;
    }

    public boolean isUpdated() {
        return updated;
    }

    // helper functions for camera view
    public int worldToScreenX(float worldX) {
        return (int)((worldX - offX) * scale); // screenX
    }

    public int worldToScreenY(float worldY) {
        return (int)((worldY - offY) * scale);  // screenY
    }

    public float screenToWorldX(int screenX) {
        return screenX / scale + offX;
    }

    public float screenToWorldY(int screenY) {
        return screenY / scale + offY;
    }

}
