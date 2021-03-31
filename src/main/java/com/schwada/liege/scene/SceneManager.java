package com.schwada.liege.scene;

import com.schwada.liege.logic.AppState;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Manages switching scenes and injecting
 * dependencies into the controllers
 * of the application.
 *
 * @author Schwada
 */
public class SceneManager {
    private final static Logger logger = Logger.getLogger(SceneManager.class.getName());

    private final Stage primaryStage;
    private final AppState state;
    private FXMLLoader loader;

    /**
     * Constructor that prepares the scene loader for injecting dependencies
     * through a controller factory and specifies what
     * the application should do on exit.
     *
     * @param primaryStage Stage on which scenes will be switched
     * @param state Game state that will be injected into controllers
     */
    public SceneManager(Stage primaryStage, AppState state) {
        logger.info("Instantiating scene manager...");
        this.state = state;
        this.loader = new FXMLLoader();
        this.loader.setControllerFactory(this.getControllerFactory());
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(event -> {
            // TODO: move to own class and extend logic
            logger.info("Exiting application...");
            Platform.exit();
        });
    }

    /**
     * Switches current scene to specified type and
     * sets the title and size properties for the window.
     *
     * @param type Type of scene to switch to.
     */
    public void setScene(SceneType type) {
        logger.info("Scene manager - switching scene to type: " + type +" and loading controller");
        this.loader = new FXMLLoader();
        this.loader.setControllerFactory(this.getControllerFactory());
        Parent loadedScene;

//        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
//        System.out.println("LOADED: " + getClass().getResource(type.name().toLowerCase() + ".fxml"));
//        this.loader.setLocation(getClass().getResource(type.name().toLowerCase() + ".fxml"));
        try {
            this.loader.setLocation(new URL("jrt:/com.schwada.liege/com/schwada/liege/scene/menu.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            loadedScene = this.loader.load();
        } catch (IOException e) {
            logger.info("Scene manager - failed switching to scene type: " + type + " - unable to find scene definition");
            e.printStackTrace();
            return;
        }

        // TODO: Make adjustable screen size
        this.primaryStage.setScene(new Scene(loadedScene));
        this.primaryStage.setMaximized(true);
        this.primaryStage.setResizable(false);
        this.primaryStage.setFullScreen(true);
        this.primaryStage.setFullScreenExitHint("");
        this.primaryStage.setTitle(type.name().toLowerCase());
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.primaryStage.show();
        logger.info("Scene manager - switched scene to type: " + type);
    }

    /**
     * Gets the factory for controllers
     * that contains dependency injection.
     *
     * @return controller factory
     */
    private Callback<Class<?>, Object> getControllerFactory() {
        return type -> {
            try {
                List<Object> parameters = new ArrayList<>();
                for (Constructor<?> c : type.getConstructors()) {
                    for (int i = 0; i <= c.getParameterCount() - 1; i++) {
                        // TODO: autowire properties regardless of type or order
                        if (c.getParameterTypes()[i] == SceneManager.class) parameters.add(this);
                        if (c.getParameterTypes()[i] == AppState.class) parameters.add(this.state);
                    }
                    logger.info("Scene manager - Injecting dependencies into controller");
                    return c.newInstance(parameters.get(0), parameters.get(1));
                }
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Scene manager - failed injecting dependencies into scene");
            }
            return null;
        };
    }

}
