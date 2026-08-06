package com.example.proyecto_colaborativo.Clases;

import com.fasterxml.jackson.annotation.JsonAlias;
import javafx.beans.property.*;

public class Producto {

    // 1. CORRECCIÓN: El constructor vacío DEBE inicializar las propiedades de JavaFX
    public Producto() {
        this.idProducto = new SimpleIntegerProperty();
        this.nombre = new SimpleStringProperty();
        this.cantidad = new SimpleIntegerProperty();
        this.precio = new SimpleDoubleProperty();
        this.codigoBarra = new SimpleStringProperty();
    }

    public static Integer idProveedorParaAsociar = null;
    public static boolean debeAsociarProveedor = false;

    // 2. CORRECCIÓN: Mapeamos los nombres que vienen de la API de Spring Boot
    @JsonAlias({"id", "idProducto"})
    private final IntegerProperty idProducto;

    private final StringProperty nombre;

    @JsonAlias({"stock", "cantidad"})
    private final IntegerProperty cantidad;

    private final DoubleProperty precio;

    @JsonAlias({"codigoBarra", "codigoBarras"})
    private final StringProperty codigoBarra;

    public static Producto productoSeleccionadoParaEditar = null;

    // Constructor
    public Producto(int idProducto, String nombre, Integer cantidad, double precio, String codigoBarra) {
        this.idProducto = new SimpleIntegerProperty(idProducto);;
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
    }

    // Constructor
    public Producto(String nombre, Integer cantidad, double precio, String codigoBarra) {
        //this.idProducto = new SimpleIntegerProperty(idProductoProperty().getValue());
        this.idProducto = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
    }

    public Producto(String nuevonombre, Integer nuevacantidad, Double nuevoPrecio, IntegerProperty idProducto, StringProperty nombre, IntegerProperty cantidad, DoubleProperty precio, StringProperty codigoBarra) {
        this.idProducto = idProducto;

        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.codigoBarra = codigoBarra;
    }



    // Getters de Propiedades (Requeridos por TableView)
    public IntegerProperty idProductoProperty() { return idProducto; }
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoBarraProperty() { return codigoBarra; }

    // Getters ordinarios
    public Integer getidProducto() { return idProducto.get(); }
    public String getNombre() { return nombre.get(); }
    public Integer getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public String getCodigoBarra() { return codigoBarra.get(); }

    // Setters ordinarios
    public void setidProducto(int idProducto) { this.idProducto.set(idProducto);}
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setCantidad(Integer cantidad) { this.cantidad.set(cantidad); }
    public void setPrecio(double precio) { this.precio.set(precio); }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra.set(codigoBarra); }

}



