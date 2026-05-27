package com.example.proyecto_colaborativo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
//<3
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        System.out.println("soy eliseo");
    }
}
// HOLAAAAAAAAAAAAAA soy eliseo


//conflicttó