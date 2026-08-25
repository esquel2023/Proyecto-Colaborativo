package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private static final String API_URL = "http://localhost:8080/tienda/api/v1/productos";
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
        // 1. Validamos que haya un producto seleccionado en la tabla
        if (ControladorProducto.productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto",
                    "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);
            return;
        }

        // 2. Pasamos el producto seleccionado a la variable estática de edición para que el formulario la lea
        Producto.productoSeleccionadoParaEditar = ControladorProducto.productoseleccionado;

        // 3. ✅ ABRIMOS EL FORMULARIO DE EDICIÓN (Quitamos la Task HTTP incorrecta de acá)
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Modificar Producto", true);

        // 4. Cuando el usuario termine de guardar en la otra pantalla y esta ventana recupere el foco:
        traerProductosDesdeBackend(); // Volvemos a pedir la lista al backend para mostrar los cambios reales
        ControladorProducto.productoseleccionado = null;
        tablaProductos.getSelectionModel().clearSelection();
    }


    @FXML
    private void clickEliminar(ActionEvent event) {
        Producto seleccionado = ControladorProducto.productoseleccionado;
        if (seleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto", "Seleccioná una fila para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // 1. Definimos la Task para ejecutar el DELETE en segundo plano
        Task<Integer> taskEliminar = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/" + seleccionado.getidProducto()))
                        .DELETE()
                        .build();
                return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
            }
        };

            // 2. Qué pasa si la petición sale bien
            taskEliminar.setOnSucceeded(e -> {
                int codigo = taskEliminar.getValue();
                if (codigo == 204 || codigo == 200) {
                    listaProductos.remove(seleccionado); // Lo borra visualmente de la tabla al instante
                    ControladorProducto.productoseleccionado = null;
                    tablaProductos.getSelectionModel().clearSelection();
                    AlertasUtils.mostrarAlerta("Éxito", "Eliminado", "El producto fue borrado correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    AlertasUtils.mostrarAlerta("Error", "No se pudo borrar", "El servidor respondió con código: " + codigo, Alert.AlertType.ERROR);
                }
            });

            // 3. Qué pasa si el servidor está apagado o falla la red
            taskEliminar.setOnFailed(e -> AlertasUtils.mostrarAlerta("Error", "Falla de red", "No se pudo conectar con el servidor.", Alert.AlertType.ERROR));

            // 4. Arrancamos el hilo secundario de forma segura
            Thread thread = new Thread(taskEliminar);
            thread.setDaemon(true);
            thread.start();
        }

        @FXML
        private void clickSalir (ActionEvent event){
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        }

        private void cargarDatosDesdeBD () {
            // Tu método de respaldo local
        }
    }


