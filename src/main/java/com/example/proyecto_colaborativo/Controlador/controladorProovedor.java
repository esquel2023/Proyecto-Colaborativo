package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class controladorProovedor {

    @FXML
    private TextField nombre;

    @FXML
    private TextField cuit;

    @FXML
    private TextField email;

    @FXML
    private TextField telefono;

    @FXML
    private ComboBox<String> condicionIVA;

    @FXML
    private TextField pais;

    @FXML
    private TextField provincia;

    @FXML
    private TextField localidad;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/proveedores";


    @FXML
    private TextField txtBuscar;

    @FXML
    private TableView<proovedorClase> tablaProovedores;

    @FXML
    private TableColumn<proovedorClase, String> nombreTabla;

    @FXML
    private TableColumn<proovedorClase, String> telefonoTabla;

    @FXML
    private TableColumn<proovedorClase, String> colEmail;


    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonModificar;

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonBuscar;

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaLabel;

    private final ObservableList<proovedorClase> listaProveedores =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarProveedores();

        configurarComboIVA();

        configurarSeleccionTabla();

        obtenerProveedorsApi();

    //    iniciarReloj();
    }

    private void configurarTabla() {

        if (nombreTabla != null && colEmail != null && telefonoTabla != null) {
            nombreTabla.setCellValueFactory(
                    new PropertyValueFactory<>("nombreEntidad")
            );

            telefonoTabla.setCellValueFactory(
                    new PropertyValueFactory<>("telefonoEntidad")
            );

            colEmail.setCellValueFactory(
                    new PropertyValueFactory<>("emailEntidad")
            );
        }

        tablaProovedores.setPlaceholder(
                new Label("No hay proveedores cargados")
        );

        tablaProovedores.setItems(listaProveedores);
    }

    private void cargarProveedores() {

        listaProveedores.clear();

        listaProveedores.addAll(

        );
    }


    private void configurarComboIVA() {

        condicionIVA.setItems(
                FXCollections.observableArrayList(
                        "Responsable Inscripto",
                        "Monotributista",
                        "Consumidor Final",
                        "Exento"
                )
        );
    }


    private void configurarSeleccionTabla() {
        tablaProovedores // Corregido typo de tablaProovedores a tablaProveedores
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        // Datos básicos
                        nombre.setText(seleccionado.getNombreEntidad());
                        telefono.setText(seleccionado.getTelefonoEntidad());
                        email.setText(seleccionado.getEmailEntidad());
                        cuit.setText(seleccionado.getCuitcuilEntidad());

                        // Nuevos campos de ubicación (asumiendo estos getters en tu clase)
                        pais.setText(seleccionado.getPais());
                        provincia.setText(seleccionado.getProvincia());
                        localidad.setText(seleccionado.getCiudad());
                    }
                });
    }

    @FXML
    private void botonAgrega(ActionEvent event) throws IOException {

        String txtNombre = nombre.getText().trim();
        String txtTelefono = telefono.getText().trim();
        String txtEmail = email.getText().trim();
        String txtCuit = cuit.getText().trim();
        String txtPais = pais.getText().trim();
        String txtProvincia = provincia.getText().trim();
        String txtCiudad = localidad.getText().trim();

        if (txtNombre.isEmpty()
                || txtTelefono.isEmpty()
                || txtEmail.isEmpty()
                || txtCuit.isEmpty()) {

            AlertasUtils.mostrarInformacion(
                    "Campos vacíos",
                    "Complete Nombre, CUIT, Correo y Teléfono."
            );

            return;
        }


        String mensaje =
                "¿Confirmás los datos del proveedor?\n\n"
                        + "Nombre: " + txtNombre + "\n"
                        + "Teléfono: " + txtTelefono + "\n"
                        + "Email: " + txtEmail + "\n"
                        + "CUIT: " + txtCuit;


        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle("Confirmación de proveedor");

        alerta.setHeaderText("Revisá los datos antes de guardar");

        alerta.setContentText(mensaje);


        ButtonType confirmar = new ButtonType("Confirmar");

        ButtonType cancelar = new ButtonType("Cancelar");


        alerta.getButtonTypes().setAll(
                confirmar,
                cancelar
        );


        Optional<ButtonType> resultado = alerta.showAndWait();


        if (resultado.isPresent() && resultado.get() == confirmar) {
                    cargarProveedores();
                    limpiarCampos();

            try {
                proovedorClase nuevoProveedor = new proovedorClase();

                nuevoProveedor.setNombreEntidad(txtNombre);
                nuevoProveedor.setTelefonoEntidad(txtTelefono);
                nuevoProveedor.setEmailEntidad(txtEmail);
                nuevoProveedor.setCuitcuilEntidad(txtCuit);
                nuevoProveedor.setPais(txtPais);
                nuevoProveedor.setProvincia(txtProvincia);
                nuevoProveedor.setCiudad(txtCiudad);


                //  ClienteDAO.insertar(nuevoCliente);
                agregarProveedorApi(nuevoProveedor);

                limpiarCampos();
                System.out.println(" Proveedor agregado con éxito.");


            } catch (Exception e) {
                System.out.println("Error al intentar procesar e insertar el Proveedor.");
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void botonModifica(ActionEvent event) {

        proovedorClase proveedorSeleccionado = tablaProovedores.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado == null) {
            AlertasUtils.mostrarInformacion("Proveedor", "Seleccione un proveedor de la tabla.");
            return;
        }
        String txtNombre = nombre.getText().trim();
        String txtTelefono = telefono.getText().trim();
        String txtEmail = email.getText().trim();
        String txtCuit = cuit.getText().trim();
        String txtPais = pais.getText().trim();
        String txtProvincia = provincia.getText().trim();
        String txtCiudad = localidad.getText().trim();


        if (txtNombre.isEmpty() || txtTelefono.isEmpty() || txtEmail.isEmpty() || txtCuit.isEmpty()) {
            AlertasUtils.mostrarInformacion("Campos vacíos", "No puede dejar campos vacíos.");
            return;
        }

        proovedorClase proveedorAEditar = new proovedorClase();
        proveedorAEditar.setId(proveedorSeleccionado.getId());
        proveedorAEditar.setNombreEntidad(txtNombre);
        proveedorAEditar.setTelefonoEntidad(txtTelefono);
        proveedorAEditar.setEmailEntidad(txtEmail);
        proveedorAEditar.setCuitcuilEntidad(txtCuit);
        proveedorAEditar.setPais(txtPais);
        proveedorAEditar.setProvincia(txtProvincia);
        proveedorAEditar.setCiudad(txtCiudad);

        modificarProveedorApi(proveedorAEditar);
    }

    @FXML
    private void botonEliminar(ActionEvent event) {

        proovedorClase proveedorSeleccionado = tablaProovedores.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado == null) {
            AlertasUtils.mostrarInformacion("Proveedor", "Seleccione un proveedor para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar proveedor");
        alerta.setHeaderText("¿Desea eliminar este proveedor?");
        alerta.setContentText(proveedorSeleccionado.getNombreEntidad());

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // En lugar de borrarlo localmente aquí, llamamos al hilo de la API
            eliminarProveedorApi(proveedorSeleccionado);
        }
    }

    @FXML
    private void buscarProveedor(ActionEvent event) {

        String texto =
                txtBuscar
                        .getText()
                        .trim()
                        .toLowerCase();


        if (texto.isEmpty()) {

            tablaProovedores.setItems(
                    listaProveedores
            );

            return;
        }

        ObservableList<proovedorClase> filtrados =
                FXCollections.observableArrayList();


        for (proovedorClase proveedor
                : listaProveedores) {

            boolean coincideNombre =
                    proveedor.getNombreEntidad() != null
                            && proveedor
                            .getNombreEntidad()
                            .toLowerCase()
                            .contains(texto);


            boolean coincideTelefono =
                    proveedor.getTelefonoEntidad() != null
                            && proveedor
                            .getTelefonoEntidad()
                            .toLowerCase()
                            .contains(texto);


            boolean coincideEmail =
                    proveedor.getEmailEntidad() != null
                            && proveedor
                            .getEmailEntidad()
                            .toLowerCase()
                            .contains(texto);


            if (coincideNombre
                    || coincideTelefono
                    || coincideEmail) {

                filtrados.add(proveedor);
            }
        }

        tablaProovedores.setItems(
                filtrados
        );
    }

    private void limpiarCampos() {

        nombre.clear();

        cuit.clear();

        telefono.clear();

        email.clear();

        pais.clear();

        provincia.clear();

        localidad.clear();

        condicionIVA.getSelectionModel()
                .clearSelection();
    }
/*
    private void iniciarReloj() {

        Locale locale =
                new Locale("es", "AR");


        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "EEEE dd 'de' MMMM 'de' yyyy",
                        locale
                );


        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                );

        Timeline reloj =
                new Timeline(

                        new KeyFrame(
                                Duration.ZERO,

                                event -> {

                                    LocalDateTime ahora =
                                            LocalDateTime.now();

                                    String fecha =
                                            ahora.format(
                                                    formatoFecha
                                            );

                                    if (!fecha.isEmpty()) {

                                        fecha =
                                                fecha.substring(0, 1)
                                                        .toUpperCase()
                                                        + fecha.substring(1);
                                    }
                                    fechaLabel.setText(
                                            fecha
                                    );


                                    horaLabel.setText(
                                            ahora.format(
                                                    formatoHora
                                            )
                                    );
                                }
                        ),

                        new KeyFrame(
                                Duration.seconds(1)
                        )
                );

        reloj.setCycleCount(
                Timeline.INDEFINITE
        );

        reloj.play();
    }


 */

    /*
        /////////////
            API
        /////////////

     */
    private void eliminarProveedorApi(proovedorClase proveedorAEliminar) {
        if (proveedorAEliminar == null) return;

        Thread deleteThread = new Thread(() -> {
            try {
                // 1. Armar la URL dinámica con el ID del proveedor
                // Reemplazamos la lógica del nombre por el ID numérico directo
                String urlEliminar = "http://localhost:8080/tienda/api/v1/proveedores/" + proveedorAEliminar.getId();

                // 2. Construir la petición DELETE
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlEliminar))
                        .DELETE()
                        .build();

                // 3. Enviar la solicitud a la API
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 4. Tu API de Spring Boot responde con 204 (No Content) o 200 (OK) al eliminar con éxito
                if (response.statusCode() == 204 || response.statusCode() == 200) {
                    System.out.println("[INFO] Proveedor eliminado de la API con éxito.");

                    // 5. Remover el proveedor visualmente de la tabla en el hilo de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedores.remove(proveedorAEliminar);
                        tablaProovedores.getSelectionModel().clearSelection(); // Corregido typo: tablaProveedores
                        limpiarCampos(); // Opcional: limpia el formulario tras borrar
                    });
                } else {
                    System.out.println("[ERROR] No se pudo eliminar. Código API: " + response.statusCode());
                    javafx.application.Platform.runLater(() -> {
                        AlertasUtils.mostrarInformacion("Error de API", "El servidor no pudo eliminar el registro. Código: " + response.statusCode());
                    });
                }

            } catch (java.net.ConnectException e) {
                System.out.println("[ERROR] No se pudo conectar al servidor para eliminar.");
                javafx.application.Platform.runLater(() -> {
                    AlertasUtils.mostrarInformacion("Error de Conexión", "No se pudo conectar con el backend.");
                });
            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico en el hilo de eliminación.");
                e.printStackTrace();
            }
        });

        deleteThread.setDaemon(true);
        deleteThread.start();
    }



    private void obtenerProveedorsApi() {
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
                    proovedorClase[] clientesArray = objectMapper.readValue(json, proovedorClase[].class);

                    // Refrescamos de manera segura la interfaz de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedores.clear();
                        listaProveedores.addAll(clientesArray);
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


    private void agregarProveedorApi(proovedorClase nuevoProveedor) {
        if (nuevoProveedor == null) return;

        // 1. Crear el hilo para no congelar la pantalla
        Thread postThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 2. Convertir tu objeto Java a texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(nuevoProveedor);

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
                    proovedorClase clienteCreadoApi = objectMapper.readValue(jsonRespuesta, proovedorClase.class);

                    System.out.println("[INFO] Cliente agregado a la API con éxito.");

                    // 6. Impactar el cambio de forma segura en la interfaz visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedores.add(clienteCreadoApi); // Se dibuja solo en la TableView
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


    private void modificarProveedorApi(proovedorClase proveedorModificado) {
        if (proveedorModificado == null) return;

        // 1. Crear el hilo para no congelar la pantalla
        Thread putThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 2. Convertir tu objeto Java a texto JSON
                String jsonRequestBody = objectMapper.writeValueAsString(proveedorModificado);

                // Construir la URL dinámica apuntando al ID esperado por @PathVariable en Spring Boot
                String urlConId = API_URL + "/" + proveedorModificado.getId();

                // 3. Construir la petición PUT
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlConId))
                        .header("Content-Type", "application/json") // Avisamos a Spring Boot que va un JSON
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody)) // Verbo PUT con el cuerpo
                        .build();

                // 4. Enviar los datos a la API
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 5. Spring Boot responde con 200 (OK) al modificar con éxito
                if (response.statusCode() == 200) {
                    String jsonRespuesta = response.body();

                    // Parseamos el proveedor definitivo devuelto por la API
                    proovedorClase proveedorActualizadoApi = objectMapper.readValue(jsonRespuesta, proovedorClase.class);

                    // 6. Impactar el cambio de forma segura en la interfaz visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        System.out.println("[INFO] Proveedor modificado en la API con éxito.");

                        // Buscamos el proveedor viejo en la lista por su ID y lo reemplazamos
                        for (int i = 0; i < listaProveedores.size(); i++) {
                            if (listaProveedores.get(i).getId() == proveedorActualizadoApi.getId()) {
                                listaProveedores.set(i, proveedorActualizadoApi); // Refresca automáticamente la fila en la TableView
                                break;
                            }
                        }

                        // Acciones de limpieza visual sincronizadas con el éxito del servidor
                        tablaProovedores.refresh();
                        tablaProovedores.getSelectionModel().clearSelection();
                        limpiarCampos();
                    });
                } else {
                    String errorBody = response.body();
                    int statusCode = response.statusCode();

                    System.out.println("[ERROR] No se pudo modificar. Código API: " + statusCode);
                    System.out.println("[DETALLE API]: " + errorBody); // Esto te dirá el error real en tu consola de JavaFX

                    javafx.application.Platform.runLater(() -> {
                        AlertasUtils.mostrarInformacion(
                                "Error de API (" + statusCode + ")",
                                "El servidor rechazó los datos.\nDetalle: " + (errorBody.length() > 100 ? errorBody.substring(0, 100) + "..." : errorBody)
                        );
                    });
                }
            } catch (java.net.ConnectException e) {
                System.out.println("[ERROR] No se pudo conectar al servidor para modificar. ¿Está encendido Spring Boot?");
                javafx.application.Platform.runLater(() -> {
                    AlertasUtils.mostrarInformacion("Error de Conexión", "No se pudo conectar con el servidor. Verifica el backend.");
                });
            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico en el hilo de modificación.");
                e.printStackTrace();
            }
        });

        putThread.setDaemon(true);
        putThread.start();
    }


    public void buscarProductos(ActionEvent actionEvent) {
    }
}