package com.example.proyecto_colaborativo.Controlador;

import com.example.proyecto_colaborativo.Utilits.AlertasUtils;
import com.example.proyecto_colaborativo.Utilits.BuscadorUtils;
import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Utilits.NavegacionUtils;
import com.example.proyecto_colaborativo.bd.ProductoDAO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ControladorProducto {


    /* =========================================================
       TABLA
       ========================================================= */

    @FXML
    public TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Integer> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Integer> colCantidad;

    @FXML
    private TableColumn<Producto, Double> colPrecio;


    /* =========================================================
       CAMPOS
       ========================================================= */

    @FXML
    private TextField codigo;

    @FXML
    private TextField txtbuscadorProductos;

    @FXML
    private Button botonSalir;


    /* =========================================================
       PRECIO
       ========================================================= */

    @FXML
    private TextField precioCosto;

    @FXML
    private TextField porcentaje;

    @FXML
    private Label labelPrecioVenta;


    /* =========================================================
       FECHA Y HORA
       ========================================================= */

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaLabel;


    /* =========================================================
       LISTA DE PRODUCTOS
       ========================================================= */

    public static final ObservableList<Producto> listaProductos =
            FXCollections.observableArrayList();


    public static Producto productoseleccionado;


    private ControladorFactura controladorFactura;


    public void setControladorProducto(
            ControladorFactura controladorFactura
    ) {

        this.controladorFactura = controladorFactura;
    }


    /* =========================================================
       INITIALIZE
       ========================================================= */

    @FXML
    public void initialize() {

        /* ================= FECHA Y HORA ================= */

        iniciarReloj();


        /* ================= TABLA ================= */

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("idProducto")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCantidad.setCellValueFactory(
                new PropertyValueFactory<>("cantidad")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );


        /* ================= CARGAR DATOS ================= */

        cargarDatosDesdeBD();


        /* ================= BUSCADOR ================= */

        BuscadorUtils.configuradorBuscador(

                txtbuscadorProductos,

                tablaProductos,

                listaProductos,

                (producto, texto) -> {

                    boolean coincideNombre =
                            producto.getNombre() != null
                                    && producto
                                    .getNombre()
                                    .toLowerCase()
                                    .contains(texto);


                    boolean coincideCodigo =
                            producto.getCodigoBarra() != null
                                    && producto
                                    .getCodigoBarra()
                                    .toLowerCase()
                                    .contains(texto);


                    return coincideNombre
                            || coincideCodigo;
                }
        );


        /* ================= SELECCION TABLA ================= */

        tablaProductos
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue != null) {

                                ControladorProducto.productoseleccionado =
                                        newValue;


                                System.out.println(
                                        "Seleccionaste: "
                                                + newValue.getNombre()
                                );
                            }
                        }
                );


        /* ================= DOBLE CLICK ================= */

        tablaProductos.setRowFactory(tv -> {

            TableRow<Producto> fila =
                    new TableRow<>();


            fila.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2
                        && !fila.isEmpty()) {

                    Producto productoSeleccionado =
                            fila.getItem();


                    if (controladorFactura != null) {

                        controladorFactura.recibirProducto(
                                productoSeleccionado
                        );


                        Stage stage =
                                (Stage)
                                        tablaProductos
                                                .getScene()
                                                .getWindow();


                        stage.close();
                    }
                }
            });


            return fila;
        });
    }


    /* =========================================================
       RELOJ
       ========================================================= */

    private void iniciarReloj() {

        Locale locale =
                new Locale("es", "AR");


        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "EEEE dd 'de' MMMM 'de' yyyy",
                        locale
                );


        DateTimeFormatter formatoHora =
                DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                );


        Timeline reloj =
                new Timeline(

                        new KeyFrame(

                                Duration.ZERO,

                                event -> {

                                    LocalDateTime ahora =
                                            LocalDateTime.now();


                                    String fecha =
                                            ahora.format(
                                                    formatoFecha
                                            );


                                    if (!fecha.isEmpty()) {

                                        fecha =
                                                fecha
                                                        .substring(0, 1)
                                                        .toUpperCase()
                                                        +
                                                        fecha.substring(1);
                                    }


                                    if (fechaLabel != null) {

                                        fechaLabel.setText(
                                                fecha
                                        );
                                    }


                                    if (horaLabel != null) {

                                        horaLabel.setText(
                                                ahora.format(
                                                        formatoHora
                                                )
                                        );
                                    }
                                }
                        ),


                        new KeyFrame(
                                Duration.seconds(1)
                        )
                );


        reloj.setCycleCount(
                Timeline.INDEFINITE
        );


        reloj.play();
    }


    /* =========================================================
       MODIFICAR
       ========================================================= */

    @FXML
    private void clickModificar(
            ActionEvent event
    ) {

        if (productoseleccionado == null) {

            AlertasUtils.mostrarAlerta(

                    "Sin selección",

                    "No se seleccionó ningún producto",

                    "Debes seleccionar un producto de la tabla para poder modificarlo.",

                    Alert.AlertType.WARNING
            );

            return;
        }


        Producto.productoSeleccionadoParaEditar =
                productoseleccionado;


        NavegacionUtils.abrirPantalla(

                "ProductoAgregar.fxml",

                "Modificar Producto",

                true
        );


        tablaProductos.refresh();


        tablaProductos
                .getSelectionModel()
                .clearSelection();


        productoseleccionado = null;
    }


    /* =========================================================
       AGREGAR
       ========================================================= */

    @FXML
    private void clickAgregar(
            ActionEvent event
    ) {

        NavegacionUtils.abrirPantalla(

                "ProductoAgregar.fxml",

                "Agregar Nuevo Producto",

                true
        );


        cargarDatosDesdeBD();
    }


    /* =========================================================
       CARGAR DATOS BD
       ========================================================= */

    private void cargarDatosDesdeBD() {

        try {

            listaProductos.clear();


            listaProductos.setAll(
                    ProductoDAO.listar()
            );


        } catch (Exception e) {

            AlertasUtils.mostrarAlerta(

                    "Error de BD",

                    "Error de lectura",

                    "No se pudieron recuperar los productos de la base de datos.",

                    Alert.AlertType.ERROR
            );


            e.printStackTrace();
        }
    }


    /* =========================================================
       ELIMINAR
       ========================================================= */

    @FXML
    public void clickEliminar(
            ActionEvent event
    ) throws SQLException {

        if (productoseleccionado == null) {

            AlertasUtils.mostrarAlerta(

                    "Error",

                    "Producto no Seleccionado",

                    "Debes seleccionar un producto de la tabla para eliminarlo.",

                    Alert.AlertType.INFORMATION
            );

            return;
        }


        Alert alerta =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        alerta.setTitle(
                "Confirmar Eliminación"
        );


        alerta.setHeaderText(
                "¿Estás seguro de que querés eliminar este producto?"
        );


        alerta.setContentText(

                "Producto: "
                        + productoseleccionado.getNombre()

                        + "\nEsta acción no se puede deshacer."
        );


        java.util.Optional<ButtonType> resultado =
                alerta.showAndWait();


        if (resultado.isPresent()
                && resultado.get()
                == ButtonType.OK) {


            ProductoDAO.eliminar(
                    productoseleccionado
                            .getidProducto()
            );


            listaProductos.remove(
                    productoseleccionado
            );


            tablaProductos
                    .getSelectionModel()
                    .clearSelection();


            productoseleccionado = null;


            System.out.println(
                    "¡Producto eliminado con éxito!"
            );


        } else {

            System.out.println(
                    "Eliminación cancelada por el usuario."
            );
        }
    }


    /* =========================================================
       SALIR
       ========================================================= */

    @FXML
    public void clickSalir(
            ActionEvent event
    ) {

        Stage stage =
                (Stage)
                        botonSalir
                                .getScene()
                                .getWindow();


        stage.close();
    }


    /* =========================================================
       CALCULAR PRECIO
       ========================================================= */

    @FXML
    private void clickCalcularPrecio() {

        try {

            String costoTexto =
                    precioCosto
                            .getText()
                            .replace("$", "")
                            .replace(",", ".")
                            .trim();


            String porcentajeTexto =
                    porcentaje
                            .getText()
                            .replace("%", "")
                            .replace(",", ".")
                            .trim();


            double costo =
                    Double.parseDouble(
                            costoTexto
                    );


            double ganancia =
                    Double.parseDouble(
                            porcentajeTexto
                    );


            double precioVenta =
                    costo
                            + (costo * ganancia / 100);


            labelPrecioVenta.setText(

                    String.format(
                            "$ %.2f",
                            precioVenta
                    )
            );


        } catch (NumberFormatException e) {

            labelPrecioVenta.setText(
                    "Datos inválidos"
            );
        }
    }

    public void calcularPrecio(ActionEvent actionEvent) {
    }
}