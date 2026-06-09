package com.example.proyecto_colaborativo;

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

    public void botonCaja(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("aperturaycierrecaja.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 540);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public void botonVentas(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ventas.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 540);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public void botonProductos (ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 540);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();

    }

    public void botonProveedores (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("proveedores.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage stage = new Stage();
        stage.setTitle("Proveedores");
        stage.setScene(scene);
        stage.show();
    }
    public void botonStock (ActionEvent actionEvent){
            }

    public void botonClientes (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 540);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void botonFactura (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("factura.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 540);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        }


//    holas 12345


//    soy Oscar

        }
