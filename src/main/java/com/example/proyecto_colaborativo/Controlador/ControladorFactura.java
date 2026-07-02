package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.*;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorFactura implements Initializable {

    public SplitMenuButton tipoFactura;
    public Text unidades;
    public Text total;
    public Label ingresarCodigo;
    public Label totalFinal;
    public Label cliente;
    public Label Total;
    public TextField cantidadUnidades;
    public TextField nombre;
    public TextField nombreYApellido;
    public Button agregarCliente;
    public Button agregarProducto;
    public Button eliminarProducto;
    public SplitMenuButton codigo;

    @FXML
    private Button buscarCliente;

    @FXML
    private TableView<Producto> TablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigoDeBarra;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Integer> colCantidad;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Double> colSubtotal;

    private final ObservableList<Producto> listaUsuarios = FXCollections.observableArrayList();

    private final ProductoDAO usuarioDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarColumnas();
        obtenerProducto();
        configurarTablaEditable();
        Calcular();
        TablaProductos.setItems(listaUsuarios);
    }

    private void configurarTablaEditable() {
        TablaProductos.setEditable(true);

        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCantidad.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Integer nuevaCantidad = event.getNewValue();
            if (nuevaCantidad != null && nuevaCantidad > 0) {
                p.setCantidad(nuevaCantidad);
                p.cantidadProperty().set(nuevaCantidad);
                Calcular();
                TablaProductos.refresh();
            } else {
                TablaProductos.refresh();
            }
        });

        colPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrecio.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Double nuevoPrecio = event.getNewValue();
            if (nuevoPrecio != null && nuevoPrecio > 0) {
                p.setPrecio(nuevoPrecio);
                p.precioProperty().set(nuevoPrecio);
                Calcular();
            } else {
                TablaProductos.refresh();
            }
        });
    }

    private void Calcular() {
        double totalAcumulado = 0.0;

        for (Producto p : listaUsuarios) {
            totalAcumulado += (p.getPrecio() * p.getCantidad());
        }

        String totalFormateado = String.format("$ %.2f", totalAcumulado);

        if (totalFinal != null) {
            totalFinal.setText(totalFormateado);
        }
        if (Total != null) {
            Total.setText(totalFormateado);
        }
    }

    private void obtenerProducto() {
        try {
            List<Producto> datosBD = usuarioDAO.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarColumnas() {
        colCodigoDeBarra.setCellValueFactory(new PropertyValueFactory<>("CodigoDeBarra"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("Precio"));

        colSubtotal.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            if (p != null) {
                double subtotal = p.getPrecio() * p.getCantidad();
                return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
            }
            return new javafx.beans.property.SimpleDoubleProperty(0.0).asObject();
        });
    }

    public void facturaTipo(ActionEvent actionEvent) {
    }

    public void agregarProducto(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Parent root = loader.load();

            ControladorProducto controller = loader.getController();
            controller.setControladorProducto(this);

            Stage stage = new Stage();
            stage.setTitle("agregarProducto");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(ActionEvent actionEvent) {
        Producto seleccionado = TablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaUsuarios.remove(seleccionado);
            Calcular();
        }
    }

    public void buscarCliente(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("buscadorCliente.fxml"));
            Parent root = loader.load();

            controladorBuscadorCliente controller = loader.getController();
            controller.setControladorFactura(this);

            Stage stage = new Stage();
            stage.setTitle("buscadorCliente");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asignarClienteDesdeBuscador(String nombreCliente) {
        if (this.cliente != null) {
            this.cliente.setText(nombreCliente);
        }
    }

    public void eliminarCliente(ActionEvent actionEvent) {
        if (cliente != null && !cliente.getText().isEmpty()) {
            cliente.setText("");
            if (nombreYApellido != null) nombreYApellido.clear();
        }else AlertasUtils.mostrarAlerta("Información","Sin cliente", "No hay ningún cliente seleccionado en esta factura para eliminar", Alert.AlertType.ERROR);


    }

    public void agregarCliente(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("cliente.fxml"));
            Parent root = loader.load();

            controladorCliente controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Registrar / Seleccionar Cliente");
            stage.setScene(new Scene(root, 440, 540));

            stage.showAndWait();

            clienteClase clienteSeleccionado = controller.tablaClientes.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado != null) {
                cliente.setText(clienteSeleccionado.getNombreEntidad());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ingresarPago(ActionEvent actionEvent) {}
    public void selecEfectivo(ActionEvent actionEvent) {}
    public void selecTarjeta(ActionEvent actionEvent) {}
    public void elegirCodigo(ActionEvent actionEvent) {}

    // >>> CORRECCIÓN ABSOLUTA: El método que faltaba para procesar el Doble Clic sin duplicar filas <<<
    public void recibirProducto(Producto producto) {

            // Tu código original validaba con contains
            if (!listaUsuarios.contains(producto)) {
                // Inicializar cantidades de manera explícita para la primera inserción
                producto.setCantidad(1);
                if (producto.cantidadProperty() != null) {
                    producto.cantidadProperty().set(1);
                }

                listaUsuarios.add(producto);
            } else {
                // Si el objeto ya existía (según el contains), se incrementaba su cantidad en +1
                int nuevaCantidad = producto.getCantidad() + 1;
                producto.setCantidad(nuevaCantidad);
                if (producto.cantidadProperty() != null) {
                    producto.cantidadProperty().set(nuevaCantidad);
                }
            }

            // Refrescar la vista de la tabla y calcular el total de la factura
            TablaProductos.refresh();
            Calcular();
        }

    public void botonA(ActionEvent actionEvent) {
        tipoFactura.setText("A");
    }

    public void botonB(ActionEvent actionEvent) {
        tipoFactura.setText("B");
    }

    public void botonC(ActionEvent actionEvent) {
        tipoFactura.setText("C");
    }

    public void CodigoBarras(ActionEvent actionEvent) {
        codigo.setText("CodigoBarras");
    }

    public void CodigoNumerico(ActionEvent actionEvent) {
        codigo.setText("CodigoNumerico");
    }

    public void IngresarPago(ActionEvent actionEvent) {

    }
}

