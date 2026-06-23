package com.example.proyecto_colaborativo;

import com.example.proyecto_colaborativo.Clases.Producto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductoTest {

    @Test
    void precioProperty() {
        Producto producto = new  Producto("Alfajor", 2, 7000, "prod001");
        assertEquals(
                7000,
                producto.getPrecio()
        );
    }

    @Test
    void getCantidad() {
        Producto producto = new  Producto("Alfajor", -0, 7000, "prod001");
        assertEquals(
                -0,
                producto.getCantidad()
        );
    }

    @Test
    void nombreProperty() {
        Producto producto = new  Producto("Helado", 2, 7000, "prod002");
        assertEquals(
                "Helado",
                producto.getNombre()
        );

    }

    @Test
    void codigoTablaProperty() {
        Producto producto = new  Producto("Pan", 1, -1000, "prod003");
        assertEquals(
                -1000,
                producto.getPrecio()
        );
    }

}