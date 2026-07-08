package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static com.example.proyecto_colaborativo.Utilits.NavegacionUtils.abrirPantalla;

public class controladorProovedor {
/*

    public TextField buscadorProovedores;
    @FXML public TableView<proovedorClase> tablaProovedores;
    @FXML public TableView<proovedorClase> tablaProductosProovedor;
    @FXML public TableColumn<proovedorClase, String> prooductosProovedor;
    @FXML public TableColumn<proovedorClase, String> precioProovedor;
    @FXML public TableColumn<proovedorClase, String> nombreTabla;
    @FXML public TableColumn<proovedorClase, String> telefonoTabla;
    @FXML public SplitMenuButton splitIva;
    public ComboBox condicionIVA;


    @FXML private TextField cuil;
    @FXML private TextField telefono;
    @FXML private TextField nombre;
    @FXML private TextField email;
    public TextField pais;
    public TextField provincia;
    public TextField localidad;

 */

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
    public TableColumn<proovedorClase,String> nombreTabla;
    public TableColumn<proovedorClase,String> telefonoTabla;
    public TableColumn<proovedorClase,String> colEmail;

    public ComboBox<String> condicionIVA;
    
    public TextField buscadorProovedores;
    public Button botonAgregar;
    public Button botonProducto;

    proovedorClase proveedorSelec;

    public void initialize() {
        condicionIVA.getItems().addAll(
                "Responsable Inscripto",
                "Monotributista"

        );
        if (tablaProovedores != null) {
            tablaProovedores.setPlaceholder(new Label("No hay proveedores cargados"));

            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));



            listaProveedoresObs.setAll(ProveedorDAO.listar());

            tablaProovedores.setItems(listaProveedoresObs);


            tablaProovedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            try{
                ProveedorDAO.eliminar(proveedorSeleccionado.getNombreEntidad());
                listaProveedoresObs.remove(proveedorSeleccionado);
                limpiarCampos();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

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

                ProveedorDAO.insertar(nuevoProveedor);

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
                nuevoemail.isEmpty() || nuevotelefono.isEmpty()|| nuevaciudad.isEmpty() || nuevopais.isEmpty() || nuevaprov.isEmpty()) {
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
}



