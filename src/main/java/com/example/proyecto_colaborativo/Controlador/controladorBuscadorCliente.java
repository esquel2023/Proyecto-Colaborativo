package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class controladorBuscadorCliente {

    @FXML
    public TableView<claseFactura> tablaClientes1;
    @FXML
    public TableColumn<claseFactura, String> nombreTabla1;
    @FXML
    public TableColumn<claseFactura, String> dniTabla1;
    @FXML
    private TextField buscadorClientes;

    @FXML
    public TableView<clienteClase> tablaClientes;
    @FXML
    private TableColumn<clienteClase, String> nombreTabla;
    @FXML
    private TableColumn<clienteClase, String> dniTabla;
    @FXML
    private TableColumn<clienteClase, String> telefonoTabla;

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();
    private ControladorFactura facturaController;

    // EL MÉTODO YA TIENE SUS LLAVES PERFECTAS AQUÍ
    public void setControladorFactura(ControladorFactura facturaController) {
        this.facturaController = facturaController;
    } // <-- Cierra el método correctamente

    @FXML
    public void initialize() {
        // 1. Configuración de tabla Clientes
        nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
        dniTabla.setCellValueFactory(new PropertyValueFactory<>("dniEntidad"));
        telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

        listaClientesObs.setAll(ClienteDAO.listar());
        tablaClientes.setItems(listaClientesObs);

        // 2. UNIFICADO: Un solo listener de selección sin duplicados
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, clienteseleccionado) -> {
            if (clienteseleccionado != null) {
                if (facturaController != null) {
                    // Envía el dato directo al Label de la factura
                    facturaController.asignarClienteDesdeBuscador(clienteseleccionado.getNombreEntidad());

                    // Cierra la ventana flotante del buscador
                    Stage stage = (Stage) tablaClientes.getScene().getWindow();
                    stage.close();
                }
            } else {
                listaFacturasObs.clear();
            }
        });

        // 3. UNIFICADO: Un solo buscador inteligente seguro contra nulos y minúsculas
        BuscadorUtils.configuradorBuscador(
                buscadorClientes,
                tablaClientes,
                tablaClientes.getItems(),
                (cliente, texto) -> {
                    boolean coincideNombre = cliente.getNombreEntidad() != null &&
                            cliente.getNombreEntidad().toLowerCase().contains(texto.toLowerCase());
                    return coincideNombre;
                });

    } // Cierra initialize
} // Cierra la clase de forma definitiva
