package com.example.proyecto_colaborativo.Controlador;
import com.example.proyecto_colaborativo.*;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.beans.property.DoubleProperty;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    public TextField nombreYApellido;
    public Button agregarCliente;

    @FXML
    private Button buscarCliente;

    @FXML
    private TableView<Producto> TablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigoDeBarra;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colCantidad;

    @FXML
    private TableColumn<Producto, DoubleProperty> colPrecio;

    private final ObservableList<Producto> listaUsuarios = FXCollections.observableArrayList();

    // Instanciamos el DAO de manera global en el controlador
    private final ProductoDAO usuarioDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarColumnas();
        obtenerProducto();
        Calcular();
        TablaProductos.setItems(listaUsuarios);
    }

    private void Calcular() {

//        clasefactura = new claseFactura(toString(total.getText()));
//        total.setTextt(toHexString(clasefactura.calcularPrecioFinal()));
//     claseFactura clasefactura = new claseFactura(Controladorfactura);
//        totalFinal.setText(String.valueOf(claseFactura.calcularPrecioFinal()));
    }

    private void obtenerProducto() {

    }

    private void cargarColumnas() {
        // Enlazar de manera obligatoria las columnas con los nombres de tus variables privadas
        colCodigoDeBarra.setCellValueFactory(new PropertyValueFactory<>("CodigoDeBarra"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("Precio"));


        // 3. Cargar los registros de forma limpia invocando al DAO


        // El controlador recibe una lista de Java pura y la convierte en observable para la interfaz
        List<Producto> datosBD = usuarioDAO.listar();
        listaUsuarios.addAll(datosBD);
    }

    public void facturaTipo(ActionEvent actionEvent) {

    }

    public void agregarProducto(ActionEvent actionEvent) {
    }

    public void eliminarProducto(ActionEvent actionEvent) {
    }


    public void buscarCliente(ActionEvent actionEvent) throws IOException {
        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("buscadorCliente.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador DESPUÉS de cargar el root
            controladorBuscadorCliente controller = loader.getController();
            controller.setControladorFactura(this);

            // 3. Configurar y mostrar la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("buscadorCliente");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // >>> NUEVO MÉTODO en Factura: El buscador usará esto para meter el nombre aquí <<<
    // Cambia el nombre del método para que coincida exactamente con lo que busca el controlador del buscador
    public void asignarClienteDesdeBuscador(String nombreCliente) {
        if (this.cliente != null) {
            this.cliente.setText(nombreCliente);
        }
    }

    public void eliminarCliente(ActionEvent actionEvent) {
    }

    public void agregarCliente(ActionEvent actionEvent) throws IOException {
        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de clientes
            controladorCliente controller = loader.getController();

            // >>> AHORA SÍ va a reconocer el método porque lo agregamos en el Paso 1 <<<


            // 3. Configurar y mostrar la nueva ventana de forma MODAL (espera a que se cierre)
            Stage stage = new Stage();
            stage.setTitle("Registrar / Seleccionar Cliente");
            stage.setScene(new Scene(root, 440, 540));

            // Usamos showAndWait para que el código de abajo "espere" a que el usuario termine
            stage.showAndWait();

            // 4. Al cerrarse la ventana, verificamos si seleccionó un cliente de esa tabla
            clienteClase clienteSeleccionado = controller.tablaClientes.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado != null) {
                // 5. Mostrar el dato en el Label de la factura
                cliente.setText(clienteSeleccionado.getNombreEntidad());
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