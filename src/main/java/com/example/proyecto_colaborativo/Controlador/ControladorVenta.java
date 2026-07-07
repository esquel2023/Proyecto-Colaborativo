package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.event.ActionEvent;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ControladorVenta implements Initializable {
    @FXML
    private Label labelFecha;

    @FXML
    private Label labelHora;


     // ACCIÓN DEL BOTÓN: Abre la pantalla final y le pasa los datos
    public void clickAgregarVentaProducto(ActionEvent event) throws IOException {
        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("VentasProducto.fxml", "Gestión de Productos", false);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarFechaHora();
        iniciarReloj();
    }

    private void actualizarFechaHora() {
        LocalDateTime ahora = LocalDateTime.now();

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy", new Locale("es", "AR"));

        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        String fecha = formatoFecha.format(ahora);


        fecha = fecha.substring(0,1).toUpperCase() + fecha.substring(1);

        labelFecha.setText(fecha);
        labelHora.setText(formatoHora.format(ahora));
    }

    private void iniciarReloj() {

        Timeline reloj = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> actualizarFechaHora())
        );

        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }
}

