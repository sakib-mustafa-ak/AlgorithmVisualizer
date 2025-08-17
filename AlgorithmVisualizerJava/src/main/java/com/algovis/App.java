package com.algovis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    static {
        try {
            Class.forName("com.algovis.core.RegistryBootstrap");
        } catch (ClassNotFoundException e) {
            System.err.println("RegistryBootstrap class not found: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/algovis/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/com/algovis/styles.css").toExternalForm());
        stage.setTitle("Algorithm Visualizer (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}