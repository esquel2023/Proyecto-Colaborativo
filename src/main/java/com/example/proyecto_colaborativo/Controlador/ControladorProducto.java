package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ControladorProducto {

    @FXML public TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TextField txtbuscadorProductos;
    @FXML private Button botonSalir;
    @FXML private TextField codigo;

    private static final String API_URL = "http://localhost:8080/tienda/api/v1/productos/fake-productos";
    public static final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    public static Producto productoseleccionado;

    private ControladorFactura controladorFactura;
    private controladorProveedorSelec controladorProveedorSelec;

    public void setControladorProveedorSelec(controladorProveedorSelec controladorProveedorSelec) {
        this.controladorProveedorSelec = controladorProveedorSelec;
    }

    public void setControladorProducto(ControladorFactura controladorFactura) {
        this.controladorFactura = controladorFactura;
    }

    @FXML
    public void initialize() {
        tablaProductos.setItems(listaProductos);
        traerProductosDesdeBackend();

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        BuscadorUtils.configuradorBuscador(
                txtbuscadorProductos,
                tablaProductos,
                listaProductos,
                (producto, texto) -> {
                    boolean coincideNombre = producto.getNombre() != null &&
                            producto.getNombre().toLowerCase().contains(texto);
                    boolean coincideCodigo = producto.getCodigoBarra() != null &&
                            producto.getCodigoBarra().toLowerCase().contains(texto);
                    return coincideNombre || coincideCodigo;
                }
        );

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ControladorProducto.productoseleccionado = newValue;
                System.out.println("Seleccionaste de la API: " + newValue.getNombre());
            }
        });

        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!fila.isEmpty())) {
                    Producto pSeleccionado = fila.getItem();
                    if (controladorProveedorSelec != null) controladorProveedorSelec.recibirProducto(pSeleccionado);
                    if (controladorFactura != null) {
                        controladorFactura.recibirProducto(pSeleccionado);
                        Stage stage = (Stage) tablaProductos.getScene().getWindow();
                        stage.close();
                    }
                }
            });
            return fila;
        });
    }

    private void traerProductosDesdeBackend() {
        Task<List<Producto>> task = new Task<> () {
            @Override
            protected List<Producto> call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Producto>>() {});
            }
        };

        task.setOnSucceeded(event -> {
            listaProductos.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            System.err.println("Error al conectar con la API de Spring Boot: " + error.getMessage());
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void clickAgregar(ActionEvent event) {
        Producto.productoSeleccionadoParaEditar = null;
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Agregar Nuevo Producto", true);
        traerProductosDesdeBackend(); // Refresca la tabla al cerrar
    }

    @FXML
    private void clickModificar(ActionEvent event) {
        if (ControladorProducto.productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto",
                    "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);
            return;
        }
        Producto.productoSeleccionadoParaEditar = ControladorProducto.productoseleccionado;
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Modificar Producto", true);
        traerProductosDesdeBackend(); // Refresca la tabla al regresar
    }

    @FXML
    private void clickEliminar(ActionEvent event) {
        if (ControladorProducto.productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto",
                    "Seleccioná una fila para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        System.out.println("Eliminar: " + ControladorProducto.productoseleccionado.getNombre());
    }

    @FXML
    private void clickSalir(ActionEvent event) {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
    }

    private void cargarDatosDesdeBD() {
        // Tu método de respaldo local
    }
}
