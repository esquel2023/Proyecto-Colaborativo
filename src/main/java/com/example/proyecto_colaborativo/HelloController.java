package com.example.proyecto_colaborativo;

import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button caja;

    @FXML
    private Button ventas;

    @FXML
    private Button productos;

    @FXML
    private Button proveedores;

    @FXML
    private Button stock;

    @FXML
    private Button clientes;

    @FXML
    private Button factura;

    @FXML
    private Label fechaHoraLabel;

    @FXML
    private Label saludoLabel;

    @FXML
    private Label lblCaja;

    @FXML
    private Button btnOjoCaja;

    @FXML
    private boolean cajaVisible = true;
    @FXML
    private double montoCaja = 0.00;
    @FXML
    private void actualizarFechaHora() {

        LocalDateTime ahora = LocalDateTime.now();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                "EEEE d 'de' MMMM · HH:mm",
                new Locale("es", "AR")
        );

        int hora = ahora.getHour();
        String saludo;

        if (hora < 12) {
            saludo = "☀ Buenos días";
        } else if (hora < 19) {
            saludo = "🌤 Buenas tardes";
        } else {
            saludo = "🌙 Buenas noches";
        }

        saludoLabel.setText(saludo + "");
        fechaHoraLabel.setText(capitalizar(ahora.format(formato)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline reloj = new Timeline(
                new KeyFrame(Duration.ZERO, e -> actualizarFechaHora()),
                new KeyFrame(Duration.seconds(1))
        );

        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.play();
        lblCaja.setText(String.format("$ %,.2f", montoCaja));
        asignarImagen(btnOjoCaja, "/ImagenesBilletes/openn.png");
    }



    private String capitalizar(String format) {

        return format;
    }

    @FXML
    public void botonCaja(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("aperturaycierrecaja.fxml", "Caja", false);
    }

    @FXML
    public void botonVentas(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("ventas.fxml", "Ventas", false);
    }

    @FXML
    public void botonProductos(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("Producto.fxml", "Producto", false);
    }

    @FXML
    public void botonProveedores(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("proveedores.fxml", "Nuevo Proveedor", false);    }

    @FXML
    public void botonStock(ActionEvent actionEvent) {
        NavegacionUtils.abrirPantalla("stock.fxml", "Stock", false);
    }

    @FXML
    public void botonClientes(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("BifurcacionCliente.fxml", "Cliente", false);
    }

    @FXML
    public void botonFactura(ActionEvent actionEvent) throws IOException {
        NavegacionUtils.abrirPantalla("factura.fxml", "Factura", false);
    }

    @FXML
    public void toggleCaja(ActionEvent actionEvent) {

            cajaVisible = !cajaVisible;

            if (cajaVisible) {

                lblCaja.setText(String.format("$ %,.2f", montoCaja));


                asignarImagen(btnOjoCaja, "/ImagenesBilletes/openn.png");



            } else {

                lblCaja.setText("••••••••");

                asignarImagen(btnOjoCaja, "/ImagenesBilletes/closee.png");



            }

        }
    private void asignarImagen(Button boton, String rutaRecurso) {
        InputStream stream = getClass().getResourceAsStream(rutaRecurso);

        if (stream != null) {
            Image imagen = new Image(stream);
            ImageView imageView = new ImageView(imagen);

            imageView.setFitWidth(20);   // ancho
            imageView.setFitHeight(20);  // alto
            imageView.setPreserveRatio(true);

            boton.setGraphic(imageView);
        } else {
            System.err.println("No se encontró el archivo: " + rutaRecurso);
        }
    }
}


