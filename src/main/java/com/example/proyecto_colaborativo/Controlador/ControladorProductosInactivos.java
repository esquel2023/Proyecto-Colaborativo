package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
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
    /*

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
        // 1. Enlace de columnas usando propiedades nativas
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoBarraProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());

        // 2. Cargar los datos iniciales desde la BD (Llena listaProductos con los inactivos)
        cargarDatosDesdeBD();

        // 3. ✅ ACTIVAR EL BUSCADO EN TIEMPO REAL:
        // Pasamos tu campo txtbuscadorProductos, tu tabla y la lista de inactivos
        BuscadorUtils.configuradorBuscador(
                txtbuscadorProductos,
                tablaProductos,
                listaProductos,
                (producto, texto) -> {
                    boolean coincideNombre = producto.getNombre() != null &&
                            producto.getNombre().toLowerCase().contains(texto);
                    boolean coincideCodigo = producto.getCodigoBarra() != null &&
                            producto.getCodigoBarra().toLowerCase().contains(texto);
                    return coincideNombre || coincideCodigo;
                }
        );

        // 4. Listener de Selección de Filas
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ControladorProductosInactivos.productoInactivoSeleccionado = newValue;
                System.out.println("Seleccionaste inactivo: " + newValue.getNombre());
            }
        });
    }


    @FXML
    private void clickReactivar(ActionEvent event) {
        // 1. Validamos que el usuario haya seleccionado una fila de la tabla
        if (productoInactivoSeleccionado == null) {
            AlertasUtils.mostrarAdvertencia("Sin selección", "Debes seleccionar un producto para reactivarlo.");
            return;
        }

        try {
            // 2. Modificamos el check internamente en el objeto pasándolo a true
            //productoInactivoSeleccionado.setActivado(true);

            // 3. Persistimos el cambio llamando al método oficial y seguro del DAO
            ProductoDAO.actualizar(productoInactivoSeleccionado);

            // 4. Avisamos al usuario del éxito de la operación
            AlertasUtils.mostrarInformacion("Éxito", "El producto '" + productoInactivoSeleccionado.getNombre() + "' se ha reactivado correctamente.");

            // 5. Volvemos a leer de la BD (como ahora es 'true', desaparecerá automáticamente de esta tabla)
            cargarDatosDesdeBD();

        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de BD", "No se pudo reactivar el producto en la base de datos.");
            e.printStackTrace();
        } finally {
            // 6. Limpiamos la selección de la tabla por seguridad
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
            // 💡 CAMBIO: Llamamos directamente al nuevo método especializado del DAO
            var productosInactivosBD = ProductoDAO.listarInactivos();

            // setAll limpia la tabla e inyecta los registros frescos de la BD
            listaProductos.setAll(productosInactivosBD);

        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de Base de Datos",
                    "No se pudieron recuperar los productos inactivos de la base de datos.");
            e.printStackTrace();
        }
    }

    private void limpiarSeleccion() {
        tablaProductos.getSelectionModel().clearSelection();
        ControladorProductosInactivos.productoInactivoSeleccionado = null;
    }

     */
}
