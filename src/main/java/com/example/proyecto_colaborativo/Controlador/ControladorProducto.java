package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.BuscadorUtils;
import com.example.proyecto_colaborativo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorProducto {



    // 1. Inyectamos la TableView apuntando a la clase Producto
    @FXML
    private TableView<Producto> tablaProductos;
    // 2. Inyectamos las columnas existentes de tu FXML
    @FXML
    private TableColumn<Producto, String> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
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
    private TextField codigoBarras;
    @FXML
    private Button botonSalir;
    @FXML
    private TextField porcentaje;
    @FXML
    private TextField precioCosto;


    // Lista observable que contendrá los productos reales
    // Lista observable única para toda la clase
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    //@FXML


    // VARIABLE NUEVA: Guarda el objeto seleccionado para poder modificarlo después
    private Producto productoseleccionado;


    public void initialize(){
    // 3. Vinculamos cada columna con el nombre exacto de la propiedad en la clase Producto

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoTabla"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        listaProductos.addAll(
                new Producto("Arroz 1kg", 50, 1200.50, "PROD001"),
                new Producto("Leche Entera", 30, 950.00, "PROD002"),
                new Producto("Aceite Girasol", 15, 2500.00, "PROD003")
        );

        // ==========================================
        // LLAMADA A LA CLASE REUTILIZABLE
        // ==========================================

        BuscadorUtils.configuradorBuscador(
                txtbuscadorProductos,
                tablaProductos,
                listaProductos,
                (producto,texto)->{
                    // Acá definís la lógica específica para la clase Producto
                    return producto.getNombre().toLowerCase().contains(texto) ||
                            producto.getCodigoTabla().toLowerCase().contains(texto);

                }
        );

        // 4. Cargar los datos en la tabla
      // tablaProductos.setItems(listaProductos);


        // Escuchar cuando el usuario selecciona una fila de la tabla
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // ASIGNACIÓN: Guardamos la referencia del producto seleccionado
                this.productoseleccionado = newValue;

                // 'newValue' contiene el objeto Producto seleccionado
                System.out.println("Seleccionaste: " + newValue.getNombre());
                // Ejemplo: Llenar tus campos de texto automáticamente con los datos del producto
                codigo.setText(newValue.getCodigoTabla());
                nombre.setText(newValue.getNombre());
                cantidad.setText(String.valueOf(newValue.getCantidad()));
                precioFinal.setText(String.valueOf(newValue.getPrecio()));
            }

        });
    }
    // MÉTODO NUEVO: Se ejecuta al presionar el botón Modificar
    @FXML
    private void clickModificar(ActionEvent event) {
        // 1. Validar que el usuario haya seleccionado una fila previamente
        if (productoseleccionado == null) {
            mostrarAlerta("Sin selección", "No se seleccionó ningún producto", "Debes seleccionar un producto de la tabla para poder modificarlo.", Alert.AlertType.WARNING);
            //System.out.println("Error: Debes seleccionar un producto de la tabla.");
            return;

        }

        try {
            // 2. Tomar los nuevos valores directamente desde los TextField
            Integer nuevacantidad = Integer.valueOf(cantidad.getText());
            Double nuevoPrecio = Double.parseDouble(precioFinal.getText());
            String nuevacodigo = codigo.getText();
            String nuevonombre = nombre.getText();

            // VALIDACIÓN: Controlar que cantidad y precio no sean negativos
            if (nuevacantidad < 0 || nuevacantidad <0) {
                mostrarAlerta("Valores inválidos", "Números negativos detectados",
                        "La cantidad y el precio final no pueden ser números negativos. Por favor, ingresá valores mayores o iguales a cero.",javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }


            // 3. Modificar las propiedades del objeto observable
            // Al usar .set(), JavaFX avisa automáticamente a la TableView y se refresca sola
            productoseleccionado.codigoTablaProperty().set(String.valueOf(nuevacodigo));
            productoseleccionado.nombreProperty().set(nuevonombre);
            productoseleccionado.cantidadProperty().set(nuevacantidad);
            productoseleccionado.precioProperty().set(nuevoPrecio);


            // 4. Refrescar la tabla para asegurar que los cambios visuales se apliquen
            tablaProductos.refresh();

            // 5. Limpiar los campos y la selección de la tabla para el siguiente producto
            tablaProductos.getSelectionModel().clearSelection();
            limpiarCampos();
            this.productoseleccionado = null;

            mostrarAlerta("Éxito", "Producto modificado", "El producto se modificó correctamente en la tabla.", Alert.AlertType.INFORMATION);
           // System.out.println("¡Producto modificado con éxito en la tabla!");

        } catch (NumberFormatException e) {
            // Alerta si el usuario ingresó letras o formatos incorrectos
            mostrarAlerta("Error de formato", "Datos numéricos inválidos",
                    "Por favor, verifica los campos:\n" +
                            "- Cantidad: Debe ser un número entero (ej: 10, 50).\n" +
                            "- Precio: Debe ser un número decimal válido (ej: 1200.50). Usa el punto para los decimales.", Alert.AlertType.ERROR);
        //System.out.println("Error: El precio ingresado no es un número válido.");
        }

    }



    @FXML
    private void clickAgregar(ActionEvent event) {
        try {
            // 1. Validar que los campos no estén vacíos
            if (codigo.getText().isEmpty() || nombre.getText().isEmpty() ||
                    cantidad.getText().isEmpty() || precioFinal.getText().isEmpty()) {
                mostrarAlerta("Campos vacíos", "Faltan datos", "Debes completar todos los campos para agregar un producto.",Alert.AlertType.WARNING);
                //System.out.println("Error: Debes ingresar un valor.");
                return;
            }

            // 2. Extraer los datos de las cajas de texto
            String nuevocodigo = codigo.getText();
            String nuevonombre = nombre.getText();
            Integer nuevacantidad = Integer.parseInt(cantidad.getText());
            Double nuevoPrecio = Double.parseDouble(precioFinal.getText());

            // VALIDACIÓN: Controlar que cantidad y precio no sean negativos
            if (nuevacantidad < 0 || nuevoPrecio < 0){
                mostrarAlerta("Valores inválidos", "Números negativos detectados",
                        "La cantidad y el precio final no pueden ser números negativos. Por favor, ingresá valores mayores o iguales a cero.",javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }


            // 3. Crear la nueva instancia de Producto
            Producto nuevoProducto = new Producto( nuevonombre, nuevacantidad, nuevoPrecio,nuevocodigo);

            // 4. Añadirlo a la lista global. La tabla se actualiza de inmediato de forma automática.
            listaProductos.add(nuevoProducto);

            // 5. Limpiar los componentes de la interfaz
            limpiarCampos();
            mostrarAlerta("Éxito", "Producto agregado", "El producto se añadió correctamente a la lista.",Alert.AlertType.INFORMATION);
            //System.out.println("Error: El precio ingresado no es un número válido.");

        } catch (NumberFormatException e) {
            // Alerta si el usuario ingresó letras o formatos incorrectos
            mostrarAlerta("Error de formato", "Datos numéricos inválidos",
                    "Por favor, verifica los campos:\n" +
                            "- Cantidad: Debe ser un número entero (ej: 10, 50).\n" +
                            "- Precio: Debe ser un número decimal válido (ej: 1200.50). Usa el punto para los decimales.",Alert.AlertType.ERROR);
            //System.out.println("Error: El precio ingresado no es un número válido.");
        }

    }

    //----------------------------------------------------------------------------------------
    // NUEVA FUNCIÓN AUXILIAR PARA GENERAR ALERTAS REUTILIZABLES
    //----------------------------------------------------------------------------------------
    private void mostrarAlerta(String titulo, String encabezado, String contenido, javafx.scene.control.Alert.AlertType tipo) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(tipo);
        alerta.setTitle("Aviso del Sistema");
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


    //----------------------------------------------------------------------------------------
    //FUNCIONES
    //----------------------------------------------------------------------------------------
    // Método auxiliar para limpiar las cajas de texto
    private void limpiarCampos() {
        codigo.setText("");
        codigoBarras.setText("");
        nombre.setText("");
        cantidad.setText("");
        precioFinal.setText("");
    }


    public void clickEliminar(ActionEvent event) {
        // 1. Validar que el usuario haya seleccionado un producto de la tabla
        if (productoseleccionado == null){
           mostrarAlerta("Error", "Producto no Seleccionado", "Debes seleccionar un producto de la tabla para eliminarlo.",Alert.AlertType.INFORMATION);
            //System.out.println("Error: Debes seleccionar un producto de la tabla para eliminarlo.");
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
