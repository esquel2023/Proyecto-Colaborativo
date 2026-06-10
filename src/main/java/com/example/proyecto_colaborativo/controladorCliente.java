package com.example.proyecto_colaborativo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.util.Optional;

public class controladorCliente {
    @FXML public TableView<claseFactura> tablaClientes1;
    @FXML public TableColumn<claseFactura, String> nombreTabla1; // Esta será para la Fecha
    @FXML public TableColumn<claseFactura, String> dniTabla1;
    entidadClase Cliente = new entidadClase();
    BuscadorUtils buscadorUtils;

    @FXML private Button lupa;
    @FXML private Button botonAgregar;
    @FXML private Button botonModificar;
    @FXML private Button botonEliminar;

    @FXML private TextField cuil;
    @FXML private TextField buscadorClientes;
    @FXML private TextField telefono;
    @FXML private TextField dni;
    @FXML private TextField nombreApellido;
    @FXML private TextField direccion;
    @FXML private TextField email;

    public TableView<clienteClase> tablaClientes;
    @FXML private TableColumn<clienteClase, String> nombreTabla;
    @FXML private TableColumn<clienteClase, String> dniTabla;
    @FXML private TableColumn<clienteClase, String> telefonoTabla;

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();
    @FXML
    public void initialize() {

        nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
        dniTabla.setCellValueFactory(new PropertyValueFactory<>("dniEntidad"));
        telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));


        tablaClientes.setItems(listaClientesObs);
        nombreTabla1.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        dniTabla1.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));

        // 2. Asignar la lista observable a la segunda tabla
        tablaClientes1.setItems(listaFacturasObs);

        // 3. El Listener de selección para la tabla de clientes original
        // Cuando toques un cliente, se ejecutará este bloque de código automáticamente
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Convertimos el Object seleccionado al tipo clienteClase
                clienteClase clienteSeleccionado = (clienteClase) newSelection;


            } else {
                // Si deseleccionan al cliente, limpiamos la tabla de facturas
                listaFacturasObs.clear();
            }
        });
        BuscadorUtils.configuradorBuscador(
                buscadorClientes,   // 1. El TextField donde el usuario escribe
                tablaClientes,      // 2. La TableView de clientes
                listaClientesObs,   // 3. La lista ObservableList original
                (cliente, texto) -> { // 4. El criterio de búsqueda (BiPredicate)

                    // Convertimos el texto a minúsculas para que no importen las mayúsculas
                    String textoMinuscula = texto.toLowerCase();

                    // Filtramos por Nombre
                    boolean coincideNombre = cliente.getNombreEntidad() != null
                            && cliente.getNombreEntidad().toLowerCase().contains(textoMinuscula);

                    // Filtramos por DNI
                    boolean coincideDni = cliente.getDniEntidad() != null
                            && cliente.getDniEntidad().contains(textoMinuscula);

                    // Si coincide el nombre O el DNI, el cliente se queda en la tabla
                    return coincideNombre || coincideDni;
                }
        );

    }

    @FXML
    void botonAgregar(ActionEvent event) throws IOException {

        String txtNombre = nombreApellido.getText();
        String txtDni = dni.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtDireccion = direccion.getText();
        String txtCuil = cuil.getText();

        if (txtNombre.isEmpty() || txtDni.isEmpty() || txtCuil.isEmpty() ||
                txtDireccion.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacios, por favor, agrega toda la informacion requerida y vuelve a intentarlo.", Alert.AlertType.INFORMATION);
            return;
        }
        if (txtDni.contains("-") ||
                 !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacios, por favor, agrega toda la informacion requerida y vuelve a intentarlo.", Alert.AlertType.INFORMATION);
            return;
        }
        String texto = dni.getText();

        try {

            int numero = Integer.parseInt(texto);
            System.out.println("Número válido: " + numero);

        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos invalidos", "Dni", "Por favor, corriga el DNI sin puntos ni guiones ni letras ni caracteres... y vuelva a intentarlo.", Alert.AlertType.INFORMATION);

            return;
        }

        String mensaje = String.format(
                "¿Confirmas los datos del cliente?\n\n" +
                        "Nombre: %s\n" +
                        "DNI: %s\n" +
                        "Teléfono: %s\n" +
                        "Email: %s\n" +
                        "Dirección: %s\n" +
                        "CUIL: %s",
                txtNombre, txtDni, txtTelefono, txtEmail, txtDireccion, txtCuil
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


            clienteClase nuevoCliente = new clienteClase(txtNombre, txtDni, txtTelefono, txtEmail, txtDireccion, txtCuil);
            listaClientesObs.add(nuevoCliente);

            limpiarCampos();
            System.out.println("Cliente agregado con éxito.");

        } else {

            System.out.println("El usuario decidió corregir los datos.");
        }
    }

    @FXML
    void botonModificar(ActionEvent event) {
        clienteClase clienteSeleccionado = (clienteClase) tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Actualiza los datos del objeto con lo que haya en los TextField
            clienteSeleccionado.setNombreEntidad(nombreApellido.getText());
            clienteSeleccionado.setDniEntidad(dni.getText());
            clienteSeleccionado.setTelefonoEntidad(telefono.getText());
            clienteSeleccionado.setEmailEntidad(email.getText());
            clienteSeleccionado.setDireccionEntidad(direccion.getText());
            clienteSeleccionado.setCuitcuilEntidad(cuil.getText());

            tablaClientes.refresh();
            limpiarCampos();
        }
    }

    @FXML
    void botonElimina(ActionEvent event) {
        Object clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            listaClientesObs.remove(clienteSeleccionado);
            limpiarCampos();
        }
    }

    @FXML
    void botonLupa(ActionEvent event) {
        String buscar = buscadorClientes.getText().toLowerCase();

    }

    private void limpiarCampos() {
        nombreApellido.clear();
        dni.clear();
        telefono.clear();
        email.clear();
        direccion.clear();
        cuil.clear();
    }

    }



