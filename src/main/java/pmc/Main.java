package pmc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pmc.gui.pmc.PMCController;

/**
 * Indgangen til applikationen.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new PMCController(stage).getView(), 1200, 800));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}