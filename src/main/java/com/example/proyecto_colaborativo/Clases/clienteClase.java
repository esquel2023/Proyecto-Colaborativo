package com.example.proyecto_colaborativo.Clases;

public class clienteClase extends entidadClase {

    private int id;

    // Constructor por defecto (Obligatorio para JavaFX y DAO)
    public clienteClase() {
        super();
    }

    // Constructor completo adaptado con los nuevos campos de la interfaz
    public clienteClase(int idcliente, String nombre, String dni, String telefono, String email,
                        String cuitcuil, String tipoIdentificacion,
                        String condicionIva, String pais, String provincia, String ciudad) {
        super();
        this.setId(idcliente);
        this.setNombreEntidad(nombre);
        this.setDniEntidad(dni);
        this.setTelefonoEntidad(telefono);
        this.setEmailEntidad(email);
        this.setCuitcuilEntidad(cuitcuil);

        // CORRECCIÓN: Se asignan usando los setters heredados de entidadClase
        this.setTipoIdentificacion(tipoIdentificacion);
        this.setCondicionIva(condicionIva);
        this.setPais(pais);
        this.setProvincia(provincia);
        this.setCiudad(ciudad);
    }

    // EL ÚNICO GETTER Y SETTER PROPIO QUE DEBE QUEDAR ES EL DEL ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
