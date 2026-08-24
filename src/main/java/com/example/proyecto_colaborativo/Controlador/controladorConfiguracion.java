package com.example.proyecto_colaborativo.Controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

    public class controladorConfiguracion {

        @FXML
        private ComboBox<String> comboTema;

        @FXML
        private ComboBox<String> comboColor;

        @FXML
        private Label labelUsuario;


        /*
         * Se ejecuta automáticamente
         * cuando se abre Configuración
         */
        @FXML
        public void initialize() {

            /* TEMAS */

            comboTema.setItems(
                    FXCollections.observableArrayList(
                            "Modo claro",
                            "Modo oscuro"
                    )
            );

            comboTema.setValue("Modo claro");


            /* COLORES */

            comboColor.setItems(
                    FXCollections.observableArrayList(
                            "Azul AXYRA",
                            "Celeste",
                            "Violeta",
                            "Verde"
                    )
            );

            comboColor.setValue("Azul AXYRA");


            /* USUARIO */

            labelUsuario.setText("Administrador");


            /* Detectar cambio de tema */

            comboTema.setOnAction(event -> cambiarTema());


            /* Detectar cambio de color */

            comboColor.setOnAction(event -> cambiarColor());

        }


    /* =====================================================
       APARIENCIA
       ===================================================== */

        private void cambiarTema() {

            String temaSeleccionado = comboTema.getValue();

            if (temaSeleccionado == null) {
                return;
            }

            if (temaSeleccionado.equals("Modo oscuro")) {

                mostrarMensaje(
                        "Apariencia",
                        "Modo oscuro seleccionado.\n\n" +
                                "Después podemos conectar esta opción con un CSS oscuro para todo AXYRA."
                );

            }

        }


        private void cambiarColor() {

            String color = comboColor.getValue();

            if (color == null) {
                return;
            }

            System.out.println(
                    "Color seleccionado: " + color
            );

        }


    /* =====================================================
       COPIA DE SEGURIDAD
       ===================================================== */

        @FXML
        private void crearBackup() {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle(
                    "Guardar copia de seguridad"
            );

            fileChooser.setInitialFileName(
                    "AXYRA_Backup"
            );

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Archivo de respaldo (*.backup)",
                            "*.backup"
                    )
            );

            Stage stage =
                    (Stage) comboTema.getScene().getWindow();

            File archivo =
                    fileChooser.showSaveDialog(stage);

            if (archivo != null) {

                /*
                 * ACÁ después conectamos
                 * tu base de datos real.
                 *
                 * Por ahora solamente
                 * mostramos el mensaje.
                 */

                mostrarMensaje(
                        "Copia de seguridad",
                        "La copia de seguridad fue creada correctamente."
                );

                System.out.println(
                        "Backup guardado en: "
                                + archivo.getAbsolutePath()
                );

            }

        }


    /* =====================================================
       RESTAURAR BACKUP
       ===================================================== */

        @FXML
        private void restaurarBackup() {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Seleccionar copia de seguridad"
            );

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Archivo de respaldo (*.backup)",
                            "*.backup"
                    )
            );

            Stage stage =
                    (Stage) comboTema.getScene().getWindow();

            File archivo =
                    fileChooser.showOpenDialog(stage);

            if (archivo == null) {
                return;
            }


            Alert confirmacion =
                    new Alert(Alert.AlertType.CONFIRMATION);

            confirmacion.setTitle(
                    "Restaurar copia"
            );

            confirmacion.setHeaderText(
                    "¿Restaurar esta copia de seguridad?"
            );

            confirmacion.setContentText(
                    "Los datos actuales podrían ser reemplazados."
            );


            Optional<ButtonType> resultado =
                    confirmacion.showAndWait();


            if (resultado.isPresent()
                    && resultado.get() == ButtonType.OK) {

                /*
                 * ACÁ después conectamos
                 * la restauración real
                 * de tu base de datos.
                 */

                mostrarMensaje(
                        "Restauración completada",
                        "La copia de seguridad fue restaurada correctamente."
                );

                System.out.println(
                        "Restaurando: "
                                + archivo.getAbsolutePath()
                );

            }

        }


    /* =====================================================
       CERRAR SESIÓN
       ===================================================== */

        @FXML
        private void cerrarSesion() {

            Alert alerta =
                    new Alert(Alert.AlertType.CONFIRMATION);

            alerta.setTitle(
                    "Cerrar sesión"
            );

            alerta.setHeaderText(
                    "¿Querés cerrar sesión?"
            );

            alerta.setContentText(
                    "Volverás a la pantalla de inicio de sesión."
            );


            Optional<ButtonType> resultado =
                    alerta.showAndWait();


            if (resultado.isPresent()
                    && resultado.get() == ButtonType.OK) {

                try {

                    /*
                     * CAMBIÁ login.fxml si tu archivo
                     * tiene otro nombre.
                     */

                    FXMLLoader loader =
                            new FXMLLoader(
                                    getClass()
                                            .getResource(
                                                    "/com/example/proyecto_colaborativo/login.fxml"
                                            )
                            );

                    Parent root =
                            loader.load();


                    Stage stage =
                            (Stage) comboTema
                                    .getScene()
                                    .getWindow();


                    Scene scene =
                            new Scene(root);


                    stage.setScene(scene);

                    stage.centerOnScreen();

                    stage.show();


                } catch (IOException e) {

                    e.printStackTrace();

                    mostrarError(
                            "No se pudo volver a la pantalla de inicio de sesión."
                    );

                }

            }

        }


    /* =====================================================
       ALERTAS
       ===================================================== */

        private void mostrarMensaje(
                String titulo,
                String mensaje
        ) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(titulo);

            alert.setHeaderText(null);

            alert.setContentText(mensaje);

            alert.showAndWait();

        }


        private void mostrarError(
                String mensaje
        ) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle(
                    "AXYRA"
            );

            alert.setHeaderText(
                    "Ocurrió un error"
            );

            alert.setContentText(
                    mensaje
            );

            alert.showAndWait();

        }


    }