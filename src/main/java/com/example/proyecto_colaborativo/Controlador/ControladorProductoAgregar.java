package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import com.example.proyecto_colaborativo.bd.ProductoProveedorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.annotation.Native;
import java.sql.SQLException;

public class ControladorProductoAgregar {
    @FXML
    public TextField precioFinal;
    @FXML
    public TextField codigoBarras;
    @FXML
    public TextField cantidad;
    @FXML
    public TextField nombre;
    public TextField precioCosto;

    private Producto productoLocal = null;


    // === 3. MÉTODO INITIALIZE ===
    @FXML
    public void initialize() {
        // Al abrir la ventana, revisamos el puente estático
        if (Producto.productoSeleccionadoParaEditar != null) {
            // Guardamos el objeto en la variable de clase que declaramos arriba
            this.productoLocal = Producto.productoSeleccionadoParaEditar;

            // Rellenamos los campos
            nombre.setText(productoLocal.getNombre());
            cantidad.setText(String.valueOf(productoLocal.getCantidad()));
            precioFinal.setText(String.valueOf(productoLocal.getPrecio()));
            if (codigoBarras != null) {
                codigoBarras.setText(productoLocal.getCodigoBarra());
            }

            // Limpiamos el puente
            Producto.productoSeleccionadoParaEditar = null;
        }

        // Filtro para Cantidad (Solo permite números enteros positivos)
        cantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cantidad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Filtro para Precio (Solo permite números y un único punto decimal)
        precioFinal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                precioFinal.setText(oldValue);
            }
        });
        precioCosto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                precioCosto.setText(oldValue);
            }
        });

    }

    // === 4. TU BOTÓN GUARDAR ===
    @FXML
    private void Clickguardar(ActionEvent event) {
        // 1. Capturar los valores directamente desde los TextField
        String textoNombre = nombre.getText().trim();
        String textoCantidad = cantidad.getText().trim();
        String textoPrecio = precioFinal.getText().trim();
        String nuevocodigo = (codigoBarras != null) ? codigoBarras.getText().trim() : "";

        // 2. VALIDACIÓN: Validar campos vacíos o números negativos
        if (textoNombre.isEmpty() || textoCantidad.isEmpty() || textoPrecio.isEmpty() ) {
            AlertasUtils.mostrarAlerta("Campos vacíos", "Nombre requerido", "Por favor, ingresá el nombre del producto.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Integer nuevacantidad = Integer.valueOf(textoCantidad);
            Double nuevoPrecio = Double.parseDouble(textoPrecio);

            if (nuevacantidad < 0 || nuevoPrecio < 0) {
                AlertasUtils.mostrarAlerta("Valores inválidos", "Números negativos detectados", "La cantidad y el precio final no pueden ser números negativos.", Alert.AlertType.ERROR);
                return;
            }

            // 2. DIFERENCIAR: ¿Es una modificación o es un producto nuevo?
            if (this.productoLocal != null) {
                // === LÓGICA DE MODIFICACIÓN ===
                productoLocal.nombreProperty().set(textoNombre);
                productoLocal.setNombre(textoNombre);
                productoLocal.cantidadProperty().set(nuevacantidad);
                productoLocal.setCantidad(nuevacantidad);
                productoLocal.precioProperty().set(nuevoPrecio);
                productoLocal.setPrecio(nuevoPrecio);
                if (codigoBarras != null) {
                    productoLocal.setCodigoBarra(nuevocodigo);
                }

                // Persistir el cambio en la Base de Datos
                ProductoDAO.actualizar(productoLocal);
                AlertasUtils.mostrarAlerta("Éxito", "Producto modificado", "El producto se modificó correctamente.", Alert.AlertType.INFORMATION);

            } else {
                // === LÓGICA DE AGREGAR (NUEVO PRODUCTO) ===
                // Creamos una nueva instancia con los datos del formulario
                Producto nuevoProducto = new Producto(textoNombre, nuevacantidad, nuevoPrecio, nuevocodigo);

                // Guardar el nuevo producto en la Base de Datos
                ProductoDAO.insertar(nuevoProducto);

                // =========================================================================
                // ASOCIACIÓN CON EL PROVEEDOR EN LA TABLA INTERMEDIA
                // =========================================================================
                if (Producto.debeAsociarProveedor && Producto.idProveedorParaAsociar != null) {
                    // Rescatamos de forma segura el valor del TextField precioCosto
                    String textoCosto = (precioCosto != null) ? precioCosto.getText().trim() : "";
                    double costo = 0.0;

                    if (!textoCosto.isEmpty()) {
                        costo = Double.parseDouble(textoCosto);
                    }

                    // NOTA CRÍTICA: nuevoProducto.getidProducto() debe retornar el ID autoincremental
                    // que generó SQLite al ejecutar el método ProductoDAO.insertar(nuevoProducto).
                    int idProductoGenerado = nuevoProducto.getidProducto();
                    int idProveedorActual = Producto.idProveedorParaAsociar;

                    // Guardamos la relación y el precio de costo en la tabla intermedia
                    ProductoProveedorDAO.asociar(idProductoGenerado, idProveedorActual, costo);

                    // Limpiamos los puentes estáticos de seguridad
                    Producto.debeAsociarProveedor = false;
                    Producto.idProveedorParaAsociar = null;
                }
                // =========================================================================

                AlertasUtils.mostrarAlerta("Éxito", "Producto agregado", "El nuevo producto se registró correctamente.", Alert.AlertType.INFORMATION);
            }

            // 3. Cerrar la ventana automáticamente al terminar con éxito
            cerrarVentana();

        } catch (NumberFormatException e) {
            AlertasUtils.mostrarAlerta("Error de formato", "Datos numéricos inválidos", "Por favor, verifica los campos:\n" + "- Cantidad: Debe ser un número entero (ej: 10, 50).\n" + "- Precio Final: Debe ser un número decimal válido (ej: 1200.50).\n" + "- Precio Costo: Debe ser un número decimal válido.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            AlertasUtils.mostrarAlerta("Error de BD", "Error al procesar", "No se pudo guardar la información en la base de datos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }



    //Metodo auxiliar para cerrar la ventana actual
    private void cerrarVentana() {
        Stage stage = (Stage) nombre.getScene().getWindow();
        stage.close();
    }


}
