module com.example.proyecto_colaborativo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.example.proyecto_colaborativo to javafx.fxml;
    opens com.example.proyecto_colaborativo.Controlador to javafx.fxml;
    exports com.example.proyecto_colaborativo;
    exports com.example.proyecto_colaborativo.Utilits;
    opens com.example.proyecto_colaborativo.Utilits to javafx.fxml;
    exports com.example.proyecto_colaborativo.Controlador;
    exports com.example.proyecto_colaborativo.Clases;
    opens com.example.proyecto_colaborativo.Clases to javafx.fxml;
}