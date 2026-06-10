package com.example.proyecto_colaborativo.Utilits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NavegacionUtils {
    /**
     * Abre una nueva ventana a partir de un archivo FXML.
     *
     * @param rutaFxml   La ruta del archivo FXML (ej: "/com/example/proyecto_colaborativo/Producto.fxml")
     * @param titulo     El título que tendrá la nueva ventana
     * @param esModal    Si es true, bloquea la ventana de atrás hasta que se cierre la nueva
     */
    public  static void abrirPantalla(String rutaFxml,String titulo, boolean esModal) {
        try {
            // 1. Cargar el diseño FXML
            FXMLLoader fxmlLoader = new FXMLLoader(NavegacionUtils.class.getResource("/com/example/proyecto_colaborativo/"+rutaFxml));
            Scene scene = new Scene(fxmlLoader.load());

            // 2. Crear y configurar el nuevo Stage
            Stage nuevostage = new Stage();
            nuevostage.setTitle(titulo);
            nuevostage.setScene(scene);

            // 3. Aplicar modalidad si se requiere
            if (esModal) {
                nuevostage.initModality(Modality.APPLICATION_MODAL);
            }

            // 4. Mostrar la ventana
            nuevostage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertasUtils.mostrarAlerta( "Error de Navegación",
                    "No se pudo cargar la vista",
                    "Hubo un problema al intentar abrir: " + rutaFxml + "\nDetalle: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

}
