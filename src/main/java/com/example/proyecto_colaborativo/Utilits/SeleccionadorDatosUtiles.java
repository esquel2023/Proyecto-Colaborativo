package com.example.proyecto_colaborativo.Utilits;

public class SeleccionadorDatosUtiles {
    /*package com.example.proyecto_colaborativo.Utilits;

import com.example.proyecto_colaborativo.stock.fxml.DataReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;
import java.util.function.Function;



public class SeleccionadorDatosUtils {
    /**
     * Vincula un TableView de productos con campos de texto de cualquier controlador.
     *
     * @param tabla Tabla que se va a escuchar.
     * @param onSeleccion Acción a ejecutar para guardar el producto en el controlador (e.g., prod -> this.seleccionado = prod).
     * @param txtcampo1 Campo para el nombre.
     * @param txtcampo2 Campo para la cantidad.
     * @param txtcampo3 Campo para el precio.
     * @param txtcampo4 Campo para el código de barras (puede ser null).
     * @param <T> Tipo de objeto que maneja la tabla (debe tener métodos compatibles o heredar de una interfaz si usas varias clases).
     */
    // =========================================================================
    // SOLUCIÓN 1: PARA MOVERSE EN UNA SOLA PANTALLA (Vincular Tabla a Campos)
    // =========================================================================
    /*public static <T> void vincularTablaFormulario(
            TableView<T> tabla,
            Consumer<T> onSelection,
            TextField txtcampo1, Function<T,String> mapper1,
            TextField txtcampo2, Function<T,String> mapper2,
            TextField txtcampo3, Function<T,String> mapper3,
            TextField txtcampo4, Function<T,String> mapper4){
        tabla.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue)->{
            if (newvalue != null) {
                onSelection.accept(newvalue);
                if (txtcampo1 != null && mapper1 != null) txtcampo1.setText(mapper1.apply(newvalue));
                if (txtcampo2 != null && mapper2 != null) txtcampo2.setText(mapper2.apply(newvalue));
                if (txtcampo3 != null && mapper3 != null) txtcampo3.setText(mapper3.apply(newvalue));
                if (txtcampo4 != null && mapper4 != null) {
                    String val4 = mapper4.apply(newvalue);
                    txtcampo4.setText(val4 != null ? val4 : "");
                }
            } else {
                onSelection.accept(null);
                if (txtcampo1 != null) txtcampo1.clear();
                if (txtcampo2 != null) txtcampo2.clear();
                if (txtcampo3 != null) txtcampo3.clear();
                if (txtcampo4 != null) txtcampo4.clear();

            }
        });
    }

    // =========================================================================
    // SOLUCIÓN 2: PARA MANDAR INFORMACIÓN A DIFERENTES PANTALLAS
    // =========================================================================
    public static <T> void abrirVentanaConDatos(String fxmlPath,String titulo, T datos){
        try{
            FXMLLoader loader = new FXMLLoader(SeleccionadorDatosUtils.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Captura el controlador de la pantalla que se está abriendo
            Object controladorDestino = loader.getController();

            // Si la pantalla de destino implementa DataReceiver, le inyecta los datos automáticamente
            if(controladorDestino instanceof DataReceiver){
                // Se usa <?> para evitar el error de tipado en tiempo de ejecución
                @SuppressWarnings("unchecked")
                //((DataReceiver<T>) controladorDestino).setData(datos);
                DataReceiver<T> receptor = (DataReceiver<T>) controladorDestino;
                receptor.setData(datos);
            }

            // Crea y muestra la nueva ventana
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la pantalla de atrás hasta cerrar esta
            stage.show();
        } catch (Exception e) {
            AlertasUtils.mostrarAlerta("Error al Cargar la Pantalla", + fxmlPath ,Alert.AlertType.ERROR);
         //   System.err.println("Error al cargar la pantalla: " + fxmlPath);
            e.printStackTrace();
        }
    }
}*/

}
