package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class controladorModifCliente {

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    public Button botonAgregar; // Este botón actúa como el confirmador de cambios

    @FXML public TextField nombre;
    @FXML public TextField cuil;
    @FXML public TextField email;
    @FXML public TextField telefono;
    @FXML public TextField pais;
    @FXML public TextField provincia;
    @FXML public TextField ciudad;
    @FXML public SplitMenuButton splitTipoDoc; // Se vincula con el FXML
    @FXML public SplitMenuButton splitIva;     // Se vincula con el FXML

    private clienteClase clienteSeleccionado;

    @FXML
    public void initialize() {
        // Inicialización nativa automática de JavaFX
    }

    /**
     * Une las dos lógicas: Guarda la referencia del cliente y rellena la interfaz
     * inmediatamente al abrir la pantalla de edición de forma segura.
     */
    public void rellenarCampos(clienteClase cliente) {
        if (cliente != null) {
            this.clienteSeleccionado = cliente; // Guardamos el cliente con su ID original

            // Rellenar campos de texto comunes
            nombre.setText(cliente.getNombreEntidad());
            cuil.setText(cliente.getCuitcuilEntidad());
            email.setText(cliente.getEmailEntidad());
            telefono.setText(cliente.getTelefonoEntidad());

            // Rellenar campos geográficos nuevos
            pais.setText(cliente.getPais() != null ? cliente.getPais() : "");
            provincia.setText(cliente.getProvincia() != null ? cliente.getProvincia() : "");
            ciudad.setText(cliente.getCiudad() != null ? cliente.getCiudad() : "");

            // Rellenar selectores visuales con los valores guardados previamente
            if (cliente.getTipoIdentificacion() != null && !cliente.getTipoIdentificacion().isEmpty() && splitTipoDoc != null) {
                splitTipoDoc.setText(cliente.getTipoIdentificacion());
            }
            if (cliente.getCondicionIva() != null && !cliente.getCondicionIva().isEmpty()&& splitIva != null){
                splitIva.setText(cliente.getCondicionIva());
            }
        }
    }

    @FXML
    void cambiarTipoDoc(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        splitTipoDoc.setText(item.getText());
    }

    @FXML
    void cambiarIva(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        splitIva.setText(item.getText());
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

        String txtTipoDoc = splitTipoDoc.getText();
        String txtIva = splitIva.getText();

        // 1. Validaciones básicas
        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtTipoDoc.equals("--") || txtIva.equals("--")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Menús sin seleccionar.", "Por favor, elige el Tipo de Documento y la Condición de IVA.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Integer.parseInt(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Identificación", "Por favor, corrija el número sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        // 2. Cuadro de confirmación de cambios
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
                // Trabajamos sobre la referencia existente para conservar el ID de SQLite intacto
                clienteSeleccionado.setNombreEntidad(txtNombre);
                clienteSeleccionado.setTelefonoEntidad(txtTelefono);
                clienteSeleccionado.setEmailEntidad(txtEmail);
                clienteSeleccionado.setCuitcuilEntidad(txtCuil);

                if (txtTipoDoc.equalsIgnoreCase("D.N.I")) {
                    clienteSeleccionado.setDniEntidad(txtCuil);
                }

                // Inyectamos las 5 nuevas variables recopiladas
                clienteSeleccionado.setTipoIdentificacion(txtTipoDoc);
                clienteSeleccionado.setCondicionIva(txtIva);
                clienteSeleccionado.setPais(txtPais);
                clienteSeleccionado.setProvincia(txtProvincia);
                clienteSeleccionado.setCiudad(txtCiudad);

                // Mandamos el objeto modificado al método UPDATE del DAO
                ClienteDAO.actualizar(clienteSeleccionado);

                System.out.println("Cliente modificado con éxito.");
                limpiarCampos();

                // Cierre automático de ventana modal al terminar la actualización
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
        splitTipoDoc.setText("--");
        splitIva.setText("--");
    }
}
