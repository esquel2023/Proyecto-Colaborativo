package com.example.proyecto_colaborativo;

public class clienteClase extends eliseoClase{


    // Constructor por defecto
    public clienteClase() {
        super();

    }

    // Constructor completo adaptado a los métodos de eliseoClase
    public clienteClase(String nombre, String dni, String telefono, String email, String direccion, String cuitcuil) {
        super();
        this.setNombreEntidad(nombre);
        this.setDniEntidad(dni);
        this.setTelefonoEntidad(telefono);
        this.setEmailEntidad(email);
        this.setDireccionEntidad(direccion);
        this.setCuitcuilEntidad(cuitcuil);

    }


    public String getNombre() {

        return "";
    }
}

