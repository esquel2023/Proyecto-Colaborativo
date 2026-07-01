package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.HelloApplication;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import com.example.proyecto_colaborativo.bd.ProductoProveedorDAO;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.List;

public class controladorProveedorSelec {

    private static controladorProveedorSelec instanciaActiva;

    @FXML
    public Button botonAgregar;
    @FXML
    public Button botonEliminar;
    @FXML
    public TableView<Producto> tablaProductosProovedor;
    @FXML
    public TableColumn<Producto, String> prooductosProovedor;
    @FXML
    public TableColumn<Producto, Double> precioProovedor;
    @FXML
    public TableColumn<Producto, String> prooductosProovedor1;

    @FXML
    private Label proveedorSelec;

    private final ObservableList<Producto> listaProductosObs = FXCollections.observableArrayList();
    private proovedorClase proveedorActual;
    private Producto productoseleccionado;


    @FXML
    public void initialize() {
        instanciaActiva = this;

        // CORREGIDO: Deben mapear las variables reales de tu objeto Producto (ver tu ProductoDAO)
        prooductosProovedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioProovedor.setCellValueFactory(new PropertyValueFactory<>("precio"));
        prooductosProovedor1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));


        tablaProductosProovedor.setItems(listaProductosObs);
        tablaProductosProovedor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           this.productoseleccionado = newValue;
            configurarTablaEditable();
        });
    }
    private void configurarTablaEditable() {
        // 1. Permitir que la tabla acepte edición
        tablaProductosProovedor.setEditable(true);

        // 3. Hacer editable la columna Precio (usa DoubleStringConverter)
        precioProovedor.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        precioProovedor.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Double nuevoPrecio = event.getNewValue();
            if (nuevoPrecio != null && nuevoPrecio > 0) {
                p.setPrecio(nuevoPrecio);
                p.precioProperty().set(nuevoPrecio);

            } else {
                tablaProductosProovedor.refresh(); // Revierte el cambio visual si es inválido
            }
        });
    }

    public static void setProveedorSelec(proovedorClase proveedor) {
        if (instanciaActiva != null && proveedor != null) {
            instanciaActiva.proveedorActual = proveedor;

            if (instanciaActiva.proveedorSelec != null) {
                instanciaActiva.proveedorSelec.setText(proveedor.getNombreEntidad());
            }

            // Refrescamos la tabla automáticamente con el ID real del proveedor
            instanciaActiva.actualizarTabla(proveedor.getId());
        }
    }

    private void actualizarTabla(int idProveedor) {
        // Buscamos la lista en la base de datos usando el método del DAO
        List<Producto> listaBD = ProductoProveedorDAO.listar(idProveedor);
        // Actualizamos la lista observable de JavaFX
        listaProductosObs.setAll(listaBD);
    }

    @FXML
    public void botonAgregar(ActionEvent actionEvent) {
        if (proveedorActual == null) return;

        // 1. Listamos todos los productos de la base de datos
        List<Producto> todosLosProductos = ProductoDAO.listar();

        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador DESPUÉS de cargar el root
            ControladorProducto controller = loader.getController();
            controller.setControladorProveedorSelec(this);


            // 3. Configurar y mostrar la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("buscadorCliente");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    @FXML
    public void botonElimina(ActionEvent actionEvent) {
        desasociarProducto(productoseleccionado);

    }

    public void recibirProducto(Producto producto) {
        if (producto != null) {
            listaProductosObs.add(producto);
           ProductoProveedorDAO.asociar(producto.getidProducto(), proveedorActual.getId());

            }
        }
        public void desasociarProducto(Producto producto) {
        if (producto != null){
            listaProductosObs.remove(producto);
            ProductoProveedorDAO.desasociar(producto.getidProducto(), proveedorActual.getId());
        }
        }
    }
