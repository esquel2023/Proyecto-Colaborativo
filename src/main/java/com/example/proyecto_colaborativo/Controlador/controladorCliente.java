package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class controladorCliente {

    //   public Label totalclientes;


    public Button modificarCliente;
    public Button eliminarCliente;
    public TextField buscadorClientes;


    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();

    // Cambia la definición de tus componentes FXML
    @FXML
    TableView<clienteClase> tablaClientes;
    @FXML private TableColumn<clienteClase, String> nombreTabla;
    @FXML private TableColumn<clienteClase, String> telefonoTabla;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/clientes";

    @FXML
    public void initialize() {
        if (nombreTabla != null && telefonoTabla != null) {

            // CORRECCIÓN AQUÍ: Pon exactamente los nombres de las variables de tu "clienteClase"
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

            // Vincular la lista a la tabla visual
            tablaClientes.setItems(listaClientesObs);

            // Llamar a la API
            obtenerClientesApi();

            // Listener de selección optimizado
            tablaClientes.getSelectionModel().selectedItemProperty().addListener((_, _, clienteSeleccionado) -> {
                if (clienteSeleccionado == null) {
                    listaFacturasObs.clear();
                }
            });
        }
    }



    private void obtenerClientesApi() {
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
                    clienteClase[] clientesArray = objectMapper.readValue(json, clienteClase[].class);

                    // Refrescamos de manera segura la interfaz de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaClientesObs.clear();
                        listaClientesObs.addAll(clientesArray);
                        System.out.println("[INFO] ¡Tabla JavaFX actualizada con " + clientesArray.length + " clientes reales!");
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


    void buscadorClientes() {
        String buscar = buscadorClientes.getText().toLowerCase();
        if (!buscar.isEmpty()) {
            BuscadorUtils.configuradorBuscador(
                    buscadorClientes,
                    tablaClientes,
                    listaClientesObs,
                    (cliente, texto) -> {
                        // Validación segura contra valores nulos
                        boolean coincideNombre = cliente.getNombreEntidad() != null &&
                                cliente.getNombreEntidad().toLowerCase().contains(texto);

                        return coincideNombre;

                    });

        }
    }

    public void modificarCliente(ActionEvent actionEvent) {

        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            AlertasUtils.mostrarError("Atención", "Por favor, selecciona un cliente de la tabla para modificar.");
            return;
        }


        controladorModifCliente ctrl = NavegacionUtils.abrirPantalla("modificarCliente.fxml", "Modificacion de cliente seleccionado", false);
        assert ctrl != null;
        ctrl.rellenarCampos(clienteSeleccionado);

    }


    public void eliminarCliente(ActionEvent actionEvent) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {
                // Se envía el nombre como cadena de texto directo a la BD
          //      ClienteDAO.eliminar(clienteSeleccionado.getNombreEntidad());
                eliminarClienteApi(clienteSeleccionado);

                // Se remueve de la interfaz visual
                listaClientesObs.remove(clienteSeleccionado);
                System.out.println("Cliente eliminado con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void agregarCliente(ActionEvent actionEvent) {
        controladorAgregarCliente ctrlAC = NavegacionUtils.abrirPantalla("agregarCliente.fxml", "Nuevo Cliente", false);

    }
    private void eliminarClienteApi(clienteClase clienteAEliminar) {
        if (clienteAEliminar == null) return;

        Thread deleteThread = new Thread(() -> {
            try {
                // 1. Armar la URL dinámica con el identificador del cliente (su nombre)
                // Se codifica por si el nombre tiene espacios (ej: "Juan Perez")
                String nombrePuro = clienteAEliminar.getNombreEntidad();
                String nombreCodificado = java.net.URLEncoder.encode(nombrePuro, "UTF-8")
                        .replaceAll("\\+", "%20");
                String urlEliminar = "http://localhost:8080/tienda/api/v1/clientes/" + nombreCodificado;


                // 2. Construir la petición DELETE
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlEliminar))
                        .DELETE()
                        .build();

                // 3. Enviar la solicitud
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 4. Tu API de Spring Boot responde con 24 (No Content) al eliminar con éxito
                if (response.statusCode() == 204 || response.statusCode() == 200) {
                    System.out.println("[INFO] Cliente eliminado de la API con éxito.");

                    // 5. Remover el cliente visualmente de la tabla en el hilo de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaClientesObs.remove(clienteAEliminar);
                        tablaClientes.getSelectionModel().clearSelection();
                    });
                } else {
                    System.out.println("[ERROR] No se pudo eliminar. Código API: " + response.statusCode());
                }

            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico en el hilo de eliminación.");
                e.printStackTrace();
            }
        });

        deleteThread.setDaemon(true);
        deleteThread.start();
    }



}

