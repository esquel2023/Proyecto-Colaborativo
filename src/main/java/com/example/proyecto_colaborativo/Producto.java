package com.example.proyecto_colaborativo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Producto {

    private final StringProperty nombre;
    private final StringProperty cantidad; // Si la cantidad lleva letras (ej: "10 kg"), usá String. Si es entera, podés usar SimpleIntegerProperty.
    private final DoubleProperty precio;
    private final StringProperty codigoTabla;

    // Constructor
    public Producto(String nombre, String cantidad, double precio, String codigoTabla) {
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleStringProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoTabla = new SimpleStringProperty(codigoTabla);
    }

    // Getters de Propiedades (Requeridos por TableView)
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoTablaProperty() { return codigoTabla; }

    // Getters ordinarios
    public String getNombre() { return nombre.get(); }
    public String getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public String getCodigoTabla() { return codigoTabla.get(); }
}

