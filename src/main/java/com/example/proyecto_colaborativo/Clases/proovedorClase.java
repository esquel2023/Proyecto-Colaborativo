package com.example.proyecto_colaborativo.Clases;

public class proovedorClase extends entidadClase {
    private int id;

    public proovedorClase(int idcliente, String nombre, String dni, String telefono, String email, String direccion, String cuit) {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public proovedorClase (){
        super();
    }
    public proovedorClase(int idproveedor, String nombre, String telefono, String email, String direccion, String cuitcuil) {
        super();
        this.setId(idproveedor);
        this.setNombreEntidad(nombre);
        this.setTelefonoEntidad((telefono));
        this.setEmailEntidad(email);
        this.setDireccionEntidad(direccion);
        this.setCuitcuilEntidad(cuitcuil);

    }
}
