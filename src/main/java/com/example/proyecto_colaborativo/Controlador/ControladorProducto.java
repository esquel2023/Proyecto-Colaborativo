package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorProducto {


    // 1. Inyectamos la TableView apuntando a la clase Producto
    @FXML
    private TableView<Producto> tablaProducto;
    // 2. Inyectamos las columnas existentes de tu FXML
    @FXML
    private TableColumn<Producto, String> nombre;
    @FXML
    private TableColumn<Producto, String> cantidad;
    @FXML
    private TableColumn<Producto, Double> precio;
    @FXML
    private TableColumn<Producto, String> codigoTabla;

    // Lista observable que contendrá los productos reales
    private final ObservableList<Producto> productos = FXCollections.observableArrayList();

    @FXML

    public void initialize(){
    // 3. Vinculamos cada columna con el nombre exacto de la propiedad en la clase Producto
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        codigoTabla.setCellValueFactory(new PropertyValueFactory<>("codigoTabla"));

        // 4. Asignamos la lista de datos a la tabla
        tablaProducto.setItems(productos);

        // Ejemplo: Cargamos datos de prueba para verificar que funcione
        cargarDatosEjemplo();
    }

    private void cargarDatosEjemplo() {
        tablaProducto.getItems().add(new Producto("Alfajor Havanna", "50 u", 1500.0, "77912345"));
       // tablaProducto.getColumns().add(new Producto("Yerba Playadito 1kg", "20 u", 4200.5, "77967890"));
    }

}
