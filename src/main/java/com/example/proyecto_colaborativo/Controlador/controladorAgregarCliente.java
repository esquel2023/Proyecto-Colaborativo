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

public class controladorAgregarCliente {

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    public Button botonAgregar;

    @FXML public TextField nombre;
    @FXML public TextField cuil;
    @FXML public TextField email;
    @FXML public TextField telefono;
    @FXML public TextField pais;
    @FXML public TextField provincia;
    @FXML public TextField ciudad;
    @FXML public SplitMenuButton splitTipoDoc;
    @FXML public SplitMenuButton splitIva;

    @FXML
    public void initialize() {
        // Inicialización libre para configuraciones al arrancar la pantalla
    }

    @FXML
    void cambiarTipoDoc(ActionEvent event) {
        // Obtiene el MenuItem que presionó el usuario y actualiza el texto del SplitMenuButton
        MenuItem item = (MenuItem) event.getSource();
        splitTipoDoc.setText(item.getText());
    }

    @FXML
    void cambiarIva(ActionEvent event) {
        // Obtiene la opción de IVA elegida y actualiza el texto del SplitMenuButton
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

        // 1. Validaciones de campos de texto vacíos obligatorios
        if (txtNombre.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        // 2. Validaciones de los SplitMenuButton (No deben quedarse con el valor por defecto "--")
        if (txtTipoDoc.equals("--") || txtIva.equals("--")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Menús sin seleccionar.", "Por favor, elige el Tipo de Documento y la Condición de IVA.", Alert.AlertType.INFORMATION);
            return;
        }

        // 3. Validaciones de formatos básicos
        if (txtCuil.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Integer.parseInt(txtCuil);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Dni / CUIT", "Por favor, corrija el número de identificación sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        // 4. Mensaje de confirmación en la Alerta visual
        String mensaje = String.format(
                "¿Confirmas los datos del cliente?\n\nNombre: %s\n%s: %s\nTeléfono: %s\nEmail: %s\nIVA: %s\nUbicación: %s, %s",
                txtNombre, txtTipoDoc, txtCuil, txtTelefono, txtEmail, txtIva, txtProvincia, txtPais
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

                // Carga de campos comunes heredados
                nuevoCliente.setNombreEntidad(txtNombre);
                nuevoCliente.setTelefonoEntidad(txtTelefono);
                nuevoCliente.setEmailEntidad(txtEmail);
                nuevoCliente.setCuitcuilEntidad(txtCuil);

                // Mapeo condicional: Si seleccionó DNI, se inyecta también en la columna DNI del objeto
                if (txtTipoDoc.equalsIgnoreCase("D.N.I")) {
                    nuevoCliente.setDniEntidad(txtCuil);
                }

                // Carga de los 5 nuevos campos del formulario
                nuevoCliente.setTipoIdentificacion(txtTipoDoc);
                nuevoCliente.setCondicionIva(txtIva);
                nuevoCliente.setPais(txtPais);
                nuevoCliente.setProvincia(txtProvincia);
                nuevoCliente.setCiudad(txtCiudad);

                // Persistencia en SQLite a través del DAO
                ClienteDAO.insertar(nuevoCliente);

                // Limpieza de interfaz y cierre de ventana automático
                limpiarCampos();
                System.out.println("Cliente agregado con éxito.");

                // Cierra la ventana actual tras guardar con éxito
                Stage stage = (Stage) botonAgregar.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                System.out.println("Error al intentar procesar e insertar el cliente.");
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
