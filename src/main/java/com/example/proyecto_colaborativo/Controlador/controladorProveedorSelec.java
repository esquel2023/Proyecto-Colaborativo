package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class controladorProveedorSelec {

    private static controladorProveedorSelec instanciaActiva;


    @FXML public Button botonAgregar;
    @FXML public Button botonEliminar;
    @FXML public TableView<Producto> tablaProductosProovedor; // Reemplazar '?' por tu clase Producto si la tenés
    @FXML public TableColumn<Producto, ?> prooductosProovedor;
    @FXML public TableColumn<Producto, ?> precioProovedor;
    @FXML public TableColumn<Producto, ?> prooductosProovedor1;


    @FXML private Label proveedorSelec;

    public static void setProveedorSelec(proovedorClase proveedor) {
        if (instanciaActiva != null && instanciaActiva.proveedorSelec != null && proveedor != null) {
            instanciaActiva.proveedorSelec.setText(proveedor.getNombreEntidad());
        }
    }

    @FXML
    public void initialize() {

        instanciaActiva = this;

    }

    @FXML
    public void botonAgregar(ActionEvent actionEvent) {
        // Lógica para agregar productos relacionados al proveedor
    }

    @FXML
    public void botonElimina(ActionEvent actionEvent) {
        // Lógica para eliminar productos relacionados al proveedor
    }
}
