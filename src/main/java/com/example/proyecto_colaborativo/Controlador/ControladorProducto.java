package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ControladorProducto {


    // 1. Inyectamos la TableView apuntando a la clase Producto
    @FXML
    public TableView<Producto> tablaProductos;
    // 2. Inyectamos las columnas existentes de tu FXML
    @FXML
    private TableColumn<Producto, Integer> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    //@FXML
    //private TextField codigoBarras;
    @FXML
    private TextField codigo;
    //@FXML
    //private TextField nombre;
    // @FXML
    //private TextField cantidad;
    // @FXML
    //private TextField precioFinal;
    @FXML
    private TextField txtbuscadorProductos;
    @FXML
    private Button botonSalir;
    // @FXML
    // private TextField porcentaje;
    // @FXML
    // private TextField precioCosto;


    // Lista observable que contendrá los productos reales
    // Lista observable única para toda la clase
    public static final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // VARIABLE NUEVA: Guarda el objeto seleccionado para poder modificarlo después
    public static Producto productoseleccionado;
    private controladorProveedorSelec proveedorSelec;

    @FXML
    public void initialize() {
        // 3. Vinculamos cada columna con el nombre exacto de la propiedad en la clase Producto

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        //listaProductos.setAll(ProductoDAO.listar());

        // MEJORA: Cargamos los datos reales desde la base de datos en vez de datos fijos
        cargarDatosDesdeBD();


        // ==========================================
        // LLAMADA A LA CLASE REUTILIZABLE
        // ==========================================

        BuscadorUtils.configuradorBuscador(
                txtbuscadorProductos,
                tablaProductos,
                listaProductos,
                (producto, texto) -> {
                    // Validación segura contra valores nulos
                    boolean coincideNombre = producto.getNombre() != null &&
                            producto.getNombre().toLowerCase().contains(texto);

                    boolean coincideCodigo = producto.getCodigoBarra() != null &&
                            producto.getCodigoBarra().toLowerCase().contains(texto);

                    return coincideNombre || coincideCodigo;


                    // Acá definís la lógica específica para la clase Producto
                    // return producto.getNombre().toLowerCase().contains(texto) ||
                    //         producto.getCodigoBarra().toLowerCase().contains(texto);

                }
        );

        // Escuchar cuando el usuario selecciona una fila de la tabla
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // ASIGNACIÓN: Guardamos la referencia del producto seleccionado
                ControladorProducto.productoseleccionado = newValue;
                if (proveedorSelec != null) {
                    proveedorSelec.recibirProducto(newValue);

                }
                // 'newValue' contiene el objeto Producto seleccionado
                System.out.println("Seleccionaste: " + newValue.getNombre());


            }

        });


    }

    // MÉTODO NUEVO: Se ejecuta al presionar el botón Modificar
    @FXML
    private void clickModificar(ActionEvent event) {
        // 1. Validar que el usuario haya seleccionado una fila previamente
        if (this.productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto",
                    "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);
            return;
        }
        // 2. Mandar el producto al "puente" estático para que la otra pantalla lo pueda ver
        Producto.productoSeleccionadoParaEditar = this.productoseleccionado;
        // 3. Abrir la pantalla. IMPORTANTE: Poné 'true' (Modal) para que el código se pause
        // hasta que el usuario termine de editar en la otra ventana.
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Modificar Producto", true);

        // 4. Al regresar (cuando se cierra la ventana de edición), refrescamos la tabla y limpiamos
        tablaProductos.refresh();
        tablaProductos.getSelectionModel().clearSelection();
        this.productoseleccionado = null;

        // 2. Cargar la pantalla obteniendo su controlador (pasamos 'true' para que sea Modal)
        //ControladorProductoAgregar controller = NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Modificar Producto", true);


        // 3. Mandar el registro seleccionado a la nueva pantalla
//        if (controller != null) {
        //          controller.cargarProducto(this.productoseleccionado);
        //    }

        // 4. Al regresar (cuando se cierra la modal), refrescar cambios visuales
        //  tablaProductos.refresh();
        //  tablaProductos.getSelectionModel().clearSelection();
        //  this.productoseleccionado = null;

    }


    @FXML
    private void clickAgregar(ActionEvent event) {

        // Solo pasás: ruta del FXML, título de la ventana y si es modal (true/false)
        NavegacionUtils.abrirPantalla("ProductoAgregar.fxml", "Agregar Nuevo Producto", true);

        // 2. Al regresar (cuando el usuario le da a Guardar y la ventana se cierra),
        // volvemos a consultar la base de datos para traer el nuevo producto con su ID real.
        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {

        try {
            listaProductos.clear();
            //listaProductos.addAll(ProductoDAO.listar());
            listaProductos.setAll(ProductoDAO.listar());
            //tablaProductos.setItems(listaProductos);
        } catch (Exception e) {
            AlertasUtils.mostrarAlerta("Error de BD", "Error de lectura",
                    "No se pudieron recuperar los productos de la base de datos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    //----------------------------------------------------------------------------------------
    //FUNCIONES
    //----------------------------------------------------------------------------------------
    public void clickEliminar(ActionEvent event) throws SQLException {
        // 1. Validar que el usuario haya seleccionado un producto de la tabla
        if (productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Error", "Producto no Seleccionado", "Debes seleccionar un producto de la tabla para eliminarlo.", Alert.AlertType.INFORMATION);

            return;
        }

        // 2. Alerta de confirmación visual
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Estás seguro de que querés eliminar este producto?");
        alerta.setContentText("Producto: " + productoseleccionado.getNombre() + "\nEsta acción no se puede deshacer.");

        // 3. Mostrar la alerta y esperar la respuesta del usuario
        java.util.Optional<javafx.scene.control.ButtonType> resultado = alerta.showAndWait();

        // 4. Si el usuario hace clic en OK, se procede a la eliminación
        if (resultado.isPresent() && resultado.get() == javafx.scene.control.ButtonType.OK) {

            ProductoDAO.eliminar(productoseleccionado.getidProducto());
            // Elimina físicamente el ítem de la lista de datos
            listaProductos.remove(productoseleccionado);

            // 3. Remover el producto de la lista observable global
            tablaProductos.getSelectionModel().clearSelection();


            // 4. Resetear la variable de control
            this.productoseleccionado = null;
            System.out.println("¡Producto eliminado con éxito!");

        } else {
            System.out.println("Eliminación cancelada por el usuario.");
        }

    }

    public void clickSalir(ActionEvent event) {
        // Obtiene la ventana (Stage) actual a partir de cualquier componente de la pantalla
        javafx.stage.Stage stage = (javafx.stage.Stage) botonSalir.getScene().getWindow();

        // Cierra la ventana actual
        stage.close();
    }

    public void setProveedorSelec(controladorProveedorSelec proveedor) {
        this.proveedorSelec = proveedor;
    }
}
