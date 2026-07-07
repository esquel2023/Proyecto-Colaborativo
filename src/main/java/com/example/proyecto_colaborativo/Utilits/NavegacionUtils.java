package com.example.proyecto_colaborativo.Utilits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class NavegacionUtils {

    /**
     * Abre una nueva ventana genérica a partir de un archivo FXML y retorna su controlador.
     *
     * @param <T>      El tipo de controlador esperado para la pantalla.
     * @param rutaFxml Nombre del archivo FXML (ej: "Producto.fxml").
     * @param titulo   El título de la nueva ventana.
     * @param esModal  Si es true, bloquea la ventana padre.
     * @return El controlador de la pantalla tipo <T>, o null si ocurre un error.
     */
    public static <T> T abrirPantalla(String rutaFxml, String titulo, boolean esModal) {
        try {
            // Se asume que rutaFxml ya incluye o no el '/' inicial según tu estructura
            FXMLLoader fxmlLoader = new FXMLLoader(NavegacionUtils.class.getResource("/com/example/proyecto_colaborativo/" + rutaFxml));

            Scene scene = new Scene(fxmlLoader.load());
            Stage nuevoStage = new Stage();
            nuevoStage.setTitle(titulo);
            nuevoStage.setScene(scene);

            if (esModal) {
                nuevoStage.initModality(Modality.APPLICATION_MODAL);
                // IMPORTANTE: Muestra la ventana y detiene la ejecución aquí
                nuevoStage.showAndWait();
            } else {
                nuevoStage.show();
            }

            // Retorna el controlador casteado automáticamente al tipo <T> detectado
            return fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            AlertasUtils.mostrarError(
                    "No se pudo cargar la vista",
                    "Hubo un problema al intentar abrir " + rutaFxml + "\n\nDetalle: " + e.getMessage()
            );
        }
        return null;
    }
}
