package com.example.proyecto_colaborativo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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


    // CONTENEDOR DONDE SE VAN A MOSTRAR LAS PANTALLAS
    @FXML
    private StackPane contenidoPrincipal;


    // VISTA ORIGINAL DEL INICIO
    @FXML
    private VBox vistaInicio;


    private boolean cajaVisible = true;

    private double montoCaja = 0.00;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline reloj = new Timeline(

                new KeyFrame(
                        Duration.ZERO,
                        e -> actualizarFechaHora()
                ),

                new KeyFrame(
                        Duration.seconds(1)
                )
        );

        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.play();


        lblCaja.setText(
                String.format("$ %,.2f", montoCaja)
        );


        asignarImagen(
                btnOjoCaja,
                "/ImagenesBilletes/openn.png"
        );
    }


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


        saludoLabel.setText(saludo);

        fechaHoraLabel.setText(
                capitalizar(
                        ahora.format(formato)
                )
        );
    }


    private String capitalizar(String format) {

        return format;
    }


    // ==================================================
    // INICIO
    // ==================================================

    @FXML
    public void botonInicio(ActionEvent actionEvent) {

        contenidoPrincipal
                .getChildren()
                .setAll(vistaInicio);
    }


    // ==================================================
    // CAJA
    // ==================================================

    @FXML
    public void botonCaja(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "aperturaycierrecaja.fxml"
        );
    }


    // ==================================================
    // VENTAS
    // ==================================================

    @FXML
    public void botonVentas(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "ventas.fxml"
        );
    }


    // ==================================================
    // PRODUCTOS
    // ==================================================

    @FXML
    public void botonProductos(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "Producto.fxml"
        );
    }


    // ==================================================
    // PROVEEDORES
    // ==================================================

    @FXML
    public void botonProveedores(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "proveedores.fxml"
        );
    }


    // ==================================================
    // STOCK
    // ==================================================

    @FXML
    public void botonStock(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "stock.fxml"
        );
    }


    // ==================================================
    // CLIENTES
    // ==================================================

    @FXML
    public void botonClientes(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "clienteGeneral.fxml"
        );
    }


    // ==================================================
    // FACTURAS
    // ==================================================

    @FXML
    public void botonFactura(ActionEvent actionEvent) throws IOException {

        cargarVista(
                "factura.fxml"
        );
    }


    // ==================================================
    // MÉTODO QUE CARGA LAS PANTALLAS
    // ==================================================

    private void cargarVista(String archivoFXML) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(
                        archivoFXML
                )
        );


        Parent vista = loader.load();


        contenidoPrincipal
                .getChildren()
                .setAll(vista);
    }


    // ==================================================
    // MOSTRAR / OCULTAR MONTO DE CAJA
    // ==================================================

    @FXML
    public void toggleCaja(ActionEvent actionEvent) {

        cajaVisible = !cajaVisible;


        if (cajaVisible) {

            lblCaja.setText(
                    String.format(
                            "$ %,.2f",
                            montoCaja
                    )
            );


            asignarImagen(
                    btnOjoCaja,
                    "/ImagenesBilletes/openn.png"
            );


        } else {


            lblCaja.setText(
                    "••••••••"
            );


            asignarImagen(
                    btnOjoCaja,
                    "/ImagenesBilletes/closee.png"
            );

        }
    }


    // ==================================================
    // IMAGEN DEL OJO
    // ==================================================

    private void asignarImagen(
            Button boton,
            String rutaRecurso
    ) {

        InputStream stream =
                getClass()
                        .getResourceAsStream(
                                rutaRecurso
                        );


        if (stream != null) {

            Image imagen =
                    new Image(
                            stream
                    );


            ImageView imageView =
                    new ImageView(
                            imagen
                    );


            imageView.setFitWidth(20);

            imageView.setFitHeight(20);

            imageView.setPreserveRatio(true);


            boton.setGraphic(
                    imageView
            );


        } else {

            System.err.println(
                    "No se encontró el archivo: "
                            + rutaRecurso
            );
        }
    }
    @FXML
    private void abrirConfiguracion() throws IOException {
        cargarVista("configuracion.fxml");
    }

}


