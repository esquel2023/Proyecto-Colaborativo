package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.Optional;

public class ControladorProducto {

    @FXML public TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colCantidad;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TextField txtbuscadorProductos;
    @FXML private Button botonSalir;

    public static final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    public static Producto productoseleccionado = null;
    private controladorProveedorSelec proveedorSelec;

    @FXML
    public void initialize() {
        // Enlace moderno mediante Lambdas (Soluciona incompatibilidades de mayúsculas y minúsculas)
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoBarraProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());

        cargarDatosDesdeBD();

        // Configuración segura del Buscador Reutilizable
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

        // Listener de Selección de Filas
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ControladorProducto.productoseleccionado = newValue;
                if (proveedorSelec != null) {
                    proveedorSelec.recibirProducto(newValue);
                }
                System.out.println("Seleccionaste: " + newValue.getNombre());
            }
        });
    }

    @FXML
    private void clickModificar(ActionEvent event) {
        if (ControladorProducto.productoseleccionado == null) {
            AlertasUtils.mostrarAdvertencia("Sin selección", "Debes seleccionar un producto de la tabla para poder modificarlo.");
            return;
        }

        Producto.productoSeleccionadoParaEditar = ControladorProducto.productoseleccionado;
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Modificar Producto", true);

        tablaProductos.refresh(); // Refresca cambios visuales en el acto
        limpiarSeleccion();
    }

    @FXML
    private void clickAgregar(ActionEvent event) {
        Producto.productoSeleccionadoParaEditar = null;
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Agregar Nuevo Producto", true);

        cargarDatosDesdeBD(); // Trae el nuevo ítem con el ID que generó la BD
        limpiarSeleccion();
    }

    @FXML
    public void clickEliminar(ActionEvent event) {
        if (productoseleccionado == null) {
            AlertasUtils.mostrarInformacion("Producto no Seleccionado", "Debes seleccionar un producto de la tabla para eliminarlo.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Estás seguro de que querés eliminar este producto?");
        alerta.setContentText("Producto: " + productoseleccionado.getNombre() + "\nEsta acción no se puede deshacer.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                ProductoDAO.eliminar(productoseleccionado.getidProducto());
                listaProductos.remove(productoseleccionado); // Remueve directo de la vista reactiva
                AlertasUtils.mostrarInformacion("Éxito", "El producto se eliminó correctamente.");
            } catch (SQLException e) {
                AlertasUtils.mostrarError("Error de BD", "No se pudo eliminar el producto de la base de datos.");
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
            // 1. Traemos los datos frescos del motor de base de datos
            var productosBD = ProductoDAO.listar();

            // 2. setAll limpia la lista original e inyecta los nuevos elementos.
            // Como BuscadorUtils está escuchando esta lista, la FilteredList se actualiza sola.
            listaProductos.setAll(productosBD);

            // ✅ REMOVIDO: Ya no volvemos a hacer tablaProductos.setItems(...).
            // Dejamos que la SortedList de BuscadorUtils mantenga el control total de la vista.

        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de Base de Datos",
                    "No se pudieron recuperar los productos de la base de datos.");
            e.printStackTrace();
        }
    }

    private void limpiarSeleccion() {
        tablaProductos.getSelectionModel().clearSelection();
        ControladorProducto.productoseleccionado = null;
    }

    public void setProveedorSelec(controladorProveedorSelec controladorProveedorSelec) {
        this.proveedorSelec = proveedorSelec;
    }
}
