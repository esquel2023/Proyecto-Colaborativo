package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ControladorProducto {



    // 1. Inyectamos la TableView apuntando a la clase Producto
    @FXML
    private TableView<Producto> tablaProductos;
    // 2. Inyectamos las columnas existentes de tu FXML
    @FXML
    private TableColumn<Producto, Integer> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TextField codigoBarras;
    @FXML
    private TextField codigo;
    @FXML
    private TextField nombre;
    @FXML
    private TextField cantidad;
    @FXML
    private TextField precioFinal;
    @FXML
    private TextField txtbuscadorProductos;
    @FXML
    private Button botonSalir;
    @FXML
    private TextField porcentaje;
    @FXML
    private TextField precioCosto;


    // Lista observable que contendrá los productos reales
    // Lista observable única para toda la clase
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // VARIABLE NUEVA: Guarda el objeto seleccionado para poder modificarlo después
    private Producto productoseleccionado;

    // >>> NUEVA VARIABLE GLOBAL CONECTADA A TU FACTURA <<<
    private ControladorFactura controladorFactura;

    // >>> NUEVO MÉTODO QUE SE LLAMA DESDE CONTROLADORFACTURA <<<
    public void setControladorProducto(ControladorFactura factura) {
        this.controladorFactura = factura;
    }


    public void initialize(){
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
                (producto,texto)->{
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
                this.productoseleccionado = newValue;

                // 'newValue' contiene el objeto Producto seleccionado
                System.out.println("Seleccionaste: " + newValue.getNombre());
                // Ejemplo: Llenar tus campos de texto automáticamente con los datos del producto
                //codigo.setText(newValue.getCodigoBarra());

                //codigo.setText(String.valueOf(newValue.getidProducto()));
                nombre.setText(newValue.getNombre());
                cantidad.setText(String.valueOf(newValue.getCantidad()));
                precioFinal.setText(String.valueOf(newValue.getPrecio()));

                if (codigoBarras != null) {
                    codigoBarras.setText(newValue.getCodigoBarra() != null ? newValue.getCodigoBarra() : "");
                }
            }

        });
        tablaProductos.setRowFactory(tv -> {
            TableRow<Producto> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                // Si hace doble clic y la fila contiene un Producto válido
                if (event.getClickCount() == 2 && (!fila.isEmpty())) {
                    Producto seleccionado = fila.getItem();

                    // Si este catálogo fue abierto por la factura (controladorFactura no es null)
                    if (seleccionado != null && controladorFactura != null) {
                        // Enviamos el producto a la tabla de la factura
                        controladorFactura.recibirProducto(seleccionado);

                        // Cerramos automáticamente esta ventana de catálogo
                        Stage stage = (Stage) tablaProductos.getScene().getWindow();
                        stage.close();
                    }
                }
            });
            return fila;
        });
    }
    // MÉTODO NUEVO: Se ejecuta al presionar el botón Modificar
    @FXML
    private void clickModificar(ActionEvent event) {
        // LLAMADA A LA CLASE UTILITARIA
        // 1. Validar que el usuario haya seleccionado una fila previamente
        if (productoseleccionado == null) {
            AlertasUtils.mostrarAlerta("Sin selección", "No se seleccionó ningún producto",
                    "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);

            return;
        }

       try {
            // 2. Tomar los nuevos valores directamente desde los TextField
            Integer nuevacantidad = Integer.valueOf(cantidad.getText());
            Double nuevoPrecio = Double.parseDouble(precioFinal.getText());
            //String nuevacodigo = codigo.getText();
            String nuevonombre = nombre.getText();
           String nuevocodigo = (codigoBarras != null) ? codigoBarras.getText() : "";

            // VALIDACIÓN: Controlar que cantidad y precio no sean negativos
            if (nuevacantidad <= 0 || nuevoPrecio <=0) {
                AlertasUtils.mostrarAlerta("Valores inválidos", "Números negativos detectados",
                        "La cantidad y el precio final no pueden ser números negativos. Por favor, ingresá valores mayores o iguales a cero.",javafx.scene.control.Alert.AlertType.ERROR);

                return;
            }
            // 3. Modificar las propiedades del objeto observable
            // Al usar .set(), JavaFX avisa automáticamente a la TableView y se refresca sola
            //productoseleccionado.codigoTablaProperty().set(String.valueOf(nuevacodigo));
            //productoseleccionado.codigoBarraProperty().set(nuevacodigo);
            //productoseleccionado.setidProducto(nuevaidProducto);
            productoseleccionado.nombreProperty().set(nuevonombre);
            productoseleccionado.setNombre(nuevonombre);
            productoseleccionado.cantidadProperty().set(nuevacantidad);
            productoseleccionado.setCantidad(nuevacantidad);
            productoseleccionado.precioProperty().set(nuevoPrecio);
            productoseleccionado.setPrecio(nuevoPrecio);

            // 2. Persistir el cambio en la Base de Datos
            ProductoDAO.actualizar(productoseleccionado);


            // 4. Refrescar la tabla para asegurar que los cambios visuales se apliquen
            tablaProductos.refresh();

            // 5. Limpiar los campos y la selección de la tabla para el siguiente producto
            tablaProductos.getSelectionModel().clearSelection();
            limpiarCampos();
            this.productoseleccionado = null;
            AlertasUtils.mostrarAlerta("Éxito", "Producto modificado", "El producto se modificó correctamente en la tabla.", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            // Alerta si el usuario ingresó letras o formatos incorrectos
            AlertasUtils.mostrarAlerta("Error de formato", "Datos numéricos inválidos",
                    "Por favor, verifica los campos:\n" +
                            "- Cantidad: Debe ser un número entero (ej: 10, 50).\n" +
                            "- Precio: Debe ser un número decimal válido (ej: 1200.50). Usa el punto para los decimales.", Alert.AlertType.ERROR);

        } catch (SQLException e) {
           // throw new RuntimeException(e);
            AlertasUtils.mostrarAlerta("Error de BD", "Error al actualizar", "No se pudo guardar en la base de datos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }



    @FXML
    private void clickAgregar(ActionEvent event) {
        try {
            // 1. Validar que los campos no estén vacíos
            if (nombre.getText().isEmpty() ||
                    cantidad.getText().isEmpty() || precioFinal.getText().isEmpty()) {
                AlertasUtils.mostrarAlerta("Campos vacíos", "Faltan datos", "Debes completar todos los campos para agregar un producto.",Alert.AlertType.WARNING);

                return;
            }

            // 2. Extraer los datos de las cajas de texto
           // String nuevocodigo = codigo.getText();
            String nuevonombre = nombre.getText();
            Integer nuevacantidad = Integer.parseInt(cantidad.getText());
            Double nuevoPrecio = Double.parseDouble(precioFinal.getText());
            String nuevocodigoBarra = codigoBarras.getText();

            // VALIDACIÓN: Controlar que cantidad y precio no sean negativos
            if (nuevacantidad <= 0 || nuevoPrecio <= 0){
                AlertasUtils.mostrarAlerta("Valores inválidos Números negativos detectados",
                        "La cantidad y el precio final no pueden ser números negativos", "Por favor ingresá valores mayores o iguales a cero.",Alert.AlertType.ERROR);

                return;
            }


            // 3. Crear la nueva instancia de Producto
            Producto nuevoProducto = new Producto(nuevonombre, nuevacantidad, nuevoPrecio,nuevocodigoBarra);
            ProductoDAO.insertar(nuevoProducto);
            // 4. Añadirlo a la lista global. La tabla se actualiza de inmediato de forma automática.
            listaProductos.add(nuevoProducto);

            // 4. Guardar en la Base de Datos primero
            //ProductoDAO.insertar(nuevoProducto);

            // 5. Refrescar la interfaz gráfica
            cargarDatosDesdeBD();
            limpiarCampos();
            AlertasUtils.mostrarAlerta("Éxito", "Producto agregado", "El producto se añadió correctamente a la lista.",Alert.AlertType.INFORMATION);


        } catch (NumberFormatException e) {
            // Alerta si el usuario ingresó letras o formatos incorrectos
            AlertasUtils.mostrarAlerta("Error de formato", "Datos numéricos inválidos",
                    "Por favor, verifica los campos:\n" +
                            "- Cantidad: Debe ser un número entero (ej: 10, 50).\n" +
                            "- Precio: Debe ser un número decimal válido (ej: 1200.50). Usa el punto para los decimales.",Alert.AlertType.ERROR);

        }

    }

    private void cargarDatosDesdeBD() {

        try {
            listaProductos.clear();
            //listaProductos.addAll(ProductoDAO.listar());
            listaProductos.setAll(ProductoDAO.listar());
            //tablaProductos.setItems(listaProductos);
        }catch (Exception e) {
            AlertasUtils.mostrarAlerta("Error de BD", "Error de lectura",
                    "No se pudieron recuperar los productos de la base de datos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    //----------------------------------------------------------------------------------------
    //FUNCIONES
    //----------------------------------------------------------------------------------------
    // Método auxiliar para limpiar las cajas de texto
    private void limpiarCampos() {
        nombre.clear();
        cantidad.clear();
        precioFinal.clear();
        if (codigoBarras != null) codigoBarras.clear();
        if (codigo != null) codigo.clear();
        if (porcentaje != null) porcentaje.clear();
        if (precioCosto != null) precioCosto.clear();
    }


    public void clickEliminar(ActionEvent event) throws SQLException {
        // 1. Validar que el usuario haya seleccionado un producto de la tabla
        if (productoseleccionado == null){
           AlertasUtils.mostrarAlerta("Error", "Producto no Seleccionado", "Debes seleccionar un producto de la tabla para eliminarlo.",Alert.AlertType.INFORMATION);

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
            limpiarCampos();

            // 4. Resetear la variable de control
            this.productoseleccionado = null;
            System.out.println("¡Producto eliminado con éxito!");

        }else {
            System.out.println("Eliminación cancelada por el usuario.");
        }

    }

    public void clickSalir(ActionEvent event) {
        // Obtiene la ventana (Stage) actual a partir de cualquier componente de la pantalla
        javafx.stage.Stage stage = (javafx.stage.Stage) botonSalir.getScene().getWindow();

        // Cierra la ventana actual
        stage.close();
    }
    private void calcularPrecioFinal() {
        String textcosto = precioFinal.getText().trim();
        String textporcentaje = porcentaje.getText().trim();

        if(textcosto.isEmpty() || textporcentaje.isEmpty()){
            return;  // Esperamos a que ambos campos tengan datos
        }

        try{
            // Reemplazamos comas por puntos por si el usuario escribe con coma decimal
            double costo = Double.parseDouble(textcosto.replace(",", "."));
            double porcentaje = Double.parseDouble(textporcentaje.replace(",", "."));

            if(costo > 0 || porcentaje > 0){
                double resultado = costo * (1 + (porcentaje / 100));
                // Formateamos a dos decimales para que quede prolijo
                precioFinal.setText(String.format(java.util.Locale.US, "%.2fi", resultado).replace("i", ""));
            }
        } catch (NumberFormatException e) {
            // No hacemos nada para no interrumpir al usuario mientras escribe valores intermedios
        }

    }
}
