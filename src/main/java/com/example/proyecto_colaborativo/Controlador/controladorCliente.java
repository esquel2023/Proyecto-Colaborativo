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
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


public class controladorCliente {

    //   public Label totalclientes;


    public Button modificarCliente;
    public Button eliminarCliente;
    public TextField buscadorClientes;


    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();
    public TextField nombreApellido;
    public TextField dni;
    public TextField cuil;
    public TextField telefono;
    public TextField email;
    public TextField direccion;

    // Cambia la definición de tus componentes FXML
    @FXML
    TableView<clienteClase> tablaClientes;
    @FXML private TableColumn<clienteClase, String> nombreTabla;
    @FXML private TableColumn<clienteClase, String> telefonoTabla;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/clientes";

    @FXML
    public void initialize() {
        configurarSeleccionTabla();
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

    private void configurarSeleccionTabla() {
        tablaClientes // Corregido typo de tablaProovedores a tablaProveedores
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        // Datos básicos
                        nombreApellido.setText(seleccionado.getNombreEntidad());
                        telefono.setText(seleccionado.getTelefonoEntidad());
                        email.setText(seleccionado.getEmailEntidad());
                        cuil.setText(seleccionado.getCuitcuilEntidad());
                        direccion.setText(seleccionado.getPais());
                        // Nuevos campos de ubicación (asumiendo estos getters en tu clase)

                    }
                });
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
    private void eliminarClienteApi(clienteClase clienteAEliminar) {
        if (clienteAEliminar == null) return;

        Thread deleteThread = new Thread(() -> {
            try {
                // 1. Armar la URL dinámica con el identificador del cliente (su nombre)
                // Se codifica por si el nombre tiene espacios (ej: "Juan Perez")

                String urlEliminar = "http://localhost:8080/tienda/api/v1/clientes/" + clienteAEliminar.getId();


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


    public void botonAgregar(ActionEvent actionEvent) {
        String txtNombre = nombreApellido.getText();
        String txtDni = dni.getText();
        String txtCuil = cuil.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtDireccion = direccion.getText();






        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Long.parseLong(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Dni / CUIT", "Por favor, corrija el número de identificación sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        String mensaje = String.format(
                "¿Confirmas los datos del cliente?\n\nNombre: %s\nCUIL: %s\nTeléfono: %s\nEmail: %s\nIVA: %s\nUbicación: %s",
                txtNombre, txtCuil, txtTelefono, txtEmail, txtDni, txtDireccion
        );

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Cliente");
        alerta.setHeaderText("Revisa los datos antes de guardar");
        alerta.setContentText(mensaje);

        ButtonType botonConfirmar = new ButtonType("Confirmar");
        ButtonType botonModificar = new ButtonType("Modificar / Cancelar");
        alerta.getButtonTypes().setAll(botonConfirmar, botonModificar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonConfirmar) {
            try {
                clienteClase nuevoCliente = new clienteClase();

                nuevoCliente.setNombreEntidad(txtNombre);
                nuevoCliente.setTelefonoEntidad(txtTelefono);
                nuevoCliente.setEmailEntidad(txtEmail);
                nuevoCliente.setCuitcuilEntidad(txtCuil);
                nuevoCliente.setDniEntidad(txtDni);
                nuevoCliente.setDireccion(txtDireccion);


                //  ClienteDAO.insertar(nuevoCliente);
                agregarClienteApi(nuevoCliente);

                limpiarCampos();
                System.out.println("Cliente agregado con éxito.");


            } catch (Exception e) {
                System.out.println("Error al intentar procesar e insertar el cliente.");
                e.printStackTrace();
            }
        }
    }

    public void botonModificar(ActionEvent actionEvent) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            AlertasUtils.mostrarAlerta("SELECCIÓN VACÍA", "No seleccionó ningún registro.", "Por favor, seleccione un cliente de la tabla.", Alert.AlertType.INFORMATION);
            return;
        }

        // 2. Extraer textos de los componentes gráficos
        String txtNombre = nombreApellido.getText().trim();
        String txtDni = dni.getText().trim();
        String txtCuil = cuil.getText().trim();
        String txtTelefono = telefono.getText().trim();
        String txtEmail = email.getText().trim();
        String txtDireccion = direccion.getText().trim();

        // 3. Mismas validaciones estrictas que el Botón Agregar
        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos obligatorios.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FORMATO ERRONEO", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre (CUIL sin guiones).", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Long.parseLong(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Dni / CUIT", "Por favor, corrija el número de identificación sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        // 4. Mensaje de confirmación adaptado para Modificación
        String mensaje = String.format(
                "¿Confirmas la actualización de los datos del cliente?\n\nNombre: %s\nDNI: %s\nCUIL: %s\nTeléfono: %s\nEmail: %s\nDirección: %s",
                txtNombre, txtDni, txtCuil, txtTelefono, txtEmail, txtDireccion
        );

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Actualización");
        alerta.setHeaderText("Revisa los cambios antes de impactar en la Base de Datos");
        alerta.setContentText(mensaje);

        ButtonType botonConfirmar = new ButtonType("Confirmar Cambios");
        ButtonType botonCancelar = new ButtonType("Cancelar");
        alerta.getButtonTypes().setAll(botonConfirmar, botonCancelar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonConfirmar) {
            // 5. Instanciar el objeto modificado manteniendo el ID único
            clienteClase clienteAEditar = new clienteClase();
            clienteAEditar.setId(clienteSeleccionado.getId()); // ID obligatorio para el @PathVariable del PUT
            clienteAEditar.setNombreEntidad(txtNombre);
            clienteAEditar.setTelefonoEntidad(txtTelefono);
            clienteAEditar.setEmailEntidad(txtEmail);
            clienteAEditar.setCuitcuilEntidad(txtCuil);
            clienteAEditar.setDniEntidad(txtDni);
            clienteAEditar.setDireccion(txtDireccion);

            // Mapeos adicionales por defecto de la entidad Cliente de tu backend
            clienteAEditar.setTipoIdentificacion(clienteSeleccionado.getTipoIdentificacion() != null ? clienteSeleccionado.getTipoIdentificacion() : "CUIT");
            clienteAEditar.setCondicionIva(clienteSeleccionado.getCondicionIva() != null ? clienteSeleccionado.getCondicionIva() : "Consumidor Final");
            clienteAEditar.setPais(clienteSeleccionado.getPais());
            clienteAEditar.setProvincia(clienteSeleccionado.getProvincia());
            clienteAEditar.setCiudad(clienteSeleccionado.getCiudad());

            // 6. Delegar la petición al hilo asíncrono de la API
            modificarClienteApi(clienteAEditar);
        }

    }

    public void botonElimina(ActionEvent actionEvent) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {

                eliminarClienteApi(clienteSeleccionado);

                // Se remueve de la interfaz visual
                listaClientesObs.remove(clienteSeleccionado);
                System.out.println("Cliente eliminado con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void botonLupa(ActionEvent actionEvent) {
    }

    public void abrirHistorialCliente(ActionEvent actionEvent) {
    }


    private void agregarClienteApi(clienteClase nuevoCliente) {
        if (nuevoCliente == null) return;

        // 1. Crear el hilo para no congelar la pantalla
        Thread postThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 2. Convertir tu objeto Java a texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(nuevoCliente);

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
                    clienteClase clienteCreadoApi = objectMapper.readValue(jsonRespuesta, clienteClase.class);

                    System.out.println("[INFO] Cliente agregado a la API con éxito.");

                    // 6. Impactar el cambio de forma segura en la interfaz visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaClientesObs.add(clienteCreadoApi); // Se dibuja solo en la TableView
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
    private void limpiarCampos() {
        nombreApellido.clear();
        telefono.clear();
        email.clear();
        cuil.clear();
        dni.clear();

    }
    private void modificarClienteApi(clienteClase clienteModificado) {
        if (clienteModificado == null) return;

        // 1. Crear el hilo secundario para evitar que la UI se congele
        Thread putThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 2. Serializar el objeto modificado a una cadena de texto JSON
                String jsonRequestBody = objectMapper.writeValueAsString(clienteModificado);

                // 3. Construir la URL dinámica inyectando el ID del cliente (Ej: .../api/v1/clientes/3)
                String urlConId = API_URL + "/" + clienteModificado.getId();

                // 4. Configurar la petición HTTP con el verbo PUT
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlConId))
                        .header("Content-Type", "application/json") // Avisamos a Spring Boot que procesamos JSON
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonRequestBody)) // Agregamos el JSON al Body
                        .build();

                // 5. Enviar de manera síncrona dentro del hilo la solicitud
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 6. Validar si Spring Boot aceptó la actualización (Status 200 OK)
                if (response.statusCode() == 200) {
                    String jsonRespuesta = response.body();

                    // Deserializar la respuesta a un objeto real
                    clienteClase clienteActualizadoApi = objectMapper.readValue(jsonRespuesta, clienteClase.class);

                    // 7. Sincronizar de forma segura los cambios con el hilo visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        System.out.println("Cliente modificado en la API con éxito.");

                        // Buscamos el registro antiguo dentro de la lista observable y lo reemplazamos
                        for (int i = 0; i < listaClientesObs.size(); i++) {
                            if (listaClientesObs.get(i).getId() == clienteActualizadoApi.getId()) {
                                listaClientesObs.set(i, clienteActualizadoApi); // Refresca automáticamente la fila de la TableView
                                break;
                            }
                        }

                        // Limpieza total del estado gráfico
                        tablaClientes.refresh();
                        tablaClientes.getSelectionModel().clearSelection();
                        limpiarCampos();
                    });
                } else {
                    // Diagnóstico estricto de errores de backend (400, 404, 500, etc.)
                    String errorBody = response.body();
                    int statusCode = response.statusCode();

                    System.out.println("[ERROR] No se pudo modificar el cliente. Código API: " + statusCode);
                    System.out.println("[DETALLE API]: " + errorBody);

                    javafx.application.Platform.runLater(() -> {
                        AlertasUtils.mostrarAlerta(
                                "ERROR DE API (" + statusCode + ")",
                                "El servidor rechazó los cambios del cliente.",
                                "Detalle: " + (errorBody.length() > 80 ? errorBody.substring(0, 80) + "..." : errorBody),
                                Alert.AlertType.ERROR
                        );
                    });
                }
            } catch (java.net.ConnectException e) {
                System.out.println("[ERROR] El servidor no responde. Verifique que Spring Boot esté encendido.");
                javafx.application.Platform.runLater(() -> {
                    AlertasUtils.mostrarAlerta("ERROR DE CONEXIÓN", "Backend inaccesible.", "No se pudo conectar con el servidor central.", Alert.AlertType.ERROR);
                });
            } catch (Exception e) {
                System.out.println("[ERROR] Error imprevisto en el hilo de actualización PUT.");
                e.printStackTrace();
            }
        });

        putThread.setDaemon(true);
        putThread.start();
    }

}

