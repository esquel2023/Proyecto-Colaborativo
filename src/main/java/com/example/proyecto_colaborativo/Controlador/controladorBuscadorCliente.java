package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class controladorBuscadorCliente {

        @FXML
        public TableView<claseFactura> tablaClientes1;
        @FXML public TableColumn<claseFactura, String> nombreTabla1;
        @FXML public TableColumn<claseFactura, String> dniTabla1;
        @FXML private TextField buscadorClientes;

        @FXML public TableView<clienteClase> tablaClientes;
        @FXML private TableColumn<clienteClase, String> nombreTabla;
        @FXML private TableColumn<clienteClase, String> dniTabla;
        @FXML private TableColumn<clienteClase, String> telefonoTabla;

        private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
        private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
            // Configuración de tabla Clientes
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            dniTabla.setCellValueFactory(new PropertyValueFactory<>("dniEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

            listaClientesObs.setAll(ClienteDAO.listar());
            tablaClientes.setItems(listaClientesObs);


            // Listener de selección
            tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
                if (newSelection == null) {
                    listaFacturasObs.clear();
                }
            });

            }
        }

