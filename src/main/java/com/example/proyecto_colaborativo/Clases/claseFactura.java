package com.example.proyecto_colaborativo.Clases;

public class claseFactura {
    private String producto;
    private double precioFinal;
    private double precioUnitario;
    private double cantidad;
    private String metodoDePago;
    private String cliente;
    private String fechaEmison;
    private String numeroDeFactura;
    private String DNI;

    public claseFactura(String numeroDeFactura, String cliente, String fechaEmision,
                        String producto, double cantidad, double precioUnitario, String metodoDePago ,String DNI) {
        this.producto = producto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.metodoDePago = metodoDePago;
        this.numeroDeFactura = numeroDeFactura;
        this.cliente = cliente;
        this.fechaEmison = fechaEmision;
        this.DNI = DNI;

        if (cantidad <= 0) {
            this.cantidad = 1.0;
        } else {
            this.cantidad = cantidad;
        }

        calcularPrecioFinal();
    }

    private void calcularPrecioFinal() {
        this.precioFinal = this.precioUnitario * this.cantidad;
    }

    public void setCantidad(double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        } else {
            this.cantidad = cantidad;
        }
        calcularPrecioFinal();
    }
    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precioUnitario = precioUnitario;
        calcularPrecioFinal();
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

public String getProducto() { return producto; }
public void setProducto(String producto) { this.producto = producto; }

public double getCantidad() { return cantidad; }

public String getMetodoDePago() { return metodoDePago; }
public void setMetodoDePago(String metodoDePago) { this.metodoDePago = metodoDePago; }

public String getCliente() { return cliente; }
public void setCliente(String cliente) { this.cliente = cliente; }

public String getFechaEmison() { return fechaEmison; }
public void setFechaEmison(String fechaEmison) { this.fechaEmison = fechaEmison; }

public String getNumeroDeFactura() { return numeroDeFactura; }
public void setNumeroDeFactura(String numeroDeFactura) { this.numeroDeFactura = numeroDeFactura; }

    public String getDNI() { return DNI; }
    public void setDNI(String DNI) { this.DNI = DNI; }
}
