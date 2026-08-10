package com.example.proyecto_colaborativo.Clases;

import javafx.beans.property.*;

public class Producto {
    // 💡 Las declaramos finales para proteger la referencia de la propiedad
    private final IntegerProperty idProducto;
    private final StringProperty nombre;
    private final IntegerProperty cantidad;
    private final DoubleProperty precio;
    private final StringProperty codigoBarra;
    private final BooleanProperty activado;

    public static Producto productoSeleccionadoParaEditar = null;

    // ✅ Constructor Completo Optimizado (Para lectura de BD)
    public Producto(int idProducto, String nombre, int cantidad, double precio, String codigoBarra, boolean activado) {
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
        this.activado = new SimpleBooleanProperty(activado);
    }

    // ✅ Constructor Sin ID Optimizado (Para nuevas inserciones)
    public Producto(String nombre, int cantidad, double precio, String codigoBarra, boolean activado) {
        this.idProducto = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.codigoBarra = new SimpleStringProperty(codigoBarra);
        this.activado = new SimpleBooleanProperty(activado);
    }

    // Getters de Propiedades (Esenciales para que el TableView se entere de los cambios en tiempo real)
    public IntegerProperty idProductoProperty() { return idProducto; }
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public StringProperty codigoBarraProperty() { return codigoBarra; }
    public BooleanProperty activadoProperty() { return activado; }

    // Getters ordinarios
    public int getidProducto() { return idProducto.get(); }
    public String getNombre() { return nombre.get(); }
    public int getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public String getCodigoBarra() { return codigoBarra.get(); }
    public boolean isActivado() { return activado.get(); }

    // Setters ordinarios
    public void setidProducto(int idProducto) { this.idProducto.set(idProducto); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setCantidad(int cantidad) { this.cantidad.set(cantidad); }
    public void setPrecio(double precio) { this.precio.set(precio); }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra.set(codigoBarra); }
    public void setActivado(boolean activado) { this.activado.set(activado); }
}
