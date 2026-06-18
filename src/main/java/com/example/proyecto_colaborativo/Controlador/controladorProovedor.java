package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

public class controladorProovedor {


    public TextField buscadorProovedores;
    @FXML public TableView<Object> tablaProovedores;
    @FXML public TableView<Object> tablaProductosProovedor;
    @FXML public TableColumn<Object, String> prooductosProovedor;
    @FXML public TableColumn<Object, String> precioProovedor;
    @FXML public TableColumn<Object, String> nombreTabla;
    @FXML public TableColumn<Object, String> telefonoTabla;


    @FXML private Button lupa;
    @FXML private Button botonAgregar;
    @FXML private Button botonModificar;
    @FXML private Button botonEliminar;
    @FXML private TextField cuil;
    @FXML private TextField telefono;
    @FXML private TextField nombre;
    @FXML private TextField direccion;
    @FXML private TextField email;

    private final ObservableList<Object> listaProveedoresObs = FXCollections.observableArrayList();
    private final ObservableList<Object> listaProductosProveedorObs = FXCollections.observableArrayList();
private proovedorClase proveedorSeleccionado;
    public void initialize() {
        if (tablaProovedores != null) {
            tablaProovedores.setPlaceholder(new Label("No hay proveedores cargados"));
            tablaProductosProovedor.setPlaceholder(new Label("Este proveedor no tiene productos"));

            // 1. Configuración de mapeo de columnas de las tablas
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));
            prooductosProovedor.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
            precioProovedor.setCellValueFactory(new PropertyValueFactory<>("precioProducto"));

            listaProveedoresObs.setAll(ProveedorDAO.listar());

            tablaProovedores.setItems(listaProveedoresObs);
           // tablaProductosProovedor.setItems(listaProductosProveedorObs);

            // 3. Listener de selección optimizado
            tablaProovedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Si no hay nada seleccionado (o se deseleccionó), limpiamos todo
                if (newValue == null) {
                    listaProductosProveedorObs.clear();
                    limpiarCampos();
                    this.proveedorSeleccionado = null;
                    return;
                }

                nombre.setText(proveedorSeleccionado.getNombreEntidad());
                telefono.setText(String.valueOf(proveedorSeleccionado.getTelefonoEntidad()));
                email.setText(proveedorSeleccionado.getEmailEntidad());
                direccion.setText(proveedorSeleccionado.getDireccionEntidad());
                cuil.setText(proveedorSeleccionado.getCuitcuilEntidad());

                cargarProductosDelProveedor(proveedorSeleccionado);
            });
        }
    }

    private void cargarProductosDelProveedor(Object proveedor) {
        // Lógica para filtrar o cargar productos del proveedor seleccionado
    }

    @FXML
    void botonAgregar(ActionEvent event) throws IOException {

        String txtNombre = nombre.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtDireccion = direccion.getText();
        String txtCuil = cuil.getText();

        if (txtNombre.isEmpty() || txtCuil.isEmpty() ||
                txtDireccion.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            AlertasUtils.mostrarAlerta("Campos vacios", "Falta completar informacion", "Por favor, complete los campos faltantes y vuelva a intentarlo.", Alert.AlertType.INFORMATION);

            return;
        }


        String mensaje = String.format(
                "¿Confirmas los datos del proveedor?\n\n" +
                        "Nombre: %s\n" +
                        "Teléfono: %s\n" +
                        "Email: %s\n" +
                        "Dirección: %s\n" +
                        "CUIL: %s",
                txtNombre, txtTelefono, txtEmail, txtDireccion, txtCuil
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
            // Aquí agregas el objeto correspondiente a tu lista observable de proveedores
            proovedorClase proveedor = new proovedorClase(1, txtNombre, txtTelefono, txtEmail, txtDireccion, txtCuil);
            ProveedorDAO.insertar(proveedor);

            listaProveedoresObs.setAll(ClienteDAO.listar()); // Recarga limpia desde la base de datos
            limpiarCampos();
            System.out.println("Cliente agregado con éxito.");
        } else {
            System.out.println("El usuario decidió corregir los datos.");
        }
    }

    @FXML
    void botonModificar(ActionEvent event) {
        // 1. Obtener el proveedor seleccionado de la tabla
        Object seleccionado = tablaProovedores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            System.out.println("Error: Debes seleccionar un proveedor de la tabla.");
            return;
        }

        // 2. Convertir el Object al tipo real de tu clase proovedorClase
        proovedorClase proveedorSeleccionado = (proovedorClase) seleccionado;

        // 3. Tomar los nuevos valores escritos en tus TextField
        String nuevonombre = nombre.getText();
        String nuevotelefono = telefono.getText();
        String nuevoemail = email.getText();
        String nuevadireccion = direccion.getText();
        String nuevocuil = cuil.getText();

        // 4. Validar que no dejen ningún campo vacío
        if (nuevonombre.isEmpty() || nuevocuil.isEmpty() ||
                nuevadireccion.isEmpty() || nuevoemail.isEmpty() || nuevotelefono.isEmpty()) {
            System.out.println("Error: No puedes dejar campos vacíos.");
            return;
        }

        // 5. Modificar las propiedades usando los setters que me mostraste
        proveedorSeleccionado.setNombreEntidad(nuevonombre);
        proveedorSeleccionado.setTelefonoEntidad(Integer.valueOf(nuevotelefono));
        proveedorSeleccionado.setEmailEntidad(nuevoemail);
        proveedorSeleccionado.setDireccionEntidad(nuevadireccion);
        proveedorSeleccionado.setCuitcuilEntidad(nuevocuil);

        // 6. Refrescar la tabla para actualizar la pantalla de inmediato
        tablaProovedores.refresh();

        // 7. Limpiar la selección de la tabla y los campos de texto
        tablaProovedores.getSelectionModel().clearSelection();
        limpiarCampos();
        System.out.println("¡Proveedor modificado con éxito!");
    }

    @FXML
    void botonElimina(ActionEvent event) {
        Object proveedorSeleccionado = tablaProovedores.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            listaProveedoresObs.remove(proveedorSeleccionado);
            limpiarCampos();
        }
    }

    @FXML
    void botonLupa(ActionEvent event) {
        String buscar = buscadorProovedores.getText().toLowerCase();
    }

    private void limpiarCampos() {
        nombre.clear();
        telefono.clear();
        email.clear();
        direccion.clear();
        cuil.clear();
    }
}



