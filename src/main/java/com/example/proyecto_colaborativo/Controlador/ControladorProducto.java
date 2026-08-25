package com.example.proyecto_colaborativo.Controlador;


import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

public class ControladorProducto {

    @FXML
    public TableView<Producto> tablaProductos;
    public Label labelPrecioVenta;

    @FXML
    private TableColumn<Producto, Integer> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TextField codigo;

    @FXML
    private TextField txtbuscadorProductos;
    @FXML
    private Button botonSalir;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/productos";

    // Lista observable que contendrá los productos reales
    // Lista observable única para toda la clase
    public static final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // VARIABLE NUEVA: Guarda el objeto seleccionado para poder modificarlo después
    public static Producto productoseleccionado;
    private ControladorFactura controladorFactura;
    private controladorProveedorSelec controladorProveedorSelec;

    public void setControladorProveedorSelec(controladorProveedorSelec controladorProveedorSelec){
        this.controladorProveedorSelec = controladorProveedorSelec;
    }

    public void setControladorProducto(ControladorFactura controladorFactura) {
        this.controladorFactura = controladorFactura;
    }

    @FXML
    public void initialize() {

        obtenerProductosApi();
        // 3. Vinculamos cada columna con el nombre exacto de la propiedad en la clase Producto

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));


        tablaProductos.setItems(listaProductos);

        // ========================================
        // LLAMADA A LA CLASE REUTILIZABLE
        // ==========================================

        BuscadorUtils.configuradorBuscador(
                txtbuscadorProductos,
                tablaProductos,
                listaProductos,
                (producto, texto) -> {
                    // Validación segura contra valores nulos
                    boolean coincideNombre = producto.getNombre() != null &&
                            producto.getNombre().toLowerCase().contains(texto);

                    boolean coincideCodigo = producto.getCodigoBarra() != null &&
                            producto.getCodigoBarra().toLowerCase().contains(texto);

                    return coincideNombre || coincideCodigo;



                }
        );


        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // ASIGNACIÓN: Guardamos la referencia del producto seleccionado
                ControladorProducto.productoseleccionado = newValue;

                // 'newValue' contiene el objeto Producto seleccionado
                System.out.println("Seleccionaste: " + newValue.getNombre());


            }

        });
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!fila.isEmpty())) {
                    Producto productoSeleccionado = fila.getItem();

                    if (controladorProveedorSelec != null){
                        controladorProveedorSelec.recibirProducto(productoSeleccionado);
                    }
                    if (controladorFactura != null) {
                        controladorFactura.recibirProducto(productoSeleccionado);

                        // Opcional: Cierra la ventana del catálogo automáticamente tras elegir
                        Stage stage = (Stage) tablaProductos.getScene().getWindow();
                        stage.close();
                    }
                }
            });
            return fila;

        });

    }

    @FXML
    private void clickModificar(ActionEvent event) {

        if (this.productoseleccionado == null) {AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto", "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);return;}


        Producto.productoSeleccionadoParaEditar = ControladorProducto.productoseleccionado;

        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Agregar Producto", true);




    }


    @FXML
    private void clickAgregar(ActionEvent event) {


        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Agregar Producto", true);




    }


    public void clickEliminar(ActionEvent event) throws SQLException {
        // 1. Validar que el usuario haya seleccionado un producto de la tabla
        if (productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Error", "Producto no Seleccionado", "Debes seleccionar un producto de la tabla para eliminarlo.", Alert.AlertType.INFORMATION);

            return;
        }


        // 2. Alerta de confirmación visual
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Estás seguro de que querés eliminar este producto?");
        alerta.setContentText("Producto: " + productoseleccionado.getNombre() + "\nEsta acción no se puede deshacer.");

        // 3. Mostrar la alerta y esperar la respuesta del usuario
        java.util.Optional<javafx.scene.control.ButtonType> resultado = alerta.showAndWait();

        // 4. Si el usuario hace clic en OK, se procede a la eliminación
        if (resultado.isPresent() && resultado.get() == javafx.scene.control.ButtonType.OK) {

      //      ProductoDAO.eliminar(productoseleccionado.getidProducto());
            // Elimina físicamente el ítem de la lista de datos


            // 3. Remover el producto de la lista observable global


            eliminarProductoApi(productoseleccionado);
            listaProductos.remove(productoseleccionado);
            tablaProductos.getSelectionModel().clearSelection();
            // 4. Resetear la variable de control
            this.productoseleccionado = null;
            System.out.println("¡Producto eliminado con éxito!");

        } else {
            System.out.println("Eliminación cancelada por el usuario.");
        }

    }

    public void clickSalir(ActionEvent event) {
        // Obtiene la ventana (Stage) actual a partir de cualquier componente de la pantalla
        javafx.stage.Stage stage = (javafx.stage.Stage) botonSalir.getScene().getWindow();

        // Cierra la ventana actual
        stage.close();
    }

