package com.example.proyecto_colaborativo;

public class Producto {

    String NombreProducto;
    String Descripcion;
    Integer Precio;
    Integer Cantidad;
    Integer IdCodigo;
    String CodigoBarras;

    public Producto(String nombreProducto, String descripcion, Integer precio, Integer cantidad, Integer idCodigo, String codigoBarras) {
        NombreProducto = nombreProducto;
        Descripcion = descripcion;
        Precio = precio;
        Cantidad = cantidad;
        IdCodigo = idCodigo;
        CodigoBarras = codigoBarras;
    }
    //Nombre Producto
    public String getNombreProducto() {
        return NombreProducto;
    }
    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    //Descripcion
    public String getDescripcion() {
        return Descripcion;
    }
    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    //Precio
    public Integer getPrecio() {
        return Precio;
    }
    public void setPrecio(Integer precio) {
        Precio = precio;
    }

    //Cantidad
    public Integer getCantidad() {
        return Cantidad;
    }
    public void setCantidad(Integer cantidad) {
        Cantidad = cantidad;
    }

    //Codigo Producto
    public Integer getIdCodigo() {
        return IdCodigo;
    }
    public void setIdCodigo(Integer idCodigo) {
        IdCodigo = idCodigo;
    }

    //Codigo de Barra
    public String getCodigoBarras() {
        return CodigoBarras;
    }
    public void setCodigoBarras(String codigoBarras) {
        CodigoBarras = codigoBarras;
    }
}
