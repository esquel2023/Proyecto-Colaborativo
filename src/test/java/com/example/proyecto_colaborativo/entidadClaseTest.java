package com.example.proyecto_colaborativo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class entidadClaseTest {

    @Test
    void getNombreEntidad() {
        clienteClase cliente = new clienteClase(" Franco", "2", "7000", "eqrq","234","23");
        assertEquals(
                " Franco",
                cliente.getNombreEntidad()
        );
    }

    @Test
    void getDniEntidad() {
        clienteClase cliente = new clienteClase(" sos", " sad", "7000", "eqrq","234","23");
        assertEquals(
                " sad",
                cliente.getDniEntidad()
        );

    }

    @Test
    void getTelefonoEntidad() {
        clienteClase cliente = new clienteClase(" sos", " sad", "dosnueveucatro", "eqrq","234","23");
        assertEquals(
                "dosnueveucatro",
                cliente.getTelefonoEntidad()
        );
    }

    @Test
    void getDireccionEntidad() {
        clienteClase cliente = new clienteClase(" sos", " sad", "dosnueveucatro", "eqrq","roca123","23");
        assertEquals(
                "roca123",
                cliente.getDireccionEntidad()
        );
    }

    @Test
    void getCuitcuilEntidad() {
        clienteClase cliente = new clienteClase(" sos", " sad", "dosnueveucatro", "eqrq","","23467");
        assertEquals(
                "23467",
                cliente.getCuitcuilEntidad()
        );
    }

    @Test
    void getEmailEntidad() {
        clienteClase cliente = new clienteClase(" sos", " sad", "dosnueveucatro", "eqrq","","23467");
        assertEquals(
                "eqrq",
                cliente.getEmailEntidad()
        );

    }
}