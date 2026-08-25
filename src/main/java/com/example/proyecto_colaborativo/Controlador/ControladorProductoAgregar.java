package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

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

    // URL base de tu API de Spring Boot (ajustá la ruta si cambia tu controlador REST)
    private static final String API_URL = "http://localhost:8080/tienda/api/v1/productos/fake-productos";

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

            Producto.productoSeleccionadoParaEditar = null;
        }

        cantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cantidad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

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

        if (textoNombre.isEmpty() || textoCantidad.isEmpty() || textoPrecio.isEmpty()) {
            AlertasUtils.mostrarAdvertencia("Campos vacíos", "Nombre, cantidad y precio son requeridos.");
            return;
        }

        try {
            Integer nuevacantidad = Integer.valueOf(textoCantidad);
            Double nuevoPrecio = Double.parseDouble(textoPrecio);

            if (nuevacantidad < 0 || nuevoPrecio < 0) {
                AlertasUtils.mostrarError("Valores inválidos", "La cantidad y el precio no pueden ser negativos.");
                return;
            }

            if (this.productoLocal != null) {
                // === LÓGICA DE MODIFICACIÓN (PUT) ===
                productoLocal.setNombre(textoNombre);
                productoLocal.setCantidad(nuevacantidad);
                productoLocal.setPrecio(nuevoPrecio);
                if (codigoBarras != null) {
                    productoLocal.setCodigoBarra(nuevocodigo);
                }

                ejecutarPeticionHttp("PUT", API_URL + "/" + productoLocal.getidProducto(), textoNombre, nuevacantidad, nuevoPrecio, nuevocodigo);

            } else {
                // === LÓGICA DE AGREGAR NUEVO (POST) ===
                ejecutarPeticionHttp("POST", API_URL, textoNombre, nuevacantidad, nuevoPrecio, nuevocodigo);
            }

        } catch (NumberFormatException e) {
            AlertasUtils.mostrarError("Error de formato", "Verificá los campos numéricos. Usá el punto para los decimales.");
        }
    }

    // Método auxiliar para realizar la petición HTTP (POST o PUT) en segundo plano
    private void ejecutarPeticionHttp(String metodo, String url, String nombre, int stock, double precio, String codigo) {
        // 1. Armamos un mapa limpio con las claves exactas que espera recibir tu backend de Spring Boot
        Map<String, Object> productoMap = new HashMap<>();
        productoMap.put("nombre", nombre);
        productoMap.put("cantidad", stock);
        productoMap.put("precio", precio);
        productoMap.put("codigoBarra", codigo);

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(productoMap);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json");

                if (metodo.equalsIgnoreCase("POST")) {
                    builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
                } else {
                    builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
                }

                HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                return response.statusCode();
            }
        };

        // Si la API responde exitosamente (Código 200 o 201)
        task.setOnSucceeded(e -> {
            int statusCode = task.getValue();
            if (statusCode == 200 || statusCode == 201) {
                String accion = metodo.equalsIgnoreCase("POST") ? "agregó" : "modificó";
                AlertasUtils.mostrarInformacion("Éxito", "Operación exitosa. El producto se " + accion + " correctamente en el servidor.");
                cerrarVentana();
            } else {
                AlertasUtils.mostrarError("Error de Servidor", "El backend rechazó la solicitud. Código: " + statusCode);
            }
        });

        // Si el servidor de Spring Boot está apagado o falla la red
        task.setOnFailed(e -> {
            Throwable error = task.getException();
            System.err.println("Error HTTP: " + error.getMessage());
            AlertasUtils.mostrarError("Error de conexión", "No se pudo conectar con el servidor de Spring Boot.");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) nombre.getScene().getWindow();
        stage.close();
    }
}
