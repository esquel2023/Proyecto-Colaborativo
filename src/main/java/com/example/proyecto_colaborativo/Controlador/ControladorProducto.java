package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorProducto {



    // 1. Inyectamos la TableView apuntando a la clase Producto
    @FXML
    private TableView<Producto> tablaProductos;
    // 2. Inyectamos las columnas existentes de tu FXML
    @FXML
    private TableColumn<Producto, String> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colCantidad;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TextField codigo;
    @FXML
    private TextField nombre;
    @FXML
    private TextField cantidad;
    @FXML
    private TextField precioFinal;


    // Lista observable que contendrá los productos reales
   // private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    @FXML


    // VARIABLE NUEVA: Guarda el objeto seleccionado para poder modificarlo después
    private Producto productoseleccionado;


    public void initialize(){
    // 3. Vinculamos cada columna con el nombre exacto de la propiedad en la clase Producto

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoTabla"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(
                new Producto("Arroz 1kg", "50", 1200.50, "PROD001"),
                new Producto("Leche Entera", "30", 950.00, "PROD002"),
                new Producto("Aceite Girasol", "15", 2500.00, "PROD003")
        );

        // 4. Cargar los datos en la tabla
        tablaProductos.setItems(listaProductos);


        // Escuchar cuando el usuario selecciona una fila de la tabla
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // ASIGNACIÓN: Guardamos la referencia del producto seleccionado
                this.productoseleccionado = newValue;

                // 'newValue' contiene el objeto Producto seleccionado
                System.out.println("Seleccionaste: " + newValue.getNombre());
                // Ejemplo: Llenar tus campos de texto automáticamente con los datos del producto
                codigo.setText(newValue.getCodigoTabla());
                nombre.setText(newValue.getNombre());
                cantidad.setText(String.valueOf(newValue.getCantidad()));
                precioFinal.setText(String.valueOf(newValue.getPrecio()));
            }

        });
    }
    // MÉTODO NUEVO: Se ejecuta al presionar el botón Modificar
    @FXML
    private void clickModificar(ActionEvent event) {
        // 1. Validar que el usuario haya seleccionado una fila previamente
        if (productoseleccionado != null) {
            System.out.println("Error: Debes seleccionar un producto de la tabla.");
            return;
        }

        try {
            // 2. Tomar los nuevos valores directamente desde los TextField
            String nuevacantidad = cantidad.getText();
            Double nuevoPrecio = Double.parseDouble(precioFinal.getText());
            String nuevacodigo = codigo.getText();
            String nuevonombre = nombre.getText();

            // 3. Modificar las propiedades del objeto observable
            // Al usar .set(), JavaFX avisa automáticamente a la TableView y se refresca sola
            productoseleccionado.cantidadProperty().set(nuevacantidad);
            productoseleccionado.precioProperty().set(nuevoPrecio);
            productoseleccionado.nombreProperty().set(nuevonombre);
            productoseleccionado.cantidadProperty().set(nuevacodigo);

            // 4. Limpiar los campos y la selección de la tabla para el siguiente producto
            tablaProductos.getSelectionModel().select(productoseleccionado);
            limpiarCampos();
            this.productoseleccionado = null;
            System.out.println("¡Producto modificado con éxito en la tabla!");

        } catch (NumberFormatException e) {
        System.out.println("Error: El precio ingresado no es un número válido.");
    }

    }
    // Método auxiliar para limpiar las cajas de texto
    private void limpiarCampos() {
        codigo.setText("");
        nombre.setText("");
        cantidad.setText("");
        precioFinal.setText("");
    }

}
