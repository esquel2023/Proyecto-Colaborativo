package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class controladorCliente {

    @FXML public TableView<claseFactura> tablaClientes1;
    @FXML public TableColumn<claseFactura, String> nombreTabla1;
    @FXML public TableColumn<claseFactura, String> dniTabla1;
    public Label totalclientes;

    @FXML private TextField cuil;
    @FXML private TextField buscadorClientes;
    @FXML private TextField telefono;
    @FXML private TextField dni;
    @FXML private TextField nombreApellido;
    @FXML private TextField direccion;
    @FXML private TextField email;

    @FXML public TableView<clienteClase> tablaClientes;
    @FXML private TableColumn<clienteClase, String> nombreTabla;
    @FXML private TableColumn<clienteClase, String> dniTabla;
    @FXML private TableColumn<clienteClase, String> telefonoTabla;

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuración de tabla Clientes
        nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
        dniTabla.setCellValueFactory(new PropertyValueFactory<>("dniEntidad"));
        telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

        listaClientesObs.setAll(ClienteDAO.listar());
        tablaClientes.setItems(listaClientesObs);

        // Configuración de tabla Facturas
        nombreTabla1.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        dniTabla1.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        tablaClientes1.setItems(listaFacturasObs);

        // Listener de selección
        // UN SOLO LISTENER OPTIMIZADO
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((_, _, clienteSeleccionado) -> {
            if (clienteSeleccionado == null) {
                // Si no hay nada seleccionado, limpiamos todo de un solo viaje
                listaFacturasObs.clear();
                limpiarCampos(); // Método para vaciar los TextField
            } else {
                // Si seleccionó un cliente, rellenamos los campos
                nombreApellido.setText(clienteSeleccionado.getNombreEntidad());
                dni.setText(clienteSeleccionado.getDniEntidad());
                telefono.setText(clienteSeleccionado.getTelefonoEntidad());
                email.setText(clienteSeleccionado.getEmailEntidad());
                direccion.setText(clienteSeleccionado.getDireccionEntidad());
                cuil.setText(clienteSeleccionado.getCuitcuilEntidad());

                // Aquí puedes cargar las facturas de ese cliente de forma eficiente si lo necesitas
            }
        });

        totalclientes.setText("Cantidad total de clientes: " );

    }

    @FXML
    void botonAgregar(ActionEvent event) throws IOException {
        String txtNombre = nombreApellido.getText();
        String txtDni = dni.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtCuil = cuil.getText();

        if (txtNombre.isEmpty() || txtDni.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtDni.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Integer.parseInt(txtDni);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Dni", "Por favor, corrija el DNI sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }

        String mensaje = String.format(
                "¿Confirmas los datos del cliente?\n\nNombre: %s\nDNI: %s\nTeléfono: %s\nEmail: %s\nCUIL: %s",
                txtNombre, txtDni, txtTelefono, txtEmail, txtCuil
        );

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Cliente");
        alerta.setHeaderText("Revisa los datos antes de guardar");
        alerta.setContentText(mensaje);

        ButtonType botonConfirmar = new ButtonType("Confirmar");
        ButtonType botonModificar = new ButtonType("Modificar / Cancelar");
        alerta.getButtonTypes().setAll(botonConfirmar, botonModificar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        try {
            clienteClase nuevoCliente = new clienteClase();

            // Le cargamos los textos de la pantalla
            nuevoCliente.setNombreEntidad(txtNombre);
            nuevoCliente.setDniEntidad(txtDni);
            nuevoCliente.setTelefonoEntidad((txtTelefono));
            nuevoCliente.setEmailEntidad(txtEmail);
            nuevoCliente.setCuitcuilEntidad(txtCuil);
            System.out.println("si");

            // Lo mandamos al DAO. SQLite ignorará el ID 0 e insertará uno nuevo automático (ej: 1, 2, 3...)
            ClienteDAO.insertar(nuevoCliente);
            System.out.println("si si?");

            listaClientesObs.setAll(ClienteDAO.listar()); // Recarga la tabla leyendo los IDs nuevos
            limpiarCampos();
            System.out.println("Cliente agregado con éxito.");
        } catch (Exception e) {
            System.out.println("asd");
        }
        totalclientes.setText("Cantidad total de clientes: ");

    }

    @FXML
    void botonModificar(ActionEvent event) {
        //
        String txtNombre = nombreApellido.getText();
        String txtDni = dni.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtCuil = cuil.getText();



        if (txtNombre.isEmpty() || txtDni.isEmpty() || txtCuil.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "No completaste todos los campos.", "Hay campos vacíos.", Alert.AlertType.INFORMATION);
            return;
        }

        if (txtDni.contains("-") || !txtEmail.contains("@") || txtNombre.contains("-")) {
            AlertasUtils.mostrarAlerta("FALTAN DATOS", "Formatos inválidos.", "Por favor revisa los formatos de DNI, Email o Nombre.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            Integer.parseInt(txtDni);
        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Datos inválidos", "Dni", "Por favor, corrija el DNI sin puntos ni letras.", Alert.AlertType.INFORMATION);
            return;
        }


        try {

            clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            clienteSeleccionado.setNombreEntidad(nombreApellido.getText());
            clienteSeleccionado.setDniEntidad(dni.getText());
            clienteSeleccionado.setTelefonoEntidad((telefono.getText()));
            clienteSeleccionado.setEmailEntidad(email.getText());
            clienteSeleccionado.setDireccionEntidad(direccion.getText());
            clienteSeleccionado.setCuitcuilEntidad(cuil.getText());
            // 2. Guarda los cambios de forma permanente en la Base de Datos
            ClienteDAO.actualizar(clienteSeleccionado);

            // 3. Refresca la interfaz visual
            tablaClientes.refresh();
            limpiarCampos();
            System.out.println("Cliente modificado con éxito en la BD.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void botonElimina(ActionEvent event) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {
                // Se envía el nombre como cadena de texto directo a la BD
                ClienteDAO.eliminar(clienteSeleccionado.getNombreEntidad());

                // Se remueve de la interfaz visual
                listaClientesObs.remove(clienteSeleccionado);
                limpiarCampos();
                System.out.println("Cliente eliminado con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        totalclientes.setText("Cantidad total de clientes: ");

    }

    @FXML
    void botonLupa(ActionEvent event) {
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

    private void limpiarCampos() {
        nombreApellido.clear();
        dni.clear();
        telefono.clear();
        email.clear();
        direccion.clear();
        cuil.clear();
    }

}
