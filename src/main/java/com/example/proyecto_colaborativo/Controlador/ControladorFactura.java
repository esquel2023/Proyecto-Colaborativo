package com.example.proyecto_colaborativo.Controlador;
import com.example.proyecto_colaborativo.*;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
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
/*
   public void buscarCliente(ActionEvent actionEvent) {
        controladorCliente cliente = new controladorCliente();
        BuscadorUtils.configuradorBuscador(
               nombreYApellido,
              cliente.tablaClientes,
              cliente.tablaClientes.getItems(),
                (producto,texto)->{
                    // Validación segura contra valores nulos
                    boolean coincideNombre = cliente.get() != null &&
                            cliente.getNombreEntidad().toLowerCase().contains(texto);

                    boolean coincideCodigo = producto.getCodigoBarra() != null &&
                            producto.getCodigoBarra().toLowerCase().contains(texto);

                    return coincideNombre || coincideCodigo;



                    // Acá definís la lógica específica para la clase Producto
                    // return producto.getNombre().toLowerCase().contains(texto) ||
                    //         producto.getCodigoBarra().toLowerCase().contains(texto);

                }
        );



 */

    }/*

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
                cliente.setText(clienteSeleccionado.getNombreEntidad());
                List<clienteClase> datosBD1 = clienteDAO.listar();
                listaUsuarios.addAll();
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
    }*/

