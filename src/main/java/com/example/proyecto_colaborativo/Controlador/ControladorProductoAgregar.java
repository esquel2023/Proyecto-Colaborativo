package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class ControladorProductoAgregar {

    @FXML private AnchorPane productoActiva;
    @FXML private TextField nombre;
    @FXML private TextField codigoBarras;
    @FXML private TextField cantidad;
    @FXML private TextField precioFinal;
    @FXML private TextField precioCosto;
    @FXML private CheckBox checkProductoActivado;

    private Producto productoLocal = null;

    @FXML
    public void initialize() {
        if (Producto.productoSeleccionadoParaEditar != null) {
            this.productoLocal = Producto.productoSeleccionadoParaEditar;

            nombre.setText(productoLocal.getNombre());
            cantidad.setText(String.valueOf(productoLocal.getCantidad()));
            precioFinal.setText(String.valueOf(productoLocal.getPrecio()));
            if (codigoBarras != null) {
                codigoBarras.setText(productoLocal.getCodigoBarra());
            }

            // MODO EDICIÓN: Marcamos o desmarcamos el CheckBox según el estado real del producto
            if (checkProductoActivado != null) {
                checkProductoActivado.setSelected(productoLocal.isActivado());
            }
            // NO se limpia el puente aquí para proteger la seguridad de la transacción
        }else{
            // MODO AGREGAR: Por defecto, un producto nuevo suele nacer activado
            if (checkProductoActivado != null) {
                checkProductoActivado.setSelected(true);
            }
        }

        // Expresión regular para restringir la escritura a enteros positivos
        cantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cantidad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Expresión regular para restringir la escritura a decimales válidos
        precioFinal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                precioFinal.setText(oldValue);
            }
        });
    }

    @FXML
    private void Clickguardar(ActionEvent event) {
        String textoNombre = nombre.getText().trim();
        String textoCantidad = cantidad.getText().trim();
        String textoPrecio = precioFinal.getText().trim();
        String nuevocodigo = (codigoBarras != null) ? codigoBarras.getText().trim() : "";

        // CAPTURA: Obtenemos si el CheckBox está marcado (true) o desmarcado (false)
        boolean estaActivado = (checkProductoActivado != null) && checkProductoActivado.isSelected();

        if (textoNombre.isEmpty() || textoCantidad.isEmpty() || textoPrecio.isEmpty()) {
            AlertasUtils.mostrarAdvertencia("Campos vacíos", "Por favor, completa todos los campos.");
            return;
        }

        try {
            int nuevacantidad = Integer.parseInt(textoCantidad);
            double nuevoPrecio = Double.parseDouble(textoPrecio);

            if (nuevacantidad < 0 || nuevoPrecio < 0) {
                AlertasUtils.mostrarError("Valores inválidos", "La cantidad y el precio no pueden ser negativos.");
                return;
            }

            if (this.productoLocal != null) {
                // === OPERACIÓN MODIFICACIÓN ===

                // Respaldo de los datos originales por si la base de datos aborta
                String nombreViejo = productoLocal.getNombre();
                int cantidadVieja = productoLocal.getCantidad();
                double precioViejo = productoLocal.getPrecio();
                String codigoViejo = productoLocal.getCodigoBarra();
                boolean activadoViejo = productoLocal.isActivado();

                // Modificación reactiva en memoria
                productoLocal.setNombre(textoNombre);
                productoLocal.setCantidad(nuevacantidad);
                productoLocal.setPrecio(nuevoPrecio);
                productoLocal.setCodigoBarra(nuevocodigo);
                productoLocal.setActivado(estaActivado);

                try {
                    ProductoDAO.actualizar(productoLocal);

                    // Ahora que la base de datos dio el visto bueno, liberamos el puente con seguridad
                    Producto.productoSeleccionadoParaEditar = null;
                    AlertasUtils.mostrarInformacion("Éxito", "Producto modificado correctamente.");
                } catch (SQLException e) {
                    // Si el servidor falla, devolvemos el objeto de la TableView a su estado real
                    productoLocal.setNombre(nombreViejo);
                    productoLocal.setCantidad(cantidadVieja);
                    productoLocal.setPrecio(precioViejo);
                    productoLocal.setCodigoBarra(codigoViejo);
                    productoLocal.setActivado(activadoViejo);
                    throw e; // Lanza al catch externo para alertar
                }

            } else {
                // === OPERACIÓN REGISTRO NUEVO ===
                Producto nuevoProducto = new Producto(textoNombre, nuevacantidad, nuevoPrecio, nuevocodigo, estaActivado);
                ProductoDAO.insertar(nuevoProducto);
                AlertasUtils.mostrarInformacion("Éxito", "El producto se registró correctamente.");
            }

            cerrarVentana();

        } catch (NumberFormatException e) {
            AlertasUtils.mostrarError("Error de formato", "Revisa que los campos numéricos contengan un formato correcto.");
        } catch (SQLException e) {
            AlertasUtils.mostrarError("Error de BD", "No se pudo guardar la información en la base de datos.");
            e.printStackTrace();
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) nombre.getScene().getWindow();
        stage.close();
    }
}
