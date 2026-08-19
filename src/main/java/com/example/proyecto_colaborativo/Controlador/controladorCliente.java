package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
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

    /* =========================================================
       CAMPOS
       ========================================================= */

    @FXML
    private TextField cuil;

    @FXML
    private TextField buscadorClientes;

    @FXML
    private TextField telefono;

    @FXML
    private TextField dni;

    @FXML
    private TextField nombreApellido;

    @FXML
    private TextField direccion;

    @FXML
    private TextField email;


    /* =========================================================
       TABLA CLIENTES
       ========================================================= */

    @FXML
    public TableView<clienteClase> tablaClientes;

    @FXML
    private TableColumn<clienteClase, String> nombreTabla;

    @FXML
    private TableColumn<clienteClase, String> dniTabla;

    @FXML
    private TableColumn<clienteClase, String> telefonoTabla;


    /* =========================================================
       TOTAL CLIENTES
       ========================================================= */

    @FXML
    private Label totalclientes;


    /* =========================================================
       LISTA
       ========================================================= */

    private final ObservableList<clienteClase> listaClientesObs =
            FXCollections.observableArrayList();


    /* =========================================================
       INITIALIZE
       ========================================================= */

    @FXML
    public void initialize() {

        nombreTabla.setCellValueFactory(
                new PropertyValueFactory<>("nombreEntidad")
        );

        dniTabla.setCellValueFactory(
                new PropertyValueFactory<>("dniEntidad")
        );

        telefonoTabla.setCellValueFactory(
                new PropertyValueFactory<>("telefonoEntidad")
        );


        /* CARGAR CLIENTES */

        listaClientesObs.setAll(
                ClienteDAO.listar()
        );

        tablaClientes.setItems(
                listaClientesObs
        );


        tablaClientes.setPlaceholder(
                new Label("No hay clientes cargados")
        );


        /* SELECCION DE CLIENTE */

        tablaClientes
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, anterior, clienteSeleccionado) -> {

                            if (clienteSeleccionado == null) {

                                limpiarCampos();

                            } else {

                                nombreApellido.setText(
                                        clienteSeleccionado.getNombreEntidad()
                                );

                                dni.setText(
                                        clienteSeleccionado.getDniEntidad()
                                );

                                telefono.setText(
                                        clienteSeleccionado.getTelefonoEntidad()
                                );

                                email.setText(
                                        clienteSeleccionado.getEmailEntidad()
                                );

                                direccion.setText(
                                        clienteSeleccionado.getDireccionEntidad()
                                );

                                cuil.setText(
                                        clienteSeleccionado.getCuitcuilEntidad()
                                );
                            }
                        }
                );


        actualizarTotalClientes();
    }


    /* =========================================================
       AGREGAR
       ========================================================= */

    @FXML
    void botonAgregar(ActionEvent event) throws IOException {

        String txtNombre =
                nombreApellido.getText();

        String txtDni =
                dni.getText();

        String txtTelefono =
                telefono.getText();

        String txtEmail =
                email.getText();

        String txtCuil =
                cuil.getText();


        if (txtNombre.isEmpty()
                || txtDni.isEmpty()
                || txtCuil.isEmpty()
                || txtEmail.isEmpty()
                || txtTelefono.isEmpty()) {

            AlertasUtils.mostrarInformacion(
                    "FALTAN DATOS",
                    "No completaste todos los campos. Hay campos vacíos."
            );

            return;
        }


        if (txtDni.contains("-")
                || !txtEmail.contains("@")
                || txtNombre.contains("-")) {

            AlertasUtils.mostrarInformacion(
                    "DATOS INVÁLIDOS",
                    "Revisá los formatos de DNI, correo o nombre."
            );

            return;
        }


        try {

            Integer.parseInt(txtDni);

        } catch (NumberFormatException e) {

            AlertasUtils.mostrarInformacion(
                    "DATOS INVÁLIDOS",
                    "Corregí el DNI. Debe escribirse sin puntos ni letras."
            );

            return;
        }


        String mensaje =
                String.format(
                        "¿Confirmás los datos del cliente?\n\n"
                                + "Nombre: %s\n"
                                + "DNI: %s\n"
                                + "Teléfono: %s\n"
                                + "Email: %s\n"
                                + "CUIL: %s",
                        txtNombre,
                        txtDni,
                        txtTelefono,
                        txtEmail,
                        txtCuil
                );


        Alert alerta =
                new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle(
                "Confirmación de Cliente"
        );

        alerta.setHeaderText(
                "Revisá los datos antes de guardar"
        );

        alerta.setContentText(
                mensaje
        );


        ButtonType botonConfirmar =
                new ButtonType("Confirmar");

        ButtonType botonCancelar =
                new ButtonType("Modificar / Cancelar");


        alerta.getButtonTypes().setAll(
                botonConfirmar,
                botonCancelar
        );


        Optional<ButtonType> resultado =
                alerta.showAndWait();


        if (resultado.isPresent()
                && resultado.get() == botonConfirmar) {

            try {

                clienteClase nuevoCliente =
                        new clienteClase();


                nuevoCliente.setNombreEntidad(
                        txtNombre
                );

                nuevoCliente.setDniEntidad(
                        txtDni
                );

                nuevoCliente.setTelefonoEntidad(
                        txtTelefono
                );

                nuevoCliente.setEmailEntidad(
                        txtEmail
                );

                nuevoCliente.setDireccionEntidad(
                        direccion.getText()
                );

                nuevoCliente.setCuitcuilEntidad(
                        txtCuil
                );


                ClienteDAO.insertar(
                        nuevoCliente
                );


                listaClientesObs.setAll(
                        ClienteDAO.listar()
                );


                limpiarCampos();

                actualizarTotalClientes();


                System.out.println(
                        "Cliente agregado con éxito."
                );


            } catch (Exception e) {

                e.printStackTrace();

                AlertasUtils.mostrarInformacion(
                        "ERROR",
                        "No se pudo agregar el cliente."
                );
            }
        }
    }


    /* =========================================================
       MODIFICAR
       ========================================================= */

    @FXML
    void botonModificar(ActionEvent event) {

        clienteClase clienteSeleccionado =
                tablaClientes
                        .getSelectionModel()
                        .getSelectedItem();


        if (clienteSeleccionado == null) {

            AlertasUtils.mostrarInformacion(
                    "CLIENTE",
                    "Seleccioná un cliente de la tabla para modificar."
            );

            return;
        }


        String txtNombre =
                nombreApellido.getText();

        String txtDni =
                dni.getText();

        String txtTelefono =
                telefono.getText();

        String txtEmail =
                email.getText();

        String txtCuil =
                cuil.getText();


        if (txtNombre.isEmpty()
                || txtDni.isEmpty()
                || txtCuil.isEmpty()
                || txtEmail.isEmpty()
                || txtTelefono.isEmpty()) {

            AlertasUtils.mostrarInformacion(
                    "FALTAN DATOS",
                    "No completaste todos los campos."
            );

            return;
        }


        if (txtDni.contains("-")
                || !txtEmail.contains("@")
                || txtNombre.contains("-")) {

            AlertasUtils.mostrarInformacion(
                    "DATOS INVÁLIDOS",
                    "Revisá los formatos de DNI, correo o nombre."
            );

            return;
        }


        try {

            Integer.parseInt(txtDni);

        } catch (NumberFormatException e) {

            AlertasUtils.mostrarInformacion(
                    "DATOS INVÁLIDOS",
                    "Corregí el DNI. Debe escribirse sin puntos ni letras."
            );

            return;
        }


        try {

            clienteSeleccionado.setNombreEntidad(
                    txtNombre
            );

            clienteSeleccionado.setDniEntidad(
                    txtDni
            );

            clienteSeleccionado.setTelefonoEntidad(
                    txtTelefono
            );

            clienteSeleccionado.setEmailEntidad(
                    txtEmail
            );

            clienteSeleccionado.setDireccionEntidad(
                    direccion.getText()
            );

            clienteSeleccionado.setCuitcuilEntidad(
                    txtCuil
            );


            ClienteDAO.actualizar(
                    clienteSeleccionado
            );


            tablaClientes.refresh();

            tablaClientes
                    .getSelectionModel()
                    .clearSelection();


            limpiarCampos();


            System.out.println(
                    "Cliente modificado con éxito."
            );


        } catch (SQLException e) {

            e.printStackTrace();

            AlertasUtils.mostrarInformacion(
                    "ERROR",
                    "No se pudo modificar el cliente."
            );
        }
    }


    /* =========================================================
       ELIMINAR
       ========================================================= */

    @FXML
    void botonElimina(ActionEvent event) {

        clienteClase clienteSeleccionado =
                tablaClientes
                        .getSelectionModel()
                        .getSelectedItem();


        if (clienteSeleccionado == null) {

            AlertasUtils.mostrarInformacion(
                    "CLIENTE",
                    "Seleccioná un cliente para eliminar."
            );

            return;
        }


        try {

            ClienteDAO.eliminar(
                    clienteSeleccionado.getNombreEntidad()
            );


            listaClientesObs.remove(
                    clienteSeleccionado
            );


            limpiarCampos();

            actualizarTotalClientes();


            System.out.println(
                    "Cliente eliminado con éxito."
            );


        } catch (Exception e) {

            e.printStackTrace();

            AlertasUtils.mostrarInformacion(
                    "ERROR",
                    "No se pudo eliminar el cliente."
            );
        }
    }


    /* =========================================================
       BUSCAR
       ========================================================= */

    @FXML
    void botonLupa(ActionEvent event) {

        String buscar =
                buscadorClientes
                        .getText()
                        .toLowerCase()
                        .trim();


        if (buscar.isEmpty()) {

            tablaClientes.setItems(
                    listaClientesObs
            );

            return;
        }


        BuscadorUtils.configuradorBuscador(

                buscadorClientes,

                tablaClientes,

                listaClientesObs,

                (cliente, texto) -> {

                    boolean coincideNombre =
                            cliente.getNombreEntidad() != null
                                    && cliente
                                    .getNombreEntidad()
                                    .toLowerCase()
                                    .contains(texto);


                    boolean coincideDni =
                            cliente.getDniEntidad() != null
                                    && cliente
                                    .getDniEntidad()
                                    .toLowerCase()
                                    .contains(texto);


                    boolean coincideTelefono =
                            cliente.getTelefonoEntidad() != null
                                    && cliente
                                    .getTelefonoEntidad()
                                    .toLowerCase()
                                    .contains(texto);


                    return coincideNombre
                            || coincideDni
                            || coincideTelefono;
                }
        );
    }


    /* =========================================================
       HISTORIAL DE FACTURAS
       ========================================================= */

    @FXML
    private void abrirHistorialCliente(ActionEvent event) {

        clienteClase clienteSeleccionado =
                tablaClientes
                        .getSelectionModel()
                        .getSelectedItem();


        if (clienteSeleccionado == null) {

            AlertasUtils.mostrarInformacion(
                    "Cliente no seleccionado",
                    "Seleccioná un cliente de la tabla para ver su historial de facturas."
            );

            return;
        }


        /*
         * Por ahora verificamos que el botón recibe
         * correctamente al cliente seleccionado.
         *
         * Después conectamos acá la pantalla real
         * del historial de facturas.
         */

        System.out.println(
                "Historial de facturas del cliente: "
                        + clienteSeleccionado.getNombreEntidad()
        );
    }


    /* =========================================================
       TOTAL CLIENTES
       ========================================================= */

    private void actualizarTotalClientes() {

        if (totalclientes != null) {

            totalclientes.setText(
                    "Cantidad total de clientes: "
                            + listaClientesObs.size()
            );
        }
    }


    /* =========================================================
       LIMPIAR
       ========================================================= */

    private void limpiarCampos() {

        nombreApellido.clear();

        dni.clear();

        telefono.clear();

        email.clear();

        direccion.clear();

        cuil.clear();
    }
}