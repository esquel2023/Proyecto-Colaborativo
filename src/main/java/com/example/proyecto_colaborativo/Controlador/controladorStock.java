package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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


    /* ================= TABLA ================= */

    @FXML
    private TableColumn<?, ?> Codigo;

    @FXML
    private TableColumn<?, ?> Producto;

    @FXML
    private TableColumn<?, ?> Cantidad;

    @FXML
    private TableColumn<?, ?> Descripcion;

    @FXML
    private TableColumn<?, ?> Fecha;


    /* ================= FECHA Y HORA ================= */

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaLabel;


    /* =========================================================
       INICIALIZAR
       ========================================================= */

    @FXML
    public void initialize() {

        iniciarReloj();
    }


    /* =========================================================
       RELOJ
       ========================================================= */

    private void iniciarReloj() {

        Locale locale = new Locale("es", "AR");

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "EEEE dd 'de' MMMM 'de' yyyy",
                        locale
                );

        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern("HH:mm:ss");


        Timeline reloj = new Timeline(

                new KeyFrame(
                        Duration.ZERO,

                        event -> {

                            LocalDateTime ahora =
                                    LocalDateTime.now();


                            String fecha =
                                    ahora.format(formatoFecha);


                            if (!fecha.isEmpty()) {

                                fecha =
                                        fecha.substring(0, 1).toUpperCase()
                                                + fecha.substring(1);
                            }


                            if (fechaLabel != null) {
                                fechaLabel.setText(fecha);
                            }


                            if (horaLabel != null) {
                                horaLabel.setText(
                                        ahora.format(formatoHora)
                                );
                            }
                        }
                ),

                new KeyFrame(
                        Duration.seconds(1)
                )
        );


        reloj.setCycleCount(
                Timeline.INDEFINITE
        );

        reloj.play();
    }


    /* =========================================================
       BOTONES
       ========================================================= */

    @FXML
    public void ClickLupa(ActionEvent actionEvent) {

        NavegacionUtils.abrirPantalla(
                "Producto.fxml",
                "Gestión de Productos",
                false
        );
    }


    @FXML
    public void clickAgregar(ActionEvent actionEvent) {

    }


    @FXML
    public void clickModificar(ActionEvent actionEvent) {

    }


    @FXML
    public void clickEliminar(ActionEvent actionEvent) {

    }


    @FXML
    public void clickdate(ActionEvent actionEvent) {

    }
}