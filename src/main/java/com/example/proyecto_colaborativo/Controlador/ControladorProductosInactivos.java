package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.bd.ProductoDAO; // Ajustá según tu paquete de DAO
import com.example.proyecto_colaborativo.Utilits.AlertasUtils; // Ajustá tus clases útiles
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.Optional;

public class ControladorProductosInactivos {

    @FXML private Button botonSalir;
    @FXML private TextField txtbuscadorProductos;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TextField porcentaje;
    @FXML private TextField precioCosto;

    // Lista en memoria idéntica al controlador original
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // Variable estática para la selección dentro de esta pantalla
    public static Producto productoInactivoSeleccionado = null;

    @FXML
    public void initialize() {
        // Enlace de columnas usando las propiedades nativas de tu modelo
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoBarraProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());

        // Listener de Selección de Filas idéntico a tu lógica
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ControladorProductosInactivos.productoInactivoSeleccionado = newValue;
                System.out.println("Seleccionaste inactivo: " + newValue.getNombre());
            }
        });

        // Aplicamos el filtro reactivo para ver SOLO los desactivados
        FilteredList<Producto> listaFiltrada = new FilteredList<>(listaProductos, p -> !p.isActivado());

        // Si usás BuscadorUtils como en tu otra pantalla, podés pasarle listaFiltrada aquí.
        // De lo contrario, vinculamos directo a la tabla:
        tablaProductos.setItems(listaFiltrada);

        cargarDatosDesdeBD();
    }

    @FXML
    private void clickReactivar(ActionEvent event) {
        if (productoInactivoSeleccionado == null) {
            AlertasUtils.mostrarAdvertencia("Sin selección", "Debes seleccionar un producto para reactivarlo.");
            return;
        }

        try {
            // Cambiamos el estado en el objeto y actualizamos en la BD
            productoInactivoSeleccionado.setActivado(true);
            ProductoDAO.modificar(productoInactivoSeleccionado); // Asegúrate de tener este método en tu DAO

            AlertasUtils.mostrarInformacion("Éxito", "El producto se ha reactivado correctamente.");
            cargarDatosDesdeBD(); // Recarga y la FilteredList lo quitará de la vista automáticamente
        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de BD", "No se pudo reactivar el producto.");
            e.printStackTrace();
        } finally {
            limpiarSeleccion();
        }
    }

    @FXML
    public void clickEliminar(ActionEvent event) {
        if (productoInactivoSeleccionado == null) {
            AlertasUtils.mostrarInformacion("Producto no Seleccionado", "Debes seleccionar un producto para eliminarlo definitivamente.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación Definitiva");
        alerta.setHeaderText("¿Estás seguro de que querés borrar permanentemente este producto?");
        alerta.setContentText("Producto: " + productoInactivoSeleccionado.getNombre() + "\nEsta acción eliminará el registro físico.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                ProductoDAO.eliminar(productoInactivoSeleccionado.getidProducto());
                listaProductos.remove(productoInactivoSeleccionado);
                AlertasUtils.mostrarInformacion("Éxito", "El producto se eliminó de forma permanente.");
            } catch (SQLException e) {
                AlertasUtils.mostrarError("Error de BD", "No se pudo eliminar de la base de datos.");
                e.printStackTrace();
            } finally {
                limpiarSeleccion();
            }
        }
    }

    @FXML
    void clickSalir(ActionEvent event) {
        if (botonSalir.getScene() != null) {
            botonSalir.getScene().getWindow().hide();
        }
    }

    private void cargarDatosDesdeBD() {
        try {
            var productosBD = ProductoDAO.listar();
            listaProductos.setAll(productosBD);
        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de Base de Datos", "No se pudieron recuperar los productos.");
            e.printStackTrace();
        }
    }

    private void limpiarSeleccion() {
        tablaProductos.getSelectionModel().clearSelection();
        ControladorProductosInactivos.productoInactivoSeleccionado = null;
    }
}
