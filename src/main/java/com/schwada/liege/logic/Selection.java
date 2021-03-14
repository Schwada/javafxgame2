package com.schwada.liege.logic;

import com.schwada.liege.graphics.Renderer;

/**
 *
 * @author Schwada
 */
public class Selection {

    private Integer selectedTileX;
    private Integer selectedTileY;

    public Selection() {
    }

    public void update(Input input, float delta, Camera camera) {
        if (input.isMouseClicked()) {
            int chosenTileX = (int)camera.screenToWorldX((int)input.getMouseX());
            int chosenTileY = (int)camera.screenToWorldY((int)input.getMouseY());
            if(chosenTileX > 0 && chosenTileX < WorldMap.MAP_WIDTH && chosenTileY > 0 && chosenTileY < WorldMap.MAP_HEIGHT) {
                this.selectedTileX = chosenTileX;
                this.selectedTileY = chosenTileY;
            }
        }
    }

    public void render(Renderer renderer, Camera camera) {
        if(selectedTileY != null && selectedTileX != null) {
            renderer.fillRect(
                    camera.worldToScreenX(this.selectedTileX),
                    camera.worldToScreenY(this.selectedTileY),
                    (int)camera.getScale(),
                    (int)camera.getScale(),
                    0xffe9ee4b
            );
        }
    }

    public Integer getSelectedTileX() {
        return selectedTileX;
    }

    public void setSelectedTileX(Integer selectedTileX) {
        this.selectedTileX = selectedTileX;
    }

    public Integer getSelectedTileY() {
        return selectedTileY;
    }

    public void setSelectedTileY(Integer selectedTileY) {
        this.selectedTileY = selectedTileY;
    }
}
