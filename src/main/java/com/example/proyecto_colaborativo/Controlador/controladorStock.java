package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class controladorStock {

    @FXML
    private TextField codigo;
    @FXML
    private TextField buscadorProductos;
    @FXML
    private TextField codigoBarras;
    @FXML
    private Button botonAgregar;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonModificar;
    @FXML
    private Button lupa;
    @FXML
    private TableColumn Codigo;
    @FXML
    private TableColumn Producto;
    @FXML
    private TableColumn Cantidad;
    @FXML
    private TableColumn Descripcion;
    @FXML
    private TableColumn Fecha;


    public void ClickLupa(ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("Producto.fxml", "Gestión de Productos", false);

    }

    public void clickAgregar(ActionEvent actionEvent) {


    }

    public void clickModificar(ActionEvent actionEvent) {
    }

    public void clickEliminar(ActionEvent actionEvent) {
    }

    public void clickdate(ActionEvent actionEvent) {
    }
}

