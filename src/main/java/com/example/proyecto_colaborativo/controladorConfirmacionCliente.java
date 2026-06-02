package com.example.proyecto_colaborativo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class controladorConfirmacionCliente{
    @FXML
    private Label nombre;
    @FXML private Label dni;
    @FXML private Label telefono;
    @FXML private Label email;
    @FXML private Label cuit;
    @FXML private Label direccion;

    clienteClase cliente;

    public void  initialize(){
        nombre.setText(cliente.getNombreEntidad());
        dni.setText(cliente.getDniEntidad());
        telefono.setText(cliente.getTelefonoEntidad());
        email.setText(cliente.getEmailEntidad());
        cuit.setText(cliente.getCuitcuilEntidad());
        direccion.setText(cliente.getDireccionEntidad());
    }


    public void botonConfirmar(ActionEvent actionEvent) {

    }

    public void botonModificar(ActionEvent actionEvent) {
    }
}
