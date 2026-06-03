package com.example.proyecto_colaborativo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class controladorContador {

        @FXML
        public ImageView billete50;
        @FXML
        public ImageView billete100;
        @FXML
        public ImageView billete200;
        @FXML
        public ImageView billete500;
        @FXML
        public ImageView billete1000;
        @FXML
        public ImageView billete2000;
        @FXML
        public ImageView billete10000;
        @FXML
        public ImageView billete20000;

        public void cargarUI() {
            try {
                cargarIcono("billete50", "/imagen/billete50.jpg");
                cargarIcono("billete100", "/imagen/billete100.jpg");
                cargarIcono("billete200", "/imagen/billete200.jpg");
                cargarIcono("billete500", "/imagen/billete500.jpg");
                cargarIcono("billete1000", "/imagen/billete1000.jpg");
                cargarIcono("billete2000", "/imagen/billete2000.jpg");
                cargarIcono("billete10000", "/imagen/billete10000.jpg");
                cargarIcono("billete20000", "/imagen/billete20000.jpg");

            } catch (NullPointerException e) {
                System.err.println("Error: No se encontró uno de los recursos gráficos en 'resources/imagen/'");
                e.printStackTrace();
            }
        }

        private void cargarIcono(String s, String s1) {
        }

        public void salirPantalla(ActionEvent actionEvent) {
        }
    }

