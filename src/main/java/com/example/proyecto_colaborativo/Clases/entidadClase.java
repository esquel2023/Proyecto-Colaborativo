package com.example.proyecto_colaborativo.Clases;


public abstract class entidadClase {

    private String nombreEntidad;
    private String telefonoEntidad;
    private String dniEntidad;
    private String emailEntidad;
    private String cuitcuilEntidad;
    private String tipoIdentificacion;
    private String condicionIva;
    private String pais;
    private String provincia;
    private String ciudad;

    public entidadClase() {
    }

    // GETTERS Y SETTERS EXISTENTES
    public String getNombreEntidad() { return nombreEntidad; }
    public void setNombreEntidad(String nombreEntidad) { this.nombreEntidad = nombreEntidad; }

    public String getTelefonoEntidad() { return telefonoEntidad; }
    public void setTelefonoEntidad(String telefonoEntidad) { this.telefonoEntidad = telefonoEntidad; }

    public String getDniEntidad() { return dniEntidad; }
    public void setDniEntidad(String dniEntidad) { this.dniEntidad = dniEntidad; }

    public String getEmailEntidad() { return emailEntidad; }
    public void setEmailEntidad(String emailEntidad) { this.emailEntidad = emailEntidad; }

    public String getCuitcuilEntidad() { return cuitcuilEntidad; }
    public void setCuitcuilEntidad(String cuitcuilEntidad) { this.cuitcuilEntidad = cuitcuilEntidad; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getCondicionIva() { return condicionIva; }
    public void setCondicionIva(String condicionIva) { this.condicionIva = condicionIva; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
}
