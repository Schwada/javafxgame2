package com.schwada.liege.controller;

import com.schwada.liege.graphics.Renderer;
import com.schwada.liege.logic.*;
import com.schwada.liege.scene.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * JavaFX controller for menu scene. Initializes the game
 * and controls its lifecycle.
 *
 * @author Schwada
 */
public class GameController implements Initializable {

    private final static Logger logger = Logger.getLogger(GameController.class.getName());

    private final SceneManager manager;
    private final AppState state;
    private final Input input;

    @FXML
    private Pane scene;
    private Renderer renderer;
    private float fps;

    /** Game objects **/
    private WorldMap worldMap;
    private Camera camera;
    private Selection selection;


    public GameController(SceneManager manager, AppState state) {
        this.manager = manager;
        this.state = state;
        this.input = new Input();
    }


    /**
     * FIXME: Should wait until SceneManager is done switching scenes to initialize game
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // bind input to scene events
        this.bindInputToScene();
        this.renderer = new Renderer(this.scene);

        // TODO: until objects and their resources are loaded should display loading
        this.camera = new Camera();
        // create objects that will go in the scene
        this.worldMap = new WorldMap();
        this.selection = new Selection();

        // TODO: could be moved to separate thread, but buffer switching needs to occur on FX thread and in sync
        AnimationTimer gameLoop = new AnimationTimer() {
            long then = 0;
            @Override
            public void handle(long now) {
                // fps begin count
                float delta = (now - then) / 1e9f;
                // buffer empty
                renderer.getEmptyBuffer();
                // update game state
                update(delta);
                // buffer fill
                render();
                // buffer switch
                renderer.switchBuffers();
                // fps get result and start over
                fps =  1 / (delta);
                then = now;
            }
        };
        gameLoop.start();
    }

    private void update(float delta) {
        // required for input to work on scene
        this.scene.requestFocus();

        // TODO: following should be moved to generic game class

        // CAMERA AND SELECTION
        camera.update(this.input, delta);
        selection.update(this.input, delta, this.camera);
        // MAP BLOCK

        // GAME OBJECTS

        // GUI

        // DEBUG
    }

    private void render() {
        this.renderer.clear();


        // TODO: following should be moved to generic game class

        // MAP BLOCK
        worldMap.render(this.renderer, this.camera);
        // SELECTION
        this.selection.render(this.renderer, this.camera);
        // GAME OBJECTS

        // GUI
            // bottom menu
        this.renderer.fillRect(0,Renderer.HEIGHT - 25,Renderer.WIDTH, 25, 0xff4f4f4f);
        this.renderer.fillRect(0,Renderer.HEIGHT - 25,Renderer.WIDTH, 5, 0xff3f3f3f);
            // left hand menu

            // modal

        // DEBUG
        this.renderer.drawString("fps: " + this.fps, 15 , 15, 0xffffffff);
    }

    private void bindInputToScene() {
        // TODO: could be decoupled to "drawing area" class together with some stuff in render class
        this.scene.setFocusTraversable(true);
        this.scene.setOnMousePressed(event -> input.setMousePress( (event.getSceneX() / Renderer.SCALE) ,(event.getSceneY() / Renderer.SCALE)));
        this.scene.setOnMouseReleased(event -> input.setMouseRelease());
        this.scene.setOnMouseMoved(event -> input.setMouseMoved(event.getSceneX() / Renderer.SCALE, event.getSceneY() / Renderer.SCALE));
        this.scene.setOnScroll(event -> input.setMouseZoom(event.getDeltaY()));
        this.scene.setOnKeyPressed(event -> input.putKey(event.getCode().toString()));
        this.scene.setOnKeyReleased(event -> input.removeKey(event.getCode().toString()));
    }

}