/*
    /////
    API
    /////


 */

    private void obtenerProductosApi() {
        Thread apiThread = new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = response.body();

                    // Transformamos el JSON directo a tu clase Cliente con Lombok
                    Producto[] ProductoArray = objectMapper.readValue(json, Producto[].class);

                    // Refrescamos de manera segura la interfaz de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProductos.clear();
                        listaProductos.addAll(ProductoArray);
                        System.out.println("[INFO] ¡Tabla JavaFX actualizada con " + ProductoArray.length + " clientes reales!");
                    });

                    System.out.println("JSON Puro Recibido: " + json);

                } else {
                    System.out.println("[ERROR] La API respondió con código: " + response.statusCode());
                }

            } catch (Exception e) {
                System.out.println("[ERROR] No se pudo conectar o parsear la data de la API.");
                e.printStackTrace();
            }

        });
        apiThread.setDaemon(true);

        // 6. Iniciamos la ejecución del hilo
        apiThread.start();

    }



    private void eliminarProductoApi(Producto producto) {

        if (producto == null) return;
        Thread putThread = new Thread(() -> {
        try {

            String urlDestino = API_URL + "/" + producto.getidProducto();
            System.out.println("[DEBUG] Enviando petición DELETE a: " + urlDestino);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlDestino))
                    .DELETE()
                    .build();

            // 3. Enviar la solicitud
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Tu API de Spring Boot responde con 24 (No Content) al eliminar con éxito
            if (response.statusCode() == 204 || response.statusCode() == 200) {
                System.out.println("[INFO] Cliente eliminado de la API con éxito.");

                // 5. Remover el cliente visualmente de la tabla en el hilo de JavaFX
                javafx.application.Platform.runLater(() -> {
                    listaProductos.remove(producto);
                    tablaProductos.getSelectionModel().clearSelection();
                });
            } else {
                System.out.println("[ERROR] No se pudo eliminar. Código API: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Error crítico en el hilo de eliminación.");
            e.printStackTrace();
        }
    });

        putThread.setDaemon(true);
        putThread.start();

    }




    private void agregarProductoApi(Producto nuevoProducto) {
        if (nuevoProducto == null) return;

        // 1. Crear el hilo para no congelar la pantalla
        Thread postThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 2. Convertir tu objeto Java a texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(nuevoProducto);

                // 3. Construir la petición POST
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json") // Avisamos a Spring Boot que va un JSON
                        .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody)) // Verbo POST con el body
                        .build();

                // 4. Enviar los datos a la API
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 5. Tu API responde con 201 (Created) si el alta fue exitosa
                if (response.statusCode() == 201 || response.statusCode() == 200) {
                    String jsonRespuesta = response.body();

                    // Parseamos el cliente definitivo devuelto por la API
                    Producto producto = objectMapper.readValue(jsonRespuesta, Producto.class);

                    System.out.println("[INFO] Cliente agregado a la API con éxito.");

                    // 6. Impactar el cambio de forma segura en la interfaz visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProductos.add(producto); // Se dibuja solo en la TableView
                    });

                } else {
                    System.out.println("[ERROR] No se pudo agregar. Código API: " + response.statusCode());
                }

            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico en el hilo de alta.");
                e.printStackTrace();
            }
        });

        postThread.setDaemon(true);
        postThread.start();
    }

    private void modificarProductoApi(Producto productoModificado) {
        if (productoModificado == null) return;

        Thread putThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 1. Convertir el objeto Java actualizado a un texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(productoModificado);

                // 2. Construir la petición PUT incluyendo el JSON en el cuerpo
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json") // Obligatorio para indicarle a Spring Boot que mandas un JSON
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody)) // Verbo PUT con su Body
                        .build();

                // 3. Enviar la solicitud
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 4. Tu API responde con 200 OK y devuelve el cliente modificado
                if (response.statusCode() == 200) {
                    String jsonRespuesta = response.body();
                    Producto clienteActualizadoApi = objectMapper.readValue(jsonRespuesta, Producto.class);

                    System.out.println("[INFO] Cliente modificado en la API con éxito.");

                    // 5. Refrescar la interfaz visual de JavaFX de forma segura
                    javafx.application.Platform.runLater(() -> {
                        // Buscamos el índice actual en la lista y lo reemplazamos por el actualizado de la API
                        int index = listaProductos.indexOf(productoModificado);
                        if (index >= 0) {
                            listaProductos.set(index, clienteActualizadoApi);
                        }
                        // Fuerza el redibujado de las celdas
                        //asd
                    });
                } else {
                    System.out.println("[ERROR] No se pudo modificar. Código API: " + response.statusCode());
                }

            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico en el hilo de modificación.");
                e.printStackTrace();
            }
        });

        putThread.setDaemon(true);
        putThread.start();
    }

    public void calcularPrecio(ActionEvent actionEvent) {
    }
}
