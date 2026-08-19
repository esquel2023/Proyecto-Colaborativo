package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;

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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class controladorProovedor {

    /* =========================================================
       CAMPOS DEL FORMULARIO
       ========================================================= */

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


    /* =========================================================
       BUSCADOR
       ========================================================= */

    @FXML
    private TextField txtBuscar;


    /* =========================================================
       TABLA
       ========================================================= */

    @FXML
    private TableView<proovedorClase> tablaProveedores;

    @FXML
    private TableColumn<proovedorClase, String> colNombre;

    @FXML
    private TableColumn<proovedorClase, String> colTelefono;

    @FXML
    private TableColumn<proovedorClase, String> colEmail;


    /* =========================================================
       BOTONES
       ========================================================= */

    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonModificar;

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonBuscar;


    /* =========================================================
       FECHA Y HORA
       ========================================================= */

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaLabel;


    /* =========================================================
       LISTA
       ========================================================= */

    private final ObservableList<proovedorClase> listaProveedores =
            FXCollections.observableArrayList();


    /* =========================================================
       INITIALIZE
       ========================================================= */

    @FXML
    public void initialize() {

        configurarTabla();

        cargarProveedores();

        configurarComboIVA();

        configurarSeleccionTabla();

        iniciarReloj();
    }


    /* =========================================================
       CONFIGURAR TABLA
       ========================================================= */

    private void configurarTabla() {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombreEntidad")
        );

        colTelefono.setCellValueFactory(
                new PropertyValueFactory<>("telefonoEntidad")
        );

        colEmail.setCellValueFactory(
                new PropertyValueFactory<>("emailEntidad")
        );

        tablaProveedores.setPlaceholder(
                new Label("No hay proveedores cargados")
        );

        tablaProveedores.setItems(listaProveedores);
    }


    /* =========================================================
       CARGAR PROVEEDORES
       ========================================================= */

    private void cargarProveedores() {

        listaProveedores.clear();

        listaProveedores.addAll(
                ProveedorDAO.listar()
        );
    }


    /* =========================================================
       COMBO CONDICION IVA
       ========================================================= */

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


    /* =========================================================
       SELECCION DE TABLA
       ========================================================= */

    private void configurarSeleccionTabla() {

        tablaProveedores
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, anterior, seleccionado) -> {

                            if (seleccionado != null) {

                                nombre.setText(
                                        seleccionado.getNombreEntidad()
                                );

                                telefono.setText(
                                        seleccionado.getTelefonoEntidad()
                                );

                                email.setText(
                                        seleccionado.getEmailEntidad()
                                );

                                cuit.setText(
                                        seleccionado.getCuitcuilEntidad()
                                );
                            }
                        }
                );
    }


    /* =========================================================
       AGREGAR
       ========================================================= */

    @FXML
    private void botonAgregar(ActionEvent event)
            throws IOException {

        String txtNombre = nombre.getText().trim();
        String txtTelefono = telefono.getText().trim();
        String txtEmail = email.getText().trim();
        String txtCuit = cuit.getText().trim();


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


        Alert alerta =
                new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle(
                "Confirmación de proveedor"
        );

        alerta.setHeaderText(
                "Revisá los datos antes de guardar"
        );

        alerta.setContentText(mensaje);


        ButtonType confirmar =
                new ButtonType("Confirmar");

        ButtonType cancelar =
                new ButtonType("Cancelar");


        alerta.getButtonTypes().setAll(
                confirmar,
                cancelar
        );


        Optional<ButtonType> resultado =
                alerta.showAndWait();


        if (resultado.isPresent()
                && resultado.get() == confirmar) {

            proovedorClase proveedor =
                    new proovedorClase(
                            1,
                            txtNombre,
                            txtTelefono,
                            txtEmail,
                            txtCuit
                    );


            ProveedorDAO.insertar(proveedor);


            cargarProveedores();

            limpiarCampos();
        }
    }


    /* =========================================================
       MODIFICAR
       ========================================================= */

    @FXML
    private void botonModificar(ActionEvent event) {

        proovedorClase proveedorSeleccionado =
                tablaProveedores
                        .getSelectionModel()
                        .getSelectedItem();


        if (proveedorSeleccionado == null) {

            AlertasUtils.mostrarInformacion(
                    "Proveedor",
                    "Seleccione un proveedor de la tabla."
            );

            return;
        }


        String nuevoNombre =
                nombre.getText().trim();

        String nuevoTelefono =
                telefono.getText().trim();

        String nuevoEmail =
                email.getText().trim();

        String nuevoCuit =
                cuit.getText().trim();


        if (nuevoNombre.isEmpty()
                || nuevoTelefono.isEmpty()
                || nuevoEmail.isEmpty()
                || nuevoCuit.isEmpty()) {

            AlertasUtils.mostrarInformacion(
                    "Campos vacíos",
                    "No puede dejar campos vacíos."
            );

            return;
        }


        proveedorSeleccionado.setNombreEntidad(
                nuevoNombre
        );

        proveedorSeleccionado.setTelefonoEntidad(
                nuevoTelefono
        );

        proveedorSeleccionado.setEmailEntidad(
                nuevoEmail
        );

        proveedorSeleccionado.setCuitcuilEntidad(
                nuevoCuit
        );


        tablaProveedores.refresh();

        tablaProveedores
                .getSelectionModel()
                .clearSelection();

        limpiarCampos();
    }


    /* =========================================================
       ELIMINAR
       ========================================================= */

    @FXML
    private void botonEliminar(ActionEvent event) {

        proovedorClase proveedorSeleccionado =
                tablaProveedores
                        .getSelectionModel()
                        .getSelectedItem();


        if (proveedorSeleccionado == null) {

            AlertasUtils.mostrarInformacion(
                    "Proveedor",
                    "Seleccione un proveedor para eliminar."
            );

            return;
        }


        Alert alerta =
                new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle("Eliminar proveedor");

        alerta.setHeaderText(
                "¿Desea eliminar este proveedor?"
        );

        alerta.setContentText(
                proveedorSeleccionado.getNombreEntidad()
        );


        Optional<ButtonType> resultado =
                alerta.showAndWait();


        if (resultado.isPresent()
                && resultado.get() == ButtonType.OK) {

            try {

                ProveedorDAO.eliminar(
                        proveedorSeleccionado
                                .getNombreEntidad()
                );

                listaProveedores.remove(
                        proveedorSeleccionado
                );

                limpiarCampos();

            } catch (SQLException e) {

                e.printStackTrace();

                AlertasUtils.mostrarInformacion(
                        "Error",
                        "No se pudo eliminar el proveedor."
                );
            }
        }
    }


    /* =========================================================
       BUSCAR
       ========================================================= */

    @FXML
    private void buscarProveedor(ActionEvent event) {

        String texto =
                txtBuscar
                        .getText()
                        .trim()
                        .toLowerCase();


        if (texto.isEmpty()) {

            tablaProveedores.setItems(
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


        tablaProveedores.setItems(
                filtrados
        );
    }


    /* =========================================================
       LIMPIAR CAMPOS
       ========================================================= */

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


    /* =========================================================
       RELOJ
       ========================================================= */

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
}