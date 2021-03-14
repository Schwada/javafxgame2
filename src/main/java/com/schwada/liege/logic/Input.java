package com.schwada.liege.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Schwada
 */
public class Input  {

    private List<String> keys = new ArrayList<>();
    private boolean mouseClicked = false;
    private int mouseZoomTime = 0;
    private double mouseZoom = 0;
    private double mouseX;
    private double mouseY;

    /**
     * Part of gameloop reset's all counters
     * dealing with input and other stuff.
     */
    public void update() {

    }

    public boolean containsKey(String codeString) {
        return keys.stream().anyMatch((key) -> (Objects.equals(key, codeString)));
    }

    public void setMousePress(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        mouseClicked = true;
    }

    public void setMouseRelease() {
        mouseClicked = false;
    }

    public void putKey(String codeString) {
        if(!this.containsKey(codeString)) {
            keys.add(codeString);
        }
    }

    public void removeKey(String toString) {
        if(containsKey(toString)){
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) == null ? toString == null : keys.get(i).equals(toString)) { keys.remove(i); }
            }
        }
    }

    public boolean isKey(String keycode){
        if(containsKey(keycode)){return containsKey(keycode);}
        return containsKey(keycode);
    }

    public boolean isMouseClicked() {return mouseClicked;}

    public double getMouseX() {return mouseX;}
    public double getMouseY() {return mouseY;}


    public int getMouseZoomTime() {
        return mouseZoomTime;
    }

    public void setMouseZoomTime(int mouseZoomTime) {
        this.mouseZoomTime = mouseZoomTime;
    }

    public double getMouseZoom() { return mouseZoom; }

    public void setMouseZoom(double deltaY) {
        this.mouseZoomTime++;
        this.mouseZoom = deltaY;
    }

    public void setMouseMoved(double mouseX, double mouseY) {
        this.mouseX = (int) mouseX;
        this.mouseY = (int) mouseY;
    }

}
