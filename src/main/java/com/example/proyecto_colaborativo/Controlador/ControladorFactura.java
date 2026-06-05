package com.example.proyecto_colaborativo.Controlador;
import com.example.proyecto_colaborativo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorFactura implements Initializable {


    public SplitMenuButton tipoFactura;
    public Label ingresarCodigo;
    public TextField cantidadUnidades;
    public Text unidades;
    public Button agregarProd;
    public TextField nombre;
    public Text dni;
    public Label totalFinal;
    public Text total;
    public Label cliente;
    @FXML
    private TableView<Producto> datosTabla;

    @FXML
    private TableColumn<Producto, String> colCodigoTabla;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colCantidad;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<claseFactura, Double> colSubtotal;
    claseFactura clasefactura;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarColumnas();
        obtenerProducto();
        Calcular();

    }

    private void Calcular() {

//        clasefactura = new claseFactura(toString(total.getText()));
//        total.setTextt(toHexString(clasefactura.calcularPrecioFinal()));
//     claseFactura clasefactura = new claseFactura(Controladorfactura);
//        totalFinal.setText(String.valueOf(claseFactura.calcularPrecioFinal()));
    }

    private void obtenerProducto() {
            ObservableList<Producto> producto =
                    FXCollections.observableArrayList();

            producto.add(
                    new Producto(
                            "Alfajor",
                            "2",
                            1000,
                            "0001"
                    )
            );

            producto.add(
                    new Producto(
                            "Helado",
                            "1",
                            2500,
                            "0002"
                    )
            );

            datosTabla.setItems(producto);
        }

        private void cargarColumnas() {
            // Enlazar de manera obligatoria las columnas con los nombres de tus variables privadas
            colCodigoTabla.setCellValueFactory(new PropertyValueFactory<>("codigoTabla"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        }

    public void facturaTipo(ActionEvent actionEvent) {
        
    }

    public void agregarProducto(ActionEvent actionEvent) {
    }

    public void eliminarProducto(ActionEvent actionEvent) {
    }

    public void buscarCliente(ActionEvent actionEvent) {
    }

    public void eliminarCliente(ActionEvent actionEvent) {
    }

    public void agregarCliente(ActionEvent actionEvent) throws IOException {
        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador DESPUÉS de cargar el root
            controladorCliente controller = loader.getController();

            // 3. Configurar y mostrar la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Factura");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();

            // 4. Obtener el objeto seleccionado de la tabla
            // Reemplaza 'ClienteClass' por el nombre real de tu clase modelo (ej. Cliente)
            clienteClase clienteSeleccionado = (clienteClase) controller.tablaClientes.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado != null) {
                // 5. Mostrar el dato en el campo de texto (ej. usando el nombre del cliente)
                cliente.setText(clienteSeleccionado.getNombre());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ingresarPago(ActionEvent actionEvent) {
    }

    public void selecEfectivo(ActionEvent actionEvent) {
    }

    public void selecTarjeta(ActionEvent actionEvent) {
    }

    public void elegirCodigo(ActionEvent actionEvent) {
    }
}
