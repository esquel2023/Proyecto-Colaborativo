package com.example.proyecto_colaborativo.Utilits;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import java.util.Optional;

public class AlertasUtils {

    // Ruta base para tus estilos CSS (opcional, ajusta a tu proyecto)
    private static final String CSS_PATH = "/styles/alertas.css";

    /**
     * Alerta genérica con aplicación automática de estilos CSS.
     */
    public static void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);

        formatearAlerta(alerta);
        alerta.showAndWait();
    }

    /**
     * Métodos especializados para evitar pasar el "Alert.AlertType" manualmente.
     */
    public static void mostrarError(String encabezado, String contenido) {
        mostrarAlerta("Error del Sistema", encabezado, contenido, Alert.AlertType.ERROR);
    }

    public static void mostrarInformacion(String encabezado, String contenido) {
        mostrarAlerta("Información", encabezado, contenido, Alert.AlertType.INFORMATION);
    }

    public static void mostrarAdvertencia(String encabezado, String contenido) {
        mostrarAlerta("Advertencia", encabezado, contenido, Alert.AlertType.WARNING);
    }

    /**
     * Alerta de confirmación mejorada y segura.
     */
    public static boolean mostrarConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);

        formatearAlerta(alerta);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    /**
     * Método privado para centralizar el diseño, iconos y CSS de las alertas.
     */
    private static void formatearAlerta(Alert alerta) {
        DialogPane dialogPane = alerta.getDialogPane();

        // 1. Aplicar CSS personalizado si existe el archivo
        if (AlertasUtils.class.getResource(CSS_PATH) != null) {
            dialogPane.getStylesheets().add(AlertasUtils.class.getResource(CSS_PATH).toExternalForm());
            dialogPane.getStyleClass().add("alerta-personalizada");
        }

        // 2. Hacer que la alerta sea responsiva al contenido
        dialogPane.setMinHeight(DialogPane.USE_PREF_SIZE);

        // 3. Añadir el icono de la aplicación a la barra de tareas (opcional)
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.setAlwaysOnTop(true); // Evita que la alerta se esconda detrás de la ventana principal
    }
}
