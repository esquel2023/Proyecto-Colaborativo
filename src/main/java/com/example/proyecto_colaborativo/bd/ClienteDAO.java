package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.clienteClase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static List<clienteClase> listar(){
        List<clienteClase> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY apellido";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                clienteClase p = new clienteClase();
                p.setNombreEntidad(rs.getString("nombre"));
                p.setDniEntidad(rs.getString("dni"));
                p.setTelefonoEntidad(String.valueOf(rs.getInt("telefono")));
                p.setEmailEntidad(rs.getString("email"));
                p.setCuitcuilEntidad(rs.getString("apellido"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(clienteClase p) {
        String sql = "INSERT INTO cliente(nombre, apellido, dni, telefono, email) VALUES(?,?,?,?,?)";
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
    public static clienteClase buscarNombre(String nombre) {
        String sql = "SELECT * FROM paciente WHERE nombre=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(nombre));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new clienteClase(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("cuit")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(clienteClase p) throws SQLException {
        // CORREGIDO: Se eliminó 'direccion' para que coincida con las columnas reales de tu tabla
        String sql = """
        UPDATE cliente
        SET nombre=?, dni=?, telefono=?, email=?, apellido=?
        WHERE nombre=?
    """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getDniEntidad());
            ps.setString(3, p.getTelefonoEntidad());
            ps.setString(4, p.getEmailEntidad());
            ps.setString(5, p.getCuitcuilEntidad()); // Recuerda que esto se mapea a 'apellido' en tu BD
            ps.setString(6, p.getNombreEntidad());    // Condición WHERE

            ps.executeUpdate();
        }
    }



    public static void eliminar(String nombre) throws SQLException {
        String sql = "DELETE FROM cliente WHERE nombre=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre); // Ahora acepta texto correctamente
            ps.executeUpdate();
        }
    }
}
