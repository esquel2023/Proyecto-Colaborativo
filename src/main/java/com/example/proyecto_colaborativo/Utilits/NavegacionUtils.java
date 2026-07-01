package com.example.proyecto_colaborativo.Utilits;

import com.example.proyecto_colaborativo.Controlador.ControladorProductoAgregar;
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
     * @param rutaFxml La ruta del archivo FXML (ej: "/com/example/proyecto_colaborativo/Producto.fxml")
     * @param titulo   El título que tendrá la nueva ventana
     * @param esModal  Si es true, bloquea la ventana de atrás hasta que se cierre la nueva
     * @return
     */
    public  static ControladorProductoAgregar abrirPantalla(String rutaFxml, String titulo, boolean esModal) {
        try {
            // 1. Cargar el diseño FXML
            FXMLLoader fxmlLoader = new FXMLLoader(NavegacionUtils.class.getResource("/com/example/proyecto_colaborativo/"+rutaFxml));
            Scene scene = new Scene(fxmlLoader.load());

            Stage nuevostage = new Stage();

            nuevostage.setTitle(titulo);

            nuevostage.setScene(scene);

            if(esModal) {
                nuevostage.initModality(Modality.APPLICATION_MODAL);
                // Muestra la ventana y detiene la ejecución del código de la principal hasta que se cierre
                nuevostage.showAndWait();
            }else{
                nuevostage.show();
            }
            //return Loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            AlertasUtils.mostrarAlerta("Error de Navegacion",
                    "No se pudo cargar la vista",
                    "Hubo un problema al intentar abrir" + rutaFxml + "\nDetalle " + e.getMessage(),
                    Alert.AlertType.ERROR);
            //return null;
        }
        return null;
    }

}
