package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.event.ActionEvent;

public class controladorBifProv {
    public void agregar(ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("proveedores.fxml", "Nuevo Proveedor", false);

    }

    public void listado(ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("proveedoresGeneral.fxml", "Listado de Proveedores", false);

    }
}
