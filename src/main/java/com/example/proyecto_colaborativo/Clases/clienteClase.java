package com.example.proyecto_colaborativo.Clases;

public class clienteClase extends entidadClase {

 private int id;

    public clienteClase() {

    }
    // Constructor por defecto


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Constructor completo adaptado a los métodos de eliseoClase
    public clienteClase(int idcliente, String nombre, String dni, String telefono, String email, String direccion, String cuitcuil) {
        super();
        this.setId(idcliente);
        this.setNombreEntidad(nombre);
        this.setDniEntidad(dni);
        this.setTelefonoEntidad((telefono));
        this.setEmailEntidad(email);
        this.setDireccionEntidad(direccion);
        this.setCuitcuilEntidad(cuitcuil);

    }



}

