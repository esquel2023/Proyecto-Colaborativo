package com.example.proyecto_colaborativo.Controlador;
import com.example.proyecto_colaborativo.*;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import com.example.proyecto_colaborativo.bd.ProductoDAO;
import javafx.beans.property.DoubleProperty;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorFactura implements Initializable {


    public SplitMenuButton tipoFactura;
    public Text unidades;
    public Text dni;
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

    // Instanciamos el DAO de manera global en el controlador
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
        // 1. Permitir que la tabla acepte edición
        TablaProductos.setEditable(true);

        // 2. Hacer editable la columna Cantidad (usa IntegerStringConverter)
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
                TablaProductos.refresh(); // Revierte el cambio visual si es inválido
            }
        });
        // 3. Hacer editable la columna Precio (usa DoubleStringConverter)
        colPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrecio.setOnEditCommit(event -> {
            Producto p = event.getRowValue();
            Double nuevoPrecio = event.getNewValue();
            if (nuevoPrecio != null && nuevoPrecio > 0) {
                p.setPrecio(nuevoPrecio);
                p.precioProperty().set(nuevoPrecio);
                Calcular(); // Recalcula el total de inmediato
            } else {
                TablaProductos.refresh(); // Revierte el cambio visual si es inválido
            }
        });
    }



    private void Calcular() {
        double totalAcumulado = 0.0;

        // Recorre todos los productos actualmente cargados en la factura
        for (Producto p : listaUsuarios) {
            totalAcumulado += (p.getPrecio() * p.getCantidad());
        }
        TablaProductos.refresh();

        String totalFormateado = String.format("$ %.2f", totalAcumulado);

        if (totalFinal != null) {
            totalFinal.setText(totalFormateado);
        }
        if (Total != null) {
            Total.setText(totalFormateado);
        }
    }

    private void obtenerProducto() {

    }

    private void cargarColumnas() {
        // Enlazar de manera obligatoria las columnas con los nombres de tus variables privadas
        colCodigoDeBarra.setCellValueFactory(new PropertyValueFactory<>("CodigoDeBarra"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("Precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));


        colSubtotal.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            // Multiplica el precio por la cantidad en caliente
            double subtotal = p.getPrecio() * p.getCantidad();
            return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();

        });
    }
    // 3. Cargar los registros de forma limpia invocando al DAO


    // El controlador recibe una lista de Java pura y la convierte en observable para la interfaz
    List<Producto> datosBD = usuarioDAO.listar();
//        listaUsuarios.addAll(datosBD);


    public void facturaTipo(ActionEvent actionEvent) {

    }

    public void agregarProducto(ActionEvent actionEvent) {
        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("Producto.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador DESPUÉS de cargar el root
            ControladorProducto controller = loader.getController();
            controller.setControladorProducto(this);

            // 3. Configurar y mostrar la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("agregarProducto");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(ActionEvent actionEvent) {
        // >>> NUEVO: Permite borrar un producto seleccionado de la factura <<<
        Producto seleccionado = TablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaUsuarios.remove(seleccionado);
            Calcular();
        }
    }


    public void buscarCliente(ActionEvent actionEvent) throws IOException {
        try {
            // 1. Cargar el FXML una sola vez
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("buscadorCliente.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador DESPUÉS de cargar el root
            controladorBuscadorCliente controller = loader.getController();
            controller.setControladorFactura(this);

            // 3. Configurar y mostrar la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("buscadorCliente");
            stage.setScene(new Scene(root, 440, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // >>> NUEVO MÉTODO en Factura: El buscador usará esto para meter el nombre aquí <<<
    // Cambia el nombre del método para que coincida exactamente con lo que busca el controlador del buscador
    public void asignarClienteDesdeBuscador(String nombreCliente) {
        if (this.cliente != null) {
            this.cliente.setText(nombreCliente);
        }
    }

    public void eliminarCliente(ActionEvent actionEvent) {
//        clienteClase clienteSeleccionado = cliente.getSelectionModel();
//        if (clienteSeleccionado != null) {
//            listaUsuarios.remove(clienteSeleccionado);
//        }
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
                // 5. Mostrar el dato en el Label de la factura
                cliente.setText(clienteSeleccionado.getNombreEntidad());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ingresarPago(ActionEvent actionEvent) {
    }

    public void selecEfectivo(ActionEvent actionEvent) {
    }

    public void selecTarjeta(ActionEvent actionEvent) {
    }

    public void elegirCodigo(ActionEvent actionEvent) {
    }
    public void recibirProducto(Producto producto) {
        if (producto != null) {
            if (!listaUsuarios.contains(producto)) {
                // Por seguridad de negocio, forzamos que entre a la factura valiendo 1 unidad
                producto.setCantidad(1);
                producto.cantidadProperty().set(1);

                listaUsuarios.add(producto);
                Calcular(); // Suma el nuevo elemento al total de la factura
            } else {
                // Si el producto ya existe en la lista, le incrementamos la cantidad en 1 automáticamente
                int cantidadActual = producto.getCantidad();
                producto.setCantidad(cantidadActual + 1);
                producto.cantidadProperty().set(cantidadActual + 1);
                TablaProductos.refresh();
                Calcular();


//                if (colCantidad <= 0 || colPrecio <= 0) {
//                    AlertasUtils.mostrarAlerta("Valores inválidos", "Números negativos detectados",
//                            "La cantidad y el precio final no pueden ser números negativos. Por favor, ingresá valores mayores o iguales a cero.", javafx.scene.control.Alert.AlertType.ERROR);
//
//                    return;
//
//                }
            }
        }
    }
}