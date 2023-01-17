package com.schwada.liege.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.schwada.liege.logic.AppState;
import com.schwada.liege.scene.SceneManager;
import com.schwada.liege.scene.SceneType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

/**
 * JavaFX controller for menu scene.
 *
 * @author Schwada
 */
public class MenuController implements Initializable {

    /** Global objects injected into controller **/
    private final SceneManager manager;

    public MenuController(SceneManager manager, AppState state) {
        this.manager = manager;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     *
     * @param actionEvent
     */
    public void handlePlay(ActionEvent actionEvent) {
        manager.setScene(SceneType.GAME);
    }

    /**
     *
     * @param actionEvent
     */
    public void handleQuit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     *
     * @param actionEvent
     */
    public void handleSettings(ActionEvent actionEvent) {
    }

}
