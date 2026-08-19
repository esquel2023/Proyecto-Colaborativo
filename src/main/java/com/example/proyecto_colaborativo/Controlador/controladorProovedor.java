package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
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
import java.util.Optional;

import static com.example.proyecto_colaborativo.Utilits.NavegacionUtils.abrirPantalla;

public class controladorProovedor {


    private final ObservableList<proovedorClase> listaProveedoresObs = FXCollections.observableArrayList();
    private final ObservableList<proovedorClase> listaProductosProveedorObs = FXCollections.observableArrayList();

    public TextField nombre;
    public TextField cuit;
    public TextField email;
    public TextField telefono;
    public TextField pais;
    public TextField provincia;
    public TextField localidad;

    public TableView<proovedorClase> tablaProovedores;
    public TableColumn<proovedorClase, String> nombreTabla;
    public TableColumn<proovedorClase, String> telefonoTabla;
    public TableColumn<proovedorClase, String> colEmail;

    public ComboBox<String> condicionIVA;

    public TextField buscadorProovedores;
    public Button botonAgregar;
    public Button botonProducto;

    proovedorClase proveedorSelec;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/proveedores";

    public void initialize() {
        condicionIVA.getItems().addAll(
                "Responsable Inscripto",
                "Monotributista"

        );
        if (tablaProovedores != null) {
            tablaProovedores.setPlaceholder(new Label("No hay proveedores cargados"));

            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));


            obtenerProveedoresApi();

            tablaProovedores.setItems(listaProveedoresObs);


