package com.example.proyecto_colaborativo.Clases;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Producto {

    public static Integer idProveedorParaAsociar = null;
    public static boolean debeAsociarProveedor = false;
    public static Producto productoSeleccionadoParaEditar = null;

    private final IntegerProperty idProducto;
    private final StringProperty nombre;
    private final IntegerProperty cantidad;
    private final DoubleProperty precio;
    private final StringProperty codigoBarra;

    // =========================================================================
    // 1. CONSTRUCTOR VACÍO - CORREGIDO CON @JsonCreator
    // Fuerza a Jackson a usar este constructor e inicializar las Properties
    // =========================================================================
    @JsonCreator
    public Producto() {
        this.idProducto = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty("");
        this.cantidad = new SimpleIntegerProperty(0);
        this.precio = new SimpleDoubleProperty(0.0);
        this.codigoBarra = new SimpleStringProperty("");
    }

    // =========================================================================
    // CONSTRUCTORES EXISTENTES
    // =========================================================================

    public Producto(IntegerProperty idProducto, IntegerProperty cantidad, DoubleProperty precio, StringProperty nombre, StringProperty codigoBarra) {
        this.idProducto = new SimpleIntegerProperty(idProducto != null ? idProducto.get() : 0);
        this.cantidad = new SimpleIntegerProperty(cantidad != null ? cantidad.get() : 0);
        this.precio = new SimpleDoubleProperty(precio != null ? precio.get() : 0.0);
        this.nombre = new SimpleStringProperty(nombre != null ? nombre.get() : "");
        this.codigoBarra = new SimpleStringProperty(codigoBarra != null ? codigoBarra.get() : "");
    }

    public Producto(int idProducto, String nombre, Integer cantidad, double precio, String codigoBarra) {
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad != null ? cantidad : 0);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
    }

    public Producto(String nombre, Integer cantidad, double precio, String codigoBarra) {
        this.idProducto = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad != null ? cantidad : 0);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
    }

    public Producto(String nuevonombre, Integer nuevacantidad, Double nuevoPrecio, IntegerProperty idProducto, StringProperty nombre, IntegerProperty cantidad, DoubleProperty precio, StringProperty codigoBarra) {
        this.idProducto = idProducto != null ? idProducto : new SimpleIntegerProperty(0);
        this.nombre = nombre != null ? nombre : new SimpleStringProperty(nuevonombre);
        this.cantidad = cantidad != null ? cantidad : new SimpleIntegerProperty(nuevacantidad != null ? nuevacantidad : 0);
        this.precio = precio != null ? precio : new SimpleDoubleProperty(nuevoPrecio != null ? nuevoPrecio : 0.0);
        this.codigoBarra = codigoBarra != null ? codigoBarra : new SimpleStringProperty("");
    }

    // =========================================================================
    // GETTERS DE PROPIEDADES (Requeridos por TableView de JavaFX)
    // =========================================================================
    public IntegerProperty idProductoProperty() { return idProducto; }
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoBarraProperty() { return codigoBarra; }

    // =========================================================================
    // GETTERS Y SETTERS TRADICIONALES - CORREGIDOS CON "id"
    // =========================================================================

    @JsonProperty("id") // ◄ CORRECCIÓN: Cambiado de "idProducto" a "id" para sintonizar con Spring Boot
    public Integer getidProducto() { return idProducto.get(); }

    @JsonProperty("id") // ◄ CORRECCIÓN: Cambiado de "idProducto" a "id" para sintonizar con Spring Boot
    public void setidProducto(int idProducto) { this.idProducto.set(idProducto); }

    @JsonProperty("nombre")
    public String getNombre() { return nombre.get(); }

    @JsonProperty("nombre")
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    @JsonProperty("cantidad")
    public Integer getCantidad() { return cantidad.get(); }

    @JsonProperty("cantidad")
    public void setCantidad(Integer cantidad) { this.cantidad.set(cantidad != null ? cantidad : 0); }

    @JsonProperty("precio")
    public double getPrecio() { return precio.get(); }

    @JsonProperty("precio")
    public void setPrecio(double precio) { this.precio.set(precio); }

    @JsonProperty("codigoBarra")
    public String getCodigoBarra() { return codigoBarra.get(); }

    @JsonProperty("codigoBarra")
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra.set(codigoBarra); }
}
