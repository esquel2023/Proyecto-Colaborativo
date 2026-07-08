package com.example.proyecto_colaborativo.Utilits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class NavegacionUtils {
    /**
     * Abre una nueva ventana a partir de un archivo FXML de forma genérica.
     *
     * @param <T>      El tipo de controlador de la pantalla que se va a abrir
     * @param rutaFxml La ruta del archivo FXML (ej: "Producto.fxml")
     * @param titulo   El título que tendrá la nueva ventana
     * @param esModal  Si es true, bloquea la ventana de atrás hasta que se cierre la nueva
     * @return El controlador genérico de la pantalla abierta, o null si ocurre un error.
     */
    // 💡 CAMBIO: Se añade <T> y se retorna T en lugar de un controlador fijo
    public static <T> T abrirPantalla(String rutaFxml, String titulo, boolean esModal) {
        try {
            // 1. Cargar el diseño FXML
            FXMLLoader fxmlLoader = new FXMLLoader(NavegacionUtils.class.getResource("/com/example/proyecto_colaborativo/" + rutaFxml));
            Scene scene = new Scene(fxmlLoader.load());

            Stage nuevostage = new Stage();
            nuevostage.setTitle(titulo);
            nuevostage.setScene(scene);

            if (esModal) {
                nuevostage.initModality(Modality.APPLICATION_MODAL);
                // Muestra la ventana y detiene la ejecución del código de la principal hasta que se cierre
                nuevostage.showAndWait();
            } else {
                nuevostage.show();
            }

            // 💡 RETORNO GENÉRICO: Java detectará automáticamente qué controlador es en base a dónde lo llames
            return fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            AlertasUtils.mostrarError("No se pudo cargar la vista",
                    "Hubo un problema al intentar abrir " + rutaFxml + "\n\nDetalle: " + e.getMessage()
            );
        }
        return null;
    }
}
