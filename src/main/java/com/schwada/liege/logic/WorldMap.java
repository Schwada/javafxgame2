package com.schwada.liege.logic;

import com.schwada.liege.graphics.Renderer;
import com.schwada.liege.graphics.WritableImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author Schwada
 */
public class WorldMap {

    public static final int MAP_WIDTH = 720;
    public static final int MAP_HEIGHT = 480;

    WritableImageView mapBuffer = new WritableImageView(Renderer.WIDTH, Renderer.HEIGHT);
    int[] baseLayer;


    public WorldMap() {
        baseLayer = getWorldLayer("base");
    }


    /**
     *
     * @param layerName
     * @return
     */
    public static int[] getWorldLayer(String layerName) {
        int[] tiles = new int[MAP_WIDTH * MAP_HEIGHT];
        try {
            // FIXME: get from FileSystemUtils
            File file = new File(WorldMap.class.getResource(layerName + ".dat").getFile());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            for (int i = 0; i < tiles.length; i++) {
                tiles[i] = fileContent[i];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tiles;
    }

    public void render(Renderer renderer, Camera camera) {
        if(camera.isUpdated()) {
            renderer.clear(mapBuffer);

            for (int x = (int) camera.getOffX(); x < Renderer.WIDTH + camera.getOffX(); x++) {
                for (int y = (int) camera.getOffY(); y < Renderer.HEIGHT + camera.getOffY(); y++) {
                    if (x >= MAP_WIDTH || x <= 0) continue;
                    if (y >= MAP_HEIGHT || y <= 0) continue;
                    if (baseLayer[x + y * MAP_WIDTH] != 0) {
                        // FIXME: improve performance
                        renderer.fillRect(camera.worldToScreenX(x), camera.worldToScreenY(y), (int) camera.getScale(), (int) camera.getScale(), 0xff00ff00, mapBuffer);
                    }else {
                        // FIXME: could potentially just be rendered as one unified rect if data inside does not change
                        renderer.fillRect(camera.worldToScreenX(x), camera.worldToScreenY(y), (int) camera.getScale(), (int) camera.getScale(), 0xff2b4254, mapBuffer);
                    }
                }
            }

        }
        renderer.setPixels(this.mapBuffer.getPixels());
    }


}
