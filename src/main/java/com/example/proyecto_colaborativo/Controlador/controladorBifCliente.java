package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import javax.swing.*;

public class controladorBifCliente {
    public Button botonAgregar;
    public Button botonListado;




    public void botonAgregar(javafx.event.ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("agregarCliente.fxml", "Nuevo Cliente", false);

    }

    public void botonListado(ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("clienteGeneral.fxml", "Nuevo Cliente", false);

    }
}
