package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.HelloApplication;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import com.example.proyecto_colaborativo.bd.ProductoProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.util.List;

public class controladorProveedorSelec {

    private static controladorProveedorSelec instanciaActiva;

    @FXML public TableView<Producto> tablaProductosProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor;
    @FXML public TableColumn<Producto, Double> precioProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor1;

    @FXML public TextField nombreProveedor;
    @FXML public TextField cuitProveedor;
    @FXML public TextField correoProveedor;
    @FXML public TextField telefonoProveedor;
    @FXML public TextField paisProveedor;
    @FXML public TextField provinciaProveedor;
    @FXML public TextField localidadProveedor;
    @FXML public TextField txtBuscar;

    @FXML private Label proveedorSelec;

    private final ObservableList<Producto> listaProductosObs = FXCollections.observableArrayList();
    private proovedorClase proveedorActual;
    private Producto productoseleccionado;

    @FXML
    public void initialize() {
        instanciaActiva = this;

        // 1. Configuración segura de la estructura de la tabla al arrancar
        if (prooductosProovedor != null && precioProovedor != null && prooductosProovedor1 != null) {
            prooductosProovedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            precioProovedor.setCellValueFactory(new PropertyValueFactory<>("precio"));
            prooductosProovedor1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

            tablaProductosProovedor.setItems(listaProductosObs);

            tablaProductosProovedor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                this.productoseleccionado = newValue;
                configurarTablaEditable();
            });
        }
    }
    public void setProveedorActual(proovedorClase proveedor) {
        if (proveedor == null) return;

        this.proveedorActual = proveedor;

        if (this.proveedorSelec != null) {
            this.proveedorSelec.setText(proveedor.getNombreEntidad());
        }

        if (nombreProveedor != null) nombreProveedor.setText(proveedor.getNombreEntidad());
        if (cuitProveedor != null) cuitProveedor.setText(proveedor.getCuitcuilEntidad());
        if (correoProveedor != null) correoProveedor.setText(proveedor.getEmailEntidad());
        if (telefonoProveedor != null) telefonoProveedor.setText(proveedor.getTelefonoEntidad());
        if (paisProveedor != null) paisProveedor.setText(proveedor.getPais());
        if (provinciaProveedor != null) provinciaProveedor.setText(proveedor.getProvincia());
        if (localidadProveedor != null) localidadProveedor.setText(proveedor.getCiudad());

        // Cargar los productos de este proveedor directo desde la Base de Datos
        actualizarTabla(proveedor.getId());
    }

    // Mantiene compatibilidad con tu llamado alternativo anterior
    public static void setProveedorSelec(proovedorClase proveedor) {
        if (instanciaActiva != null && proveedor != null) {
            instanciaActiva.setProveedorActual(proveedor);
        }
    }

    private void actualizarTabla(int idProveedor) {
        List<Producto> listaBD = ProductoProveedorDAO.listar(idProveedor);
        listaProductosObs.setAll(listaBD);
    }

    private void configurarTablaEditable() {
        tablaProductosProovedor.setEditable(true);
        precioProovedor.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        precioProovedor.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Double nuevoPrecioCosto = event.getNewValue();

            if (nuevoPrecioCosto != null && nuevoPrecioCosto >= 0 && p != null && proveedorActual != null) {
                p.setPrecio(nuevoPrecioCosto);
                p.precioProperty().set(nuevoPrecioCosto);
                ProductoProveedorDAO.actualizarPrecioCosto(p.getidProducto(), proveedorActual.getId(), nuevoPrecioCosto);
            } else {
                tablaProductosProovedor.refresh();
            }
        });
    }

    public void recibirProducto(Producto producto) {
        if (producto != null) {
            listaProductosObs.add(producto);
            ProductoProveedorDAO.asociar(producto.getidProducto(), proveedorActual.getId(), 0.0);
            tablaProductosProovedor.refresh();
        }
    }

    public void desasociarProducto(Producto producto) {
        if (producto != null && proveedorActual != null) {
            listaProductosObs.remove(producto);
            ProductoProveedorDAO.desasociar(producto.getidProducto(), proveedorActual.getId());
        }
    }

    @FXML
    public void botonAgregarProducto(ActionEvent actionEvent) {
        if (proveedorActual == null) return;
        List<Producto> todosLosProductos = ProductoDAO.listar();

        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Parent root = loader.load();
            ControladorProducto controller = loader.getController();
            controller.setControladorProveedorSelec(this);

            Stage stage = new Stage();
            stage.setTitle("Buscador de Productos");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void botonModificarProveedor(ActionEvent actionEvent) {}
    @FXML public void botonEliminarProveedor(ActionEvent actionEvent) {}
    @FXML public void buscarProducto(ActionEvent actionEvent) {}
    @FXML public void botonModificarProducto(ActionEvent actionEvent) {}

    @FXML
    public void botonEliminarProducto(ActionEvent actionEvent) {
        if (productoseleccionado != null) {
            desasociarProducto(productoseleccionado);
        }
    }
}
