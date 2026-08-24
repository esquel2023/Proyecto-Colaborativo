package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ControladorProductoAgregar {
    @FXML
    public TextField precioFinal;
    @FXML
    public TextField codigoBarras;
    @FXML
    public TextField cantidad;
    @FXML
    public TextField nombre;

    private Producto productoLocal = null;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:8080/tienda/api/v1/productos";


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


    }

    @FXML
    private void Clickguardar(ActionEvent event) {

        // 1. Capturar los valores directamente desde los TextField
        String textoNombre = nombre.getText().trim();
        String textoCantidad = cantidad.getText().trim();
        String textoPrecio = precioFinal.getText().trim();
        String nuevocodigo = (codigoBarras != null) ? codigoBarras.getText().trim() : "";


        // 2. VALIDACIÓN: Validar campos vacíos o números negativos
        if (textoNombre.isEmpty() || textoCantidad.isEmpty() || textoPrecio.isEmpty()) {
            AlertasUtils.mostrarAdvertencia("Campos vacíos", "Nombre requerido. Por favor, ingresá el nombre del producto.");

            return;
        }

        try {

            Integer nuevacantidad = Integer.valueOf(textoCantidad);
            Double nuevoPrecio = Double.parseDouble(textoPrecio);
            //Integer nuevacantidad=Integer.valueOf(cantidad.getText());
            //Double nuevoPrecio = Double.parseDouble(precioFinal.getText());


            if (nuevacantidad < 0 || nuevoPrecio < 0) {
                AlertasUtils.mostrarError("Valores inválidos", "Números negativos detectados.  cantidad y el precio final no pueden ser números negativos.");

                return;
            }

            // 2. DIFERENCIAR: ¿Es una modificación o es un producto nuevo?
            if (this.productoLocal != null) {
                // === LÓGICA DE MODIFICACIÓN ===
                // Modificamos las propiedades del objeto observable (esto refresca la TableView automáticamente)
                productoLocal.nombreProperty().set(textoNombre);
                productoLocal.setNombre(textoNombre);

                productoLocal.cantidadProperty().set(nuevacantidad);
                productoLocal.setCantidad(nuevacantidad);

                productoLocal.precioProperty().set(nuevoPrecio);
                productoLocal.setPrecio(nuevoPrecio);

                if (codigoBarras != null) {

                    productoLocal.setCodigoBarra(nuevocodigo);
                }

                AlertasUtils.mostrarInformacion("Éxito", "Producto modificado. El producto se modificó correctamente.");

            } else {

                Producto nuevoProducto = new Producto(textoNombre, nuevacantidad, nuevoPrecio, nuevocodigo);


                // Opcional: Si manejás una lista observable global para la tabla, podrías agregar aquí:
                // listadoProductosGlobal.add(nuevoProducto);

                AlertasUtils.mostrarInformacion("Éxito", "Producto agregado. El nuevo producto se registró correctamente.");

            }

            // 3. Cerrar la ventana automáticamente al terminar con éxito
            cerrarVentana();

        } catch (NumberFormatException e) {
            AlertasUtils.mostrarError("Error de formato", "Datos numéricos inválidos. Por favor, verifica los campos:\\n\" +\n" +
                    "                            \"- Cantidad: Debe ser un número entero (ej: 10, 50).\\n\" +\n" +
                    "                            \"- Precio: Debe ser un número decimal válido (ej: 1200.50). Usa el punto para los decimales. ");

        }
    }

    //Metodo auxiliar para cerrar la ventana actual
    private void cerrarVentana() {
        Stage stage = (Stage) nombre.getScene().getWindow();
        stage.close();
    }
}



