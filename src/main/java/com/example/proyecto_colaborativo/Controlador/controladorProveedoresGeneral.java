package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Clases.proovedorClase;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.bd.ProveedorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.example.proyecto_colaborativo.Utilits.NavegacionUtils.abrirPantalla;

public class controladorProveedoresGeneral {
    public TextField buscadorProovedores;
    public TableView tablaProovedores;
    public TableColumn nombreTabla;
    public TableColumn telefonoTabla;
    public TableColumn emailTabla;
    private final ObservableList<proovedorClase> listaProveedoresObs = FXCollections.observableArrayList();

    public void initialize() {
        if (tablaProovedores != null) {
            tablaProovedores.setPlaceholder(new Label("No hay proveedores cargados"));

            // 1. Configuración de mapeo de columnas de las tablas
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));
            emailTabla.setCellValueFactory(new PropertyValueFactory<>("emailEntidad"));
            listaProveedoresObs.setAll(ProveedorDAO.listar());

            tablaProovedores.setItems(listaProveedoresObs);

            tablaProovedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {

                    abrirPantalla("proveedorSeleccionado.fxml", "Proovedor Seleccionado", false);
                    controladorProveedorSelec.setProveedorSelec((proovedorClase) newValue);

                }
            });
        }

    }
    void buscadorProveedores(){
                BuscadorUtils.configuradorBuscador(
                buscadorProovedores,
                tablaProovedores,
                listaProveedoresObs,
                (proveedor, texto) -> {
                    // Validación segura contra valores nulos
                    boolean coincideNombre = proveedor.getNombreEntidad() != null &&
                            proveedor.getNombreEntidad().toLowerCase().contains(texto);

                    return coincideNombre;
                });
    }
}

