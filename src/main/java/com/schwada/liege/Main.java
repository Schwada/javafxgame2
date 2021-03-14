package com.schwada.liege;

import com.schwada.liege.logic.AppState;
import com.schwada.liege.scene.SceneManager;

import com.schwada.liege.scene.SceneType;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 *
 * @author Schwada
 */
public class Main extends Application {

    private final static Logger logger = Logger.getLogger(Main.class.getName());
    private AppState state;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$s] %5$s%6$s%n");
        logger.info("David Schwam 2021");
        logger.info("Launching application...");
        launch();
    }

    @Override
    public void init() {
        logger.info("Setting up state...");
        this.state = new AppState();
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Setting stage...");
        SceneManager manager = new SceneManager(primaryStage, state);
        manager.setScene(SceneType.MENU);
    }
}
