package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class controladorModifCliente {

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    public Button botonAgregar;

    @FXML public TextField nombre;
    @FXML public TextField cuil;
    @FXML public TextField email;
    @FXML public TextField telefono;
    @FXML public TextField pais;
    @FXML public TextField provincia;
    @FXML public TextField ciudad;

    @FXML public ComboBox<String> comboTipoDoc; // ACTUALIZADO
    @FXML public ComboBox<String> comboIva;



    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/clientes";

    private clienteClase clienteSeleccionado;

    @FXML
    public void initialize() {
        if (comboIva != null) {
            comboIva.getItems().addAll(
                    "Consumidor Final", "Exento", "Exterior",
                    "IVA No Alcanzado", "Monotributista", "Responsable Inscripto"
            );
        }
        if (comboTipoDoc != null) {
            comboTipoDoc.getItems().addAll("D.N.I", "C.U.I.T", "Pasaporte", "Otros"); // ACTUALIZADO
        }
    }

    public void rellenarCampos(clienteClase cliente) {
        if (cliente != null) {
            this.clienteSeleccionado = cliente;

            nombre.setText(cliente.getNombreEntidad());
            cuil.setText(cliente.getCuitcuilEntidad());
            email.setText(cliente.getEmailEntidad());
            telefono.setText(cliente.getTelefonoEntidad());
            pais.setText(cliente.getPais() != null ? cliente.getPais() : "");
            provincia.setText(cliente.getProvincia() != null ? cliente.getProvincia() : "");
            ciudad.setText(cliente.getCiudad() != null ? cliente.getCiudad() : "");

            // Asignación limpia de ComboBoxes nativos
            if (cliente.getTipoIdentificacion() != null && !cliente.getTipoIdentificacion().isEmpty() && comboTipoDoc != null) {
                comboTipoDoc.setValue(cliente.getTipoIdentificacion());
            }
            if (cliente.getCondicionIva() != null && !cliente.getCondicionIva().isEmpty() && comboIva != null){
                comboIva.setValue(cliente.getCondicionIva());
            }
        }
    }

    @FXML
    void botonAgregar(ActionEvent event) throws IOException {
        String txtNombre = nombre.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtCuil = cuil.getText();
        String txtPais = pais.getText();
        String txtProvincia = provincia.getText();
        String txtCiudad = ciudad.getText();

        // Extracción segura de valores seleccionados
        String txtTipoDoc = (comboTipoDoc != null && comboTipoDoc.getValue() != null) ? comboTipoDoc.getValue() : "";
        String txtIva = (comboIva != null && comboIva.getValue() != null) ? comboIva.getValue() : "";

        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtTipoDoc.isEmpty() || txtIva.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Menús sin seleccionar.", "Por favor, elige el Tipo de Documento y la Condición de IVA.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Long.parseLong(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Identificación", "Por favor, corrija el número sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        String mensaje = String.format(
                "¿Confirmas la modificación de datos del cliente?\n\nNombre: %s\n%s: %s\nTeléfono: %s\nEmail: %s",
                txtNombre, txtTipoDoc, txtCuil, txtTelefono, txtEmail
        );

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Modificación");
        alerta.setHeaderText("Revisa los datos antes de actualizar");
        alerta.setContentText(mensaje);

        ButtonType botonConfirmar = new ButtonType("Confirmar Actualización");
        ButtonType botonModificar = new ButtonType("Cancelar");
        alerta.getButtonTypes().setAll(botonConfirmar, botonModificar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonConfirmar) {
            try {
                clienteSeleccionado.setNombreEntidad(txtNombre);
                clienteSeleccionado.setTelefonoEntidad(txtTelefono);
                clienteSeleccionado.setEmailEntidad(txtEmail);
                clienteSeleccionado.setCuitcuilEntidad(txtCuil);

                if (txtTipoDoc.equalsIgnoreCase("D.N.I")) {
                    clienteSeleccionado.setDniEntidad(txtCuil);
                }

                clienteSeleccionado.setTipoIdentificacion(txtTipoDoc);
                clienteSeleccionado.setCondicionIva(txtIva);
                clienteSeleccionado.setPais(txtPais);
                clienteSeleccionado.setProvincia(txtProvincia);
                clienteSeleccionado.setCiudad(txtCiudad);

              //  ClienteDAO.actualizar(clienteSeleccionado);
                modificarClienteApi(clienteSeleccionado);

                System.out.println("Cliente modificado con éxito.");
                limpiarCampos();

                Stage stage = (Stage) botonAgregar.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                System.out.println("Error al intentar procesar la actualización del cliente.");
                e.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        nombre.clear();
        telefono.clear();
        email.clear();
        cuil.clear();
        pais.clear();
        provincia.clear();
        ciudad.clear();
        if (comboTipoDoc != null) comboTipoDoc.setValue(null);
        if (comboIva != null) comboIva.setValue(null);
    }
    private void modificarClienteApi(clienteClase clienteModificado) {
        if (clienteModificado == null) return;

        Thread putThread = new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 1. Convertir el objeto Java actualizado a un texto JSON String
                String jsonRequestBody = objectMapper.writeValueAsString(clienteModificado);

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
                    clienteClase clienteActualizadoApi = objectMapper.readValue(jsonRespuesta, clienteClase[].class != null ? clienteClase.class : clienteClase.class);

                    System.out.println("[INFO] Cliente modificado en la API con éxito.");

                    // 5. Refrescar la interfaz visual de JavaFX de forma segura
                    javafx.application.Platform.runLater(() -> {
                        // Buscamos el índice actual en la lista y lo reemplazamos por el actualizado de la API
                        int index = listaClientesObs.indexOf(clienteModificado);
                        if (index >= 0) {
                            listaClientesObs.set(index, clienteActualizadoApi);
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
}
