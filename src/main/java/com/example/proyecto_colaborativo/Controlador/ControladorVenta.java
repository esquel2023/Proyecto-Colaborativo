package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.AlertasUtils;
import com.example.proyecto_colaborativo.HelloApplication;
import com.example.proyecto_colaborativo.NavegacionUtils;
import com.example.proyecto_colaborativo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.io.IOException;

public class ControladorVenta {

     // ACCIÓN DEL BOTÓN: Abre la pantalla final y le pasa los datos
    public void clickAgregarVentaProducto(ActionEvent event) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("/com/example/proyecto_colaborativo/VentasProducto.fxml", "Gestión de Productos", false);

    }
}
