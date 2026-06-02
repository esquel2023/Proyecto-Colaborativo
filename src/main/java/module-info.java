module com.example.proyecto_colaborativo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proyecto_colaborativo to javafx.fxml;
    opens com.example.proyecto_colaborativo.Controlador to javafx.fxml;
    exports com.example.proyecto_colaborativo;
}