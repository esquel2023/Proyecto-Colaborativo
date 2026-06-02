package com.example.proyecto_colaborativo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class controladorCliente {
    clienteClase Cliente;

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

    public TableView<Object> tablaClientes;
    @FXML private TableColumn<clienteClase, String> nombreTabla;
    @FXML private TableColumn<clienteClase, String> dniTabla;
    @FXML private TableColumn<clienteClase, String> telefonoTabla;

    // Lista especial de JavaFX para que los cambios se vean reflejados en la tabla automáticamente
    private final ObservableList<Object> listaClientesObs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Vincula las columnas de la tabla con los getters heredados de eliseoClase
        nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
        dniTabla.setCellValueFactory(new PropertyValueFactory<>("dniEntidad"));
        telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

        // Asigna la lista a la tabla
        tablaClientes.setItems(listaClientesObs);
    }

    @FXML
    void botonAgregar(ActionEvent event) {
        // 1. Capturar datos de los TextField
        String txtNombre = nombreApellido.getText();
        String txtDni = dni.getText();
        String txtTelefono = telefono.getText();
        String txtEmail = email.getText();
        String txtDireccion = direccion.getText();
        String txtCuil = cuil.getText();

        // Validar campos obligatorios
        if (txtNombre.isEmpty() || txtDni.isEmpty() || txtCuil.isEmpty() || txtDireccion.isEmpty() || txtEmail.isEmpty() || txtTelefono.isEmpty()) {
            return; // Detiene la ejecución si están vacíos
        }

        // 2. Instanciar tu objeto Cliente
        clienteClase nuevoCliente = new clienteClase(txtNombre, txtDni, txtTelefono, txtEmail, txtDireccion, txtCuil);


        // 3. Agregar a la lista (actualiza la interfaz al instante)
        listaClientesObs.add(nuevoCliente);

        // 4. Limpiar los campos
        limpiarCampos();
    }

    @FXML
    void botonModificar(ActionEvent event) {
        // Obtiene el cliente seleccionado en la tabla
        clienteClase clienteSeleccionado = (clienteClase) tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            // Actualiza los datos del objeto con lo que haya en los TextField
            clienteSeleccionado.setNombreEntidad(nombreApellido.getText());
            clienteSeleccionado.setDniEntidad(dni.getText());
            clienteSeleccionado.setTelefonoEntidad(telefono.getText());
            clienteSeleccionado.setEmailEntidad(email.getText());
            clienteSeleccionado.setDireccionEntidad(direccion.getText());
            clienteSeleccionado.setCuitcuilEntidad(cuil.getText());

            tablaClientes.refresh(); // Refresca los cambios visuales en la tabla
            limpiarCampos();
        }
    }

    @FXML
    void botonElimina(ActionEvent event) { // Nombre exacto del onAction en tu FXML
        Object clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            listaClientesObs.remove(clienteSeleccionado);
            limpiarCampos();
        }
    }

    @FXML
    void botonLupa(ActionEvent event) {
        String buscar = buscadorClientes.getText().toLowerCase();
        // Lógica de filtrado en la tabla (se puede expandir)
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

