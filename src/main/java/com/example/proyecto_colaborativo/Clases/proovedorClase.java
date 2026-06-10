package com.example.proyecto_colaborativo.Clases;

public class proovedorClase extends entidadClase {
    public proovedorClase (){
        super();
    }
    public proovedorClase(String nombre, String telefono, String email, String direccion, String cuitcuil) {
        super();
        this.setNombreEntidad(nombre);
        this.setTelefonoEntidad(telefono);
        this.setEmailEntidad(email);
        this.setDireccionEntidad(direccion);
        this.setCuitcuilEntidad(cuitcuil);

    }
}