            tablaProovedores.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
                if (newValue == null) {
                    listaProductosProveedorObs.clear();
                    limpiarCampos();
                } else {
                    nombre.setText(newValue.getNombreEntidad());
                    telefono.setText(newValue.getTelefonoEntidad());
                    email.setText(newValue.getEmailEntidad());
                    cuit.setText(newValue.getCuitcuilEntidad());
                    pais.setText(newValue.getPais());
                    provincia.setText(newValue.getProvincia());
                    localidad.setText(newValue.getCiudad());
                    condicionIVA.setValue(newValue.getCondicionIva());
                    cargarProductosDelProveedor(newValue);
                    this.proveedorSelec = newValue;
                    botonProducto.getStyleClass().remove("btn-warning");
                    botonProducto.getStyleClass().add("btn-danger");
                }
            });
        }
    }

    @FXML
    public void cambiarIva(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String nuevoIva = item.getText();
        condicionIVA.setValue(nuevoIva);

    }

    private void cargarProductosDelProveedor(Object proveedor) {
        // Lógica para filtrar o cargar productos del proveedor seleccionado
    }


    private void limpiarCampos() {
        nombre.clear();
        telefono.clear();
        email.clear();
        cuit.clear();
        pais.clear();
        provincia.clear();
        localidad.clear();
    }

    public void buscarProveedor(ActionEvent actionEvent) {

        BuscadorUtils.configuradorBuscador(
                buscadorProovedores,
                tablaProovedores,
                listaProveedoresObs,
                (proveedor, texto) -> {
                    // Validación segura contra valores nulos
                    boolean coincideNombre = proveedor.getNombreEntidad() != null &&
                            proveedor.getNombreEntidad().toLowerCase().contains(texto);

                    return coincideNombre;

                });
    }

    public void botonEliminar(ActionEvent actionEvent) {
        proovedorClase proveedorSeleccionado = tablaProovedores.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            eliminarProveedorApi(proveedorSeleccionado);
            listaProveedoresObs.remove(proveedorSeleccionado);
            limpiarCampos();

        }
    }

    public void botonAgrega(ActionEvent actionEvent) {
        String txtNombre = nombre.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtCuil = cuit.getText();
        String txtPais = pais.getText();
        String txtProvincia = provincia.getText();
        String txtCiudad = localidad.getText();
        String txtIva = condicionIVA.getValue();

        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }
        if (txtIva == null || txtIva.isEmpty() || txtIva.equals("--")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Menú sin seleccionar.", "Por favor, elige la Condición de IVA.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de CUIT, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Long.parseLong(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "CUIT / CUIL", "Por favor, corrija el número de identificación sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }
        String mensaje = String.format(
                "¿Confirmas los datos del proveedor?\n\nNombre: %s\nCUIT: %s\nTeléfono: %s\nEmail: %s\nIVA: %s\nUbicación: %s, %s",
                txtNombre, txtCuil, txtTelefono, txtEmail, txtIva, txtProvincia, txtPais
        );

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Proveedor");
        alerta.setHeaderText("Revisa los datos antes de guardar");
        alerta.setContentText(mensaje);

        ButtonType botonConfirmar = new ButtonType("Confirmar");
        ButtonType botonModificar = new ButtonType("Modificar / Cancelar");
        alerta.getButtonTypes().setAll(botonConfirmar, botonModificar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonConfirmar) {
            try {
                proovedorClase nuevoProveedor = new proovedorClase();

                nuevoProveedor.setNombreEntidad(txtNombre);
                nuevoProveedor.setTelefonoEntidad(txtTelefono);
                nuevoProveedor.setEmailEntidad(txtEmail);
                nuevoProveedor.setCuitcuilEntidad(txtCuil);
                nuevoProveedor.setCondicionIva(txtIva);
                nuevoProveedor.setPais(txtPais);
                nuevoProveedor.setProvincia(txtProvincia);
                nuevoProveedor.setCiudad(txtCiudad);

                agregarProveeodorApi(nuevoProveedor);

                // Limpieza de interfaz y actualización
                limpiarCampos();
                System.out.println("Proveedor agregado con éxito.");

            } catch (Exception e) {
                System.out.println("Error al intentar procesar e insertar el proveedor.");
                e.printStackTrace();
            }
        }
    }

    public void botonModifica(ActionEvent actionEvent) {
        proovedorClase proveedorSeleccionado = tablaProovedores.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado == null) {
            System.out.println("Error: Debes seleccionar un proveedor de la tabla.");
            return;
        }

        String nuevonombre = nombre.getText();
        String nuevotelefono = telefono.getText();
        String nuevoemail = email.getText();
        String nuevocuil = cuit.getText();
        String nuevopais = pais.getText();
        String nuevaprov = provincia.getText();
        String nuevaciudad = localidad.getText();

        // 4. Validar que no dejen ningún campo vacío
        if (nuevonombre.isEmpty() || nuevocuil.isEmpty() ||
                nuevoemail.isEmpty() || nuevotelefono.isEmpty() || nuevaciudad.isEmpty() || nuevopais.isEmpty() || nuevaprov.isEmpty()) {
            System.out.println("Error: No puedes dejar campos vacíos.");
            return;
        }

        proveedorSeleccionado.setNombreEntidad(nuevonombre);
        proveedorSeleccionado.setTelefonoEntidad((nuevotelefono));
        proveedorSeleccionado.setEmailEntidad(nuevoemail);
        proveedorSeleccionado.setCuitcuilEntidad(nuevocuil);
        proveedorSeleccionado.setCiudad(nuevaciudad);
        proveedorSeleccionado.setPais(nuevopais);
        proveedorSeleccionado.setProvincia(nuevaprov);

        modificarProveedorApi(proveedorSeleccionado);

        tablaProovedores.refresh();

        // 7. Limpiar la selección de la tabla y los campos de texto
        tablaProovedores.getSelectionModel().clearSelection();
        limpiarCampos();
        System.out.println("¡Proveedor modificado con éxito!");
    }


    public void buscarProductos(ActionEvent actionEvent) {
        abrirPantalla("proveedorSeleccionado.fxml", "Proveedor Seleccionado", false);
        controladorProveedorSelec.setProveedorSelec(proveedorSelec);


    }

    /*

            API


     */
    private void obtenerProveedoresApi() {
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
                    proovedorClase[] proveedorArray = objectMapper.readValue(json, proovedorClase[].class);

                    // Refrescamos de manera segura la interfaz de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedoresObs.clear();
                        listaProveedoresObs.addAll(proveedorArray);
                        System.out.println("[INFO] ¡Tabla JavaFX actualizada con " + proveedorArray.length + " clientes reales!");
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


    private void eliminarProveedorApi(proovedorClase proveedorAeliminar) {
        if (proveedorAeliminar == null) return;

        Thread deleteThread = new Thread(() -> {
            try {


                Long idProveedor = proveedorAeliminar.getId();

                String urlEliminar = API_URL + "/" + idProveedor;

                System.out.println("[DEBUG] Intentando eliminar en: " + urlEliminar);


                // 2. Construir la petición DELETE
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlEliminar))
                        .DELETE()
                        .build();

                // 3. Enviar la solicitud
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 4. Tu API de Spring Boot responde con 24 (No Content) al eliminar con éxito
                if (response.statusCode() == 204 || response.statusCode() == 200) {
                    System.out.println("[INFO] Proveedor eliminado de la API con éxito.");

                    // 5. Remover el cliente visualmente de la tabla en el hilo de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedoresObs.remove(proveedorAeliminar);
                        tablaProovedores.getSelectionModel().clearSelection();
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

    private void modificarProveedorApi(proovedorClase proveedorModificado) {
        if (proveedorModificado == null) return;

        Thread putThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 1. Convertir el objeto Java actualizado a un texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(proveedorModificado);

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
                    proovedorClase proveedorActualizadoApi = objectMapper.readValue(jsonRespuesta, proovedorClase[].class != null ? proovedorClase.class : proovedorClase.class);

                    System.out.println("[INFO] Cliente modificado en la API con éxito.");

                    // 5. Refrescar la interfaz visual de JavaFX de forma segura
                    javafx.application.Platform.runLater(() -> {
                        // Buscamos el índice actual en la lista y lo reemplazamos por el actualizado de la API
                        int index = listaProveedoresObs.indexOf(proveedorModificado);
                        if (index >= 0) {
                            listaProveedoresObs.set(index, proveedorActualizadoApi);
                        }
                        // Fuerza el redibujado de las celdas
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
    private void agregarProveeodorApi(proovedorClase nuevoProveedor) {
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
                    proovedorClase proveedorCreadoApi = objectMapper.readValue(jsonRespuesta, proovedorClase.class);

                    System.out.println("[INFO] Cliente agregado a la API con éxito.");

                    // 6. Impactar el cambio de forma segura en la interfaz visual de JavaFX
                    javafx.application.Platform.runLater(() -> {
                        listaProveedoresObs.add(proveedorCreadoApi); // Se dibuja solo en la TableView
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
}



