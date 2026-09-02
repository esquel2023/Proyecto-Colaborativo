package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.util.List;



public class controladorProveedorSelec {

    private final java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    // Ajusta esta URL base según los endpoints reales de tu backend para ProductoProveedor
    private final String API_PRODUCTO_PROVEEDOR = "http://localhost:8080/tienda/api/v1/producto-proveedor";

    private static controladorProveedorSelec instanciaActiva;

    @FXML public TableView<Producto> tablaProductosProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor;
    @FXML public TableColumn<Producto, Double> precioProovedor;
    @FXML public TableColumn<Producto, String> prooductosProovedor1;

    @FXML public TextField nombreProveedor;
    @FXML public TextField cuitProveedor;
    @FXML public TextField correoProveedor;
    @FXML public TextField telefonoProveedor;
    @FXML public TextField paisProveedor;
    @FXML public TextField provinciaProveedor;
    @FXML public TextField localidadProveedor;
    @FXML public TextField txtBuscar;

    @FXML private Label proveedorSelec;

    private final ObservableList<Producto> listaProductosObs = FXCollections.observableArrayList();
    private proovedorClase proveedorActual;
    private Producto productoseleccionado;

    @FXML
    public void initialize() {
        instanciaActiva = this;

        // 1. Configuración segura de la estructura de la tabla al arrancar
        if (prooductosProovedor != null && precioProovedor != null && prooductosProovedor1 != null) {
            prooductosProovedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            precioProovedor.setCellValueFactory(new PropertyValueFactory<>("precio"));
            prooductosProovedor1.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

            tablaProductosProovedor.setItems(listaProductosObs);

            tablaProductosProovedor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                this.productoseleccionado = newValue;
                configurarTablaEditable();
            });
        }
    }
    public void setProveedorActual(proovedorClase proveedor) {
        if (proveedor == null) return;

        this.proveedorActual = proveedor;

        if (this.proveedorSelec != null) {
            this.proveedorSelec.setText(proveedor.getNombreEntidad());
        }

        if (nombreProveedor != null) nombreProveedor.setText(proveedor.getNombreEntidad());
        if (cuitProveedor != null) cuitProveedor.setText(proveedor.getCuitcuilEntidad());
        if (correoProveedor != null) correoProveedor.setText(proveedor.getEmailEntidad());
        if (telefonoProveedor != null) telefonoProveedor.setText(proveedor.getTelefonoEntidad());
        if (paisProveedor != null) paisProveedor.setText(proveedor.getPais());
        if (provinciaProveedor != null) provinciaProveedor.setText(proveedor.getProvincia());
        if (localidadProveedor != null) localidadProveedor.setText(proveedor.getCiudad());


        actualizarTabla(Math.toIntExact(proveedor.getId()));
    }

    // Mantiene compatibilidad con tu llamado alternativo anterior
    public static void setProveedorSelec(proovedorClase proveedor) {
        if (instanciaActiva != null && proveedor != null) {
            instanciaActiva.setProveedorActual(proveedor);
        }
    }



    private void configurarTablaEditable() {
        tablaProductosProovedor.setEditable(true);
        precioProovedor.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        precioProovedor.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Double nuevoPrecioCosto = event.getNewValue();

            if (nuevoPrecioCosto != null && nuevoPrecioCosto >= 0 && p != null && proveedorActual != null) {
                p.setPrecio(nuevoPrecioCosto);
                p.precioProperty().set(nuevoPrecioCosto);

            } else {
                tablaProductosProovedor.refresh();
            }
        });
    }

    public void recibirProducto(Producto producto) {
        if (producto == null || proveedorActual == null) return;

        Thread postThread = new Thread(() -> {
            try {
                // Creamos un DTO o mapa con los datos requeridos por la tabla relacional
                java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
                requestBody.put("idProducto", producto.getidProducto());
                requestBody.put("idProveedor", proveedorActual.getId());
                requestBody.put("precioCosto", 0.0); // Valor inicial predeterminado

                String json = objectMapper.writeValueAsString(requestBody);

                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(API_PRODUCTO_PROVEEDOR))
                        .header("Content-Type", "application/json")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                        .build();

                java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201 || response.statusCode() == 200) {
                    System.out.println("[INFO] Asociación creada en API con éxito.");
                    javafx.application.Platform.runLater(() -> {
                        listaProductosObs.add(producto);
                        tablaProductosProovedor.refresh();
                    });
                } else {
                    System.out.println("[ERROR] No se pudo asociar en la API. Código: " + response.statusCode());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico al asociar producto.");
                e.printStackTrace();
            }
        });
        postThread.setDaemon(true);
        postThread.start();
    }

    // 4. DELETE - Reemplaza tu método desasociarProducto(Producto producto)
    public void desasociarProducto(Producto producto) {
        if (producto == null || proveedorActual == null) return;

        Thread deleteThread = new Thread(() -> {
            try {
                // Construimos la URL pasando ambas llaves si es una relación compuesta (ej: /producto/X/proveedor/Y)
                String urlDelete = API_PRODUCTO_PROVEEDOR + "/producto/" + producto.getidProducto() + "/proveedor/" + proveedorActual.getId();

                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(urlDelete))
                        .DELETE()
                        .build();

                java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 204 || response.statusCode() == 200) {
                    System.out.println("[INFO] Desasociación exitosa en la API.");
                    javafx.application.Platform.runLater(() -> {
                        listaProductosObs.remove(producto);
                    });
                } else {
                    System.out.println("[ERROR] No se pudo desasociar en la API. Código: " + response.statusCode());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico al desasociar producto.");
                e.printStackTrace();
            }
        });
        deleteThread.setDaemon(true);
        deleteThread.start();
    }

    @FXML
    public void botonAgregarProducto(ActionEvent actionEvent) {
        if (proveedorActual == null) return;


        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Parent root = loader.load();
            ControladorProducto controller = loader.getController();
            controller.setControladorProveedorSelec(this);

            Stage stage = new Stage();
            stage.setTitle("Buscador de Productos");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void botonModificarProveedor(ActionEvent actionEvent) {}
    @FXML public void botonEliminarProveedor(ActionEvent actionEvent) {}
    @FXML public void buscarProducto(ActionEvent actionEvent) {}
    @FXML public void botonModificarProducto(ActionEvent actionEvent) {}

    @FXML
    public void botonEliminarProducto(ActionEvent actionEvent) {
        if (productoseleccionado != null) {
            desasociarProducto(productoseleccionado);
        }
    }
    private void actualizarPrecioEnApi(int idProducto, long idProveedor, double nuevoPrecio) {
        Thread putThread = new Thread(() -> {
            try {
                java.util.Map<String, Object> requestBody = new java.util.HashMap<>();
                requestBody.put("idProducto", idProducto);
                requestBody.put("idProveedor", idProveedor);
                requestBody.put("precioCosto", nuevoPrecio);

                String json = objectMapper.writeValueAsString(requestBody);

                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(API_PRODUCTO_PROVEEDOR))
                        .header("Content-Type", "application/json")
                        .PUT(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                        .build();

                java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    System.out.println("[INFO] Precio actualizado en API con éxito.");
                } else {
                    System.out.println("[ERROR] No se pudo actualizar el precio. Código: " + response.statusCode());
                    javafx.application.Platform.runLater(() -> tablaProductosProovedor.refresh());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Error crítico al actualizar precio.");
                e.printStackTrace();
            }
        });
        putThread.setDaemon(true);
        putThread.start();
    }
    private void actualizarTabla(int idProveedor) {
        Thread apiThread = new Thread(() -> {
            try {
                java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                        .uri(java.net.URI.create(API_PRODUCTO_PROVEEDOR + "/proveedor/" + idProveedor))
                        .GET()
                        .build();

                java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    // Mapea el JSON recibido a un array de tu clase Producto
                    Producto[] productosArray = objectMapper.readValue(response.body(), Producto[].class);

                    javafx.application.Platform.runLater(() -> {
                        listaProductosObs.setAll(productosArray);
                    });
                } else {
                    System.out.println("[ERROR] No se pudieron obtener productos. Código: " + response.statusCode());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Falló la conexión al listar productos.");
                e.printStackTrace();
            }
        });
    }
}
