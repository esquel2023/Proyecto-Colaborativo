package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.bd.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class controladorCliente {



    public Label totalclientes;
    public Button modificarCliente;
    public Button eliminarCliente;


    @FXML
    private TextField buscadorClientes;


    @FXML
    public TableView<clienteClase> tablaClientes;
    @FXML
    private TableColumn<clienteClase, String> nombreTabla;
    @FXML
    private TableColumn<clienteClase, String> telefonoTabla;

    private final ObservableList<clienteClase> listaClientesObs = FXCollections.observableArrayList();
    private final ObservableList<claseFactura> listaFacturasObs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuración de tabla Clientes
        if (nombreTabla != null && telefonoTabla != null) {
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));

            listaClientesObs.setAll(ClienteDAO.listar());
            tablaClientes.setItems(listaClientesObs);



            // Listener de selección
            // UN SOLO LISTENER OPTIMIZADO
            tablaClientes.getSelectionModel().selectedItemProperty().addListener((_, _, clienteSeleccionado) -> {
                if (clienteSeleccionado == null) {
                    listaFacturasObs.clear();
                } else {
                    // Si seleccionó un cliente, rellenamos los campos


                    // Aquí puedes cargar las facturas de ese cliente de forma eficiente si lo necesitas
                }
            });


        }
    }


    @FXML
    void modificarCliente(ActionEvent event) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            try {
                // Se envía el nombre como cadena de texto directo a la BD
                NavegacionUtils.abrirPantalla("modificarCliente.fxml", "Modificar Cliente", false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @FXML
    void eliminarCliente(ActionEvent event) {
        clienteClase clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {
                // Se envía el nombre como cadena de texto directo a la BD
                ClienteDAO.eliminar(clienteSeleccionado.getNombreEntidad());

                // Se remueve de la interfaz visual
                listaClientesObs.remove(clienteSeleccionado);

                System.out.println("Cliente eliminado con éxito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
