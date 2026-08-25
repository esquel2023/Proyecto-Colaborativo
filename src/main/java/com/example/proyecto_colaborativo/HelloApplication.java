package com.example.proyecto_colaborativo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("menuprincipal.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );

        stage.setTitle(":)");
        stage.setScene(scene);

        // Tamaño mínimo de la ventana
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        // Abrir ocupando toda la pantalla
        stage.setMaximized(true);

        stage.show();
    }
}// Exito para el proyecto