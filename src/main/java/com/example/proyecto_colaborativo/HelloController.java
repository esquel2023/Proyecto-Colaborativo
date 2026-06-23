package com.example.proyecto_colaborativo;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    Button caja;
    @FXML
    Button ventas;
    @FXML
    Button productos;
    @FXML
    Button proveedores;
    @FXML
    Button stock;
    @FXML
    Button clientes;
    @FXML
    Button factura;
    @FXML


    public void botonCaja(ActionEvent actionEvent) throws IOException {
       // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("aperturaycierrecaja.fxml", "Caja", false);
    }


    public void botonVentas(ActionEvent actionEvent) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("ventas.fxml", "ventas", false);

    }

    public void botonProductos (ActionEvent actionEvent) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("Producto.fxml", "Producto", false);
    }

    public void botonProveedores (ActionEvent actionEvent) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("proveedores.fxml", "Proveedores", false);
    }
    public void botonStock (ActionEvent actionEvent){
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("stock.fxml", "Stock", false);

    }

    public void botonClientes (ActionEvent actionEvent) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("cliente.fxml", "Cliente", false);
    }

    public void botonFactura (ActionEvent actionEvent) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("factura.fxml", "Factura", false);
        }


//    holas 12345


//    soy Oscar

        }
