package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.clienteClase;
import com.example.proyecto_colaborativo.Clases.proovedorClase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public static List<proovedorClase> listar(){
        List<proovedorClase> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor ORDER BY idproveedor";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                proovedorClase p = new proovedorClase();
                p.setId(rs.getInt("idproveedor"));
                p.setNombreEntidad(rs.getString("nombre"));
                p.setTelefonoEntidad(rs.getString("telefono"));
                p.setEmailEntidad(rs.getString("email"));
                p.setDireccionEntidad(rs.getString("direccion"));
                p.setCuitcuilEntidad(rs.getString("cuil"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(proovedorClase p) {
        String sql = "INSERT INTO proveedor(nombre, telefono, email, direccion, cuil) VALUES(?,?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getDniEntidad());
            ps.setString(3, p.getTelefonoEntidad());
            ps.setString(4, p.getEmailEntidad());
            ps.setString(5, p.getCuitcuilEntidad());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static proovedorClase buscarNombre(String nombre) {
        String sql = "SELECT * FROM proveedor WHERE idproveedor=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(nombre));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new proovedorClase(
                        rs.getInt("idcliente"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("cuil")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(proovedorClase p) throws SQLException {
        String sql = """
        UPDATE proveedor
        SET nombre=?, telefono=?, email=?, direccion=?, cuil=? 
        WHERE idproveedor=?;
    """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getDniEntidad());
            ps.setString(3, String.valueOf(p.getTelefonoEntidad()));
            ps.setString(4, p.getEmailEntidad());
            ps.setString(5, p.getCuitcuilEntidad());
            ps.setInt(6,p.getId());


            int filasAfectadas = ps.executeUpdate();
            System.out.println("Filas actualizadas en la base de datos: " + filasAfectadas);

        } catch (Exception e) {
            System.out.println("Error al actualizar:");
            e.printStackTrace();
        }
    }




    public static void eliminar(String nombre) throws SQLException {
        String sql = "DELETE FROM proveedor WHERE nombre=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre); // Ahora acepta texto correctamente
            ps.executeUpdate();
        }
    }
}

