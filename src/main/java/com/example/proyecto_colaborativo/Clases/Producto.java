package com.example.proyecto_colaborativo.Clases;

import javafx.beans.property.*;

public class Producto {

    private final StringProperty nombre;
    private final IntegerProperty cantidad; // Si la cantidad lleva letras (ej: "10 kg"), usá String. Si es entera, podés usar SimpleIntegerProperty.
    private final DoubleProperty precio;
    private final StringProperty codigoTabla;

    // Constructor
    public Producto(String nombre, Integer cantidad, double precio, String codigoTabla) {
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoTabla = new SimpleStringProperty(codigoTabla);
    }

    // Getters de Propiedades (Requeridos por TableView)
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoTablaProperty() { return codigoTabla; }

    // Getters ordinarios
    public String getNombre() { return nombre.get(); }
    public Integer getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public String getCodigoTabla() { return codigoTabla.get(); }
}

