package com.example.proyecto_colaborativo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuprincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        System.out.println(getClass().getResource("style.css"));
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Productos");
        stage.setScene(scene);
        stage.show();
    }
}// Exito para el proyecto