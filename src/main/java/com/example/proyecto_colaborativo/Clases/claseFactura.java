package com.example.proyecto_colaborativo.Clases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class claseFactura {
    private List<Producto> listaProductos;
    private double precioFinal;     // <-- El total de la suma de todos los productos
    private String metodoDePago;
    private String cliente;
    private String fechaEmison;
    private String numeroDeFactura;
    private String DNI;
}