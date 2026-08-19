package com.example.proyecto_colaborativo.Clases;

public class proovedorClase extends entidadClase {
    private Long id;

    // Constructor por defecto (Obligatorio para JavaFX y el DAO)
    public proovedorClase() {
        super();
    }

    // Constructor completo adaptado a los campos generales de entidadClase
    public proovedorClase(Long idproveedor, String nombre, String cuitcuil, String telefono,
                          String email, String condicionIva, String pais, String provincia, String ciudad) {
        super();
        this.setId(idproveedor);
        this.setNombreEntidad(nombre);
        this.setCuitcuilEntidad(cuitcuil);
        this.setTelefonoEntidad(telefono);
        this.setEmailEntidad(email);

        // Asignación directa a los setters compartidos de la clase padre
        this.setCondicionIva(condicionIva);
        this.setPais(pais);
        this.setProvincia(provincia);
        this.setCiudad(ciudad);
    }

    // GETTER Y SETTER DEL ID ÚNICO
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
