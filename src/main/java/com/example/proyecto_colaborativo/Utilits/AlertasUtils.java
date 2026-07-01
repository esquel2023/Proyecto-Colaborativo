package com.example.proyecto_colaborativo.Utilits;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AlertasUtils {

    /**
     * Muestra una alerta informativa, de advertencia o error en pantalla.
     */
    public static void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación (SÍ/NO o ACEPTAR/CANCELAR) y devuelve la respuesta.
     */
    public static boolean mostrarAlertaConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }


}