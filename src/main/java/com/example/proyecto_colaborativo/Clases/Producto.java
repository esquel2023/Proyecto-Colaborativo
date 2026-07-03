package com.example.proyecto_colaborativo.Clases;

import javafx.beans.property.*;

public class Producto {
    // Propiedades nativas de JavaFX para enlace reactivo (Binding)
    private final IntegerProperty idProducto = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty();
    private final DoubleProperty precio = new SimpleDoubleProperty();
    private final StringProperty codigoBarra = new SimpleStringProperty();
    private final BooleanProperty activado = new SimpleBooleanProperty();

    // Puente estático para transferir datos de forma segura entre ventanas
    public static Producto productoSeleccionadoParaEditar = null;

    // Constructor completo (Utilizado por el DAO al leer de la BD)
    public Producto(int idProducto, String nombre, int cantidad, double precio, String codigoBarra, boolean activado) {
        setidProducto(idProducto);
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecio(precio);
        setCodigoBarra(codigoBarra);
        setActivado(activado);
    }

    // Constructor sin ID (Utilizado para crear nuevos registros antes de insertar en la BD)
    public Producto(String nombre, int cantidad, double precio, String codigoBarra, boolean activado) {
        setidProducto(0);
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecio(precio);
        setCodigoBarra(codigoBarra);
        setActivado(activado);
    }

    // Getters de Propiedades (Requeridos obligatoriamente por TableView)
    public IntegerProperty idProductoProperty() {
        return idProducto;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public StringProperty codigoBarraProperty() {
        return codigoBarra;
    }

    public BooleanProperty activadoProperty() {
        return activado;
    }

    // Getters ordinarios (Extraen el valor primitivo puro)
    public int getidProducto() {
        return idProducto.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public double getPrecio() {
        return precio.get();
    }

    public String getCodigoBarra() {
        return codigoBarra.get();
    }

    public boolean isActivado() {
        return activado.get();
    }

    // Setters ordinarios (Modifican el valor interno de la propiedad conservando el Binding)
    public void setidProducto(int idProducto) {
        this.idProducto.set(idProducto);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra.set(codigoBarra);
    }

    public void setActivado(boolean activado) {
        this.activado.set(activado);
    }
}
