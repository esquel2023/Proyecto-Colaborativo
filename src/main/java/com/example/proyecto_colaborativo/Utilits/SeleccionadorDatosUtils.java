package com.example.proyecto_colaborativo.Utilits;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import javax.swing.text.TableView;
import java.util.function.Consumer;
import java.util.function.Function;

public class SeleccionadorDatosUtils {
    // =========================================================================
    // SOLUCIÓN 1: PARA MOVERSE EN UNA SOLA PANTALLA (Vincular Tabla a Campos)
    // =========================================================================
    public static <T> void vincularTablaFormulario(
            TableView<T> tabla,
            Consumer<T> onSelection,
            TextField txtcampo1, Function<T,String> mapper1,
            TextField txtcampo2, Function<T,String> mapper2,
            TextField txtcampo3, Function<T,String> mapper3,
            TextField txtcampo4, Function<T,String> mapper4){
        tabla.getSelectionModel().selectedItemProperty().addlistener((observable, oldvalue, newvalue)->{
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
                ((DataReceiver<T>) controladorDestino).setData(datos);
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
}
