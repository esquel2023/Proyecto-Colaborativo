package com.example.proyecto_colaborativo.Controlador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


    public class controladorBilletes implements Initializable {

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
        asignarImagen(billete50, "/ImagenesBilletes/billete50.jpg");
        asignarImagen(billete100, "/ImagenesBilletes/billete100.jpg");
        asignarImagen(billete200, "/ImagenesBilletes/billete200.jpg");
        asignarImagen(billete500, "/ImagenesBilletes/billete500.jpg");
        asignarImagen(billete1000, "/ImagenesBilletes/billete1000.jpg");
        asignarImagen(billete2000, "/ImagenesBilletes/billete2000.jpg");
        asignarImagen(billete10000, "/ImagenesBilletes/billete10000.jpg");
        asignarImagen(billete20000, "/ImagenesBilletes/billete20000.jpg");
        }


    private void asignarImagen(ImageView iv, String rutaRecurso) {
        java.io.InputStream stream = getClass().getResourceAsStream(rutaRecurso);

        if (stream != null) {
            iv.setImage(new Image(stream));
            } else {
                System.err.println("Error crítico: No se encontró el archivo en: " + rutaRecurso);
            }
        }

    private void cargarIcono(Button boton, String s1) {
        boton.setGraphic(crearIcono(s1));
    }

    private ImageView crearIcono(String rutaRecurso) {
        URL url = getClass().getResource(rutaRecurso);
        if (url == null) {
            throw new NullPointerException("No se pudo encontrar el recurso: " + rutaRecurso);
        }
        ImageView iv = new ImageView(new Image(url.toExternalForm()));
        iv.setFitWidth(30);
        iv.setFitHeight(30);
        return iv;
    }

    public void salirPantalla(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarUI();
    }
}
