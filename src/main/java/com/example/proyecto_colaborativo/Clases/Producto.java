package com.example.proyecto_colaborativo.Clases;

import com.fasterxml.jackson.annotation.JsonAlias;
import javafx.beans.property.*;

public class Producto {

    // Campos finales (Las propiedades de JavaFX se inicializan siempre)
    private final IntegerProperty idProducto = new SimpleIntegerProperty(0);
    private final StringProperty nombre = new SimpleStringProperty("");
    private final IntegerProperty cantidad = new SimpleIntegerProperty(0);
    private final DoubleProperty precio = new SimpleDoubleProperty(0.0);
    private final StringProperty codigoBarra = new SimpleStringProperty("");

    public static Integer idProveedorParaAsociar = null;
    public static boolean debeAsociarProveedor = false;
    public static Producto productoSeleccionadoParaEditar = null;

    // Constructor vacío requerido por Jackson
    public Producto() {
        // Al estar inicializados arriba, no hace falta repetir el 'new' acá
    }

    // Constructor con ID
    public Producto(Integer idProducto, String nombre, Integer cantidad, Double precio, String codigoBarra) {
        setidProducto(idProducto);
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecio(precio);
        setCodigoBarra(codigoBarra);
    }

    // Constructor sin ID
    public Producto(String nombre, Integer cantidad, Double precio, String codigoBarra) {
        setidProducto(0);
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecio(precio);
        setCodigoBarra(codigoBarra);
    }

    // Constructor alternativo
    public Producto(String nuevonombre, Integer nuevacantidad, Double nuevoPrecio, IntegerProperty idProducto, StringProperty nombre, IntegerProperty cantidad, DoubleProperty precio, StringProperty codigoBarra) {
        setidProducto(idProducto != null ? idProducto.get() : 0);
        setNombre(nuevonombre != null ? nuevonombre : (nombre != null ? nombre.get() : ""));
        setCantidad(nuevacantidad != null ? nuevacantidad : (cantidad != null ? cantidad.get() : 0));
        setPrecio(nuevoPrecio != null ? nuevoPrecio : (precio != null ? precio.get() : 0.0));
        setCodigoBarra(codigoBarra != null ? codigoBarra.get() : "");
    }

    // ==========================================
    // GETTERS DE PROPIEDADES (Para TableView)
    // ==========================================
    public IntegerProperty idProductoProperty() { return idProducto; }
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoBarraProperty() { return codigoBarra; }

    // ==========================================
    // GETTERS ORDINARIOS
    // ==========================================
    public Integer getidProducto() { return idProducto.get(); }
    public String getNombre() { return nombre.get(); }
    public Integer getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public String getCodigoBarra() { return codigoBarra.get(); }

    // ==========================================
    // SETTERS CON PROTECCIÓN CONTRA NULOS (Jackson usará estos)
    // ==========================================
    @JsonAlias({"id", "idProducto"})
    public void setidProducto(Integer idProducto) {
        this.idProducto.set(idProducto != null ? idProducto : 0);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre != null ? nombre : "");
    }

    @JsonAlias({"stock", "cantidad"})
    public void setCantidad(Integer cantidad) {
        // ✅ Si la API manda null, se transforma en 0 de forma segura sin romper JavaFX
        this.cantidad.set(cantidad != null ? cantidad : 0);
    }

    public void setPrecio(Double precio) {
        this.precio.set(precio != null ? precio : 0.0);
    }

    @JsonAlias({"codigoBarra", "codigoBarras"})
    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra.set(codigoBarra != null ? codigoBarra : "");
    }
}
