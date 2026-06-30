package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import com.example.proyecto_colaborativo.bd.ProductoProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class controladorProveedorSelec {

    private static controladorProveedorSelec instanciaActiva;

    @FXML public Button botonAgregar;
    @FXML public Button botonEliminar;
    @FXML public TableView<Producto> tablaProductosProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor;
    @FXML public TableColumn<Producto, String> precioProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor1;

    @FXML private Label proveedorSelec;

    // Lista observable correcta vinculada a la UI
    private final ObservableList<Producto> listaProductosObs = FXCollections.observableArrayList();
    private proovedorClase proveedorActual;

    // ELIMINADO EL CONSTRUCTOR CON PARÁMETROS (JavaFX usará el constructor vacío implícito)

    @FXML
    public void initialize() {
        instanciaActiva = this;

        // CORREGIDO: Deben mapear las variables reales de tu objeto Producto (ver tu ProductoDAO)
        prooductosProovedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioProovedor.setCellValueFactory(new PropertyValueFactory<>("precio"));
        prooductosProovedor1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Enlazamos la lista observable a la tabla visual una sola vez
        tablaProductosProovedor.setItems(listaProductosObs);
    }

    /**
     * Método puente llamado externamente al seleccionar un proveedor en la otra pantalla.
     */
    public static void setProveedorSelec(proovedorClase proveedor) {
        if (instanciaActiva != null && proveedor != null) {
            instanciaActiva.proveedorActual = proveedor;

            if (instanciaActiva.proveedorSelec != null) {
                instanciaActiva.proveedorSelec.setText(proveedor.getNombreEntidad());
            }

            // Refrescamos la tabla automáticamente con el ID real del proveedor
            instanciaActiva.actualizarTabla(proveedor.getId());
        }
    }

    private void actualizarTabla(int idProveedor) {
        // Buscamos la lista en la base de datos usando el método del DAO
        List<Producto> listaBD = ProductoProveedorDAO.listar(idProveedor);
        // Actualizamos la lista observable de JavaFX
        listaProductosObs.setAll(listaBD);
    }

    @FXML
    public void botonAgregar(ActionEvent actionEvent) {

            if (proveedorActual == null) {
                return;
            }

            // 1. Traemos TODOS los productos del sistema usando tu ProductoDAO.listar()
            List<Producto> todosLosProductos = ProductoDAO.listar();

            // 2. Abrimos una ventanita emergente para que el usuario elija uno
            javafx.scene.control.ChoiceDialog<Producto> dialogo = new javafx.scene.control.ChoiceDialog<>(null, todosLosProductos);
            dialogo.setTitle("Asociar Producto");
            dialogo.setHeaderText("Selecciona el producto que vende este proveedor:");
            dialogo.setContentText("Producto:");

            // 3. Si el usuario selecciona un producto y presiona OK
            dialogo.showAndWait().ifPresent(productoElegido -> {
                // 4. Lo guardamos en la base de datos usando el nuevo método del DAO
                ProductoProveedorDAO.asociar(productoElegido.getidProducto(), proveedorActual.getId());

                // 5. Refrescamos la tabla para que aparezca el nuevo producto en la lista
                actualizarTabla(proveedorActual.getId());
            });


    }

    @FXML
    public void botonElimina(ActionEvent actionEvent) {
        // Lógica para desasociar el producto seleccionado
    }
}
