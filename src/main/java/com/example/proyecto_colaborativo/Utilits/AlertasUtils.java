package com.example.proyecto_colaborativo.Utilits;

import javafx.scene.control.Alert;

public class AlertasUtils {
    /**
     * Muestra una alerta en pantalla de forma genérica.
     *
     * @param titulo     Texto de la barra de título de la ventana.
     * @param encabezado Texto destacado en la parte superior.
     * @param contenido  Cuerpo del mensaje con el detalle para el usuario.
     * @param tipo       Tipo de alerta (Alert.AlertType.ERROR, WARNING, INFORMATION, etc.).
     */
     public static void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
     }
}
