package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ControladorVenta {

     // ACCIÓN DEL BOTÓN: Abre la pantalla final y le pasa los datos
    public void clickAgregarVentaProducto(ActionEvent event) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("VentasProducto.fxml", "Gestión de Productos", false);

    }
}
