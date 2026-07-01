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
    public TableView<proovedorClase> tablaProovedores;
    public TableColumn<proovedorClase,String> nombreTabla;
    public TableColumn<proovedorClase,String> telefonoTabla;
    public TableColumn<proovedorClase,String> emailTabla;
    private final ObservableList<proovedorClase> listaProveedoresObs = FXCollections.observableArrayList();

    public void initialize() {
        if (tablaProovedores != null) {
            tablaProovedores.setPlaceholder(new Label("No hay proveedores cargados"));

            // 1. Configuración de mapeo de columnas de las tablas
            nombreTabla.setCellValueFactory(new PropertyValueFactory<>("nombreEntidad"));
            telefonoTabla.setCellValueFactory(new PropertyValueFactory<>("telefonoEntidad"));
            emailTabla.setCellValueFactory(new PropertyValueFactory<>("emailEntidad"));

            // 2. Cargamos los datos desde el DAO
            listaProveedoresObs.setAll(ProveedorDAO.listar());
            tablaProovedores.setItems(listaProveedoresObs);

            // SOLUCIÓN CLAVE: Activamos el método del buscador para que configure la lista filtrada
            buscadorProveedores();

            // 3. Listener para detectar cuando se hace clic en una fila
            tablaProovedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    abrirPantalla("proveedorSeleccionado.fxml", "Proveedor Seleccionado", false);

                    // CORREGIDO: Quitamos el casteo innecesario (proovedorClase) porque 'newValue' ya es de ese tipo
                    controladorProveedorSelec.setProveedorSelec(newValue);
                }
            });
        }
    }

    // Cambiado a public para que JavaFX o el inicializador puedan gestionarlo sin problemas
    public void buscadorProveedores(){
        BuscadorUtils.configuradorBuscador(
                buscadorProovedores,
                tablaProovedores,
                listaProveedoresObs,
                (proveedor, texto) -> {
                    // Validación segura contra valores nulos
                    boolean coincideNombre = proveedor.getNombreEntidad() != null &&
                            proveedor.getNombreEntidad().toLowerCase().contains(texto);

                    return coincideNombre;
                }
        );
    }
}
