package com.rather.parsoftables;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage primaryStage; // **Declare static Stage**

    private void setPrimaryStage(Stage stage) {
        Application.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Application.primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(primaryStage); // **Set the Stage**
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 693);
        stage.setTitle("TableParser");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}