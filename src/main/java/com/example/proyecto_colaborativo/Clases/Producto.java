package com.example.proyecto_colaborativo.Clases;

public class Producto {

    private Integer ID;
    private  String nombre;
    private  Integer precio;
    private  Integer cantidad; // Si la cantidad lleva letras (ej: "10 kg"), usá String. Si es entera, podés usar SimpleIntegerProperty.
    private  Integer codigo;



    public Producto(String nombre, Integer precio, Integer cantidad, Integer codigo) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.codigo = codigo;
    }

    public Producto() {

    }

    public Integer getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setCodigoTabla(Integer codigoTabla) {
        this.codigo = codigoTabla;
    }

}

