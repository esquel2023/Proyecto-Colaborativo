package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.clienteClase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static List<clienteClase> listar(){
        List<clienteClase> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nombre";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                clienteClase p = new clienteClase();
                p.setId(rs.getInt("idCliente"));
                p.setNombreEntidad(rs.getString("nombre"));
                p.setDniEntidad(rs.getString("dni"));
                p.setTelefonoEntidad(rs.getString("telefono"));
                p.setEmailEntidad(rs.getString("email"));
                p.setCuitcuilEntidad(rs.getString("cuitcuil"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(clienteClase p) {
        String sql = "INSERT INTO cliente (nombre, dni, telefono, email, cuitcuil) VALUES(?,?,?,?,?)";
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
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("cuitcuil")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(clienteClase p) throws SQLException {
        String sql = """
        UPDATE cliente
        SET nombre=?, dni=?, telefono=?, email=?, cuitcuil=?
        WHERE idCliente=?;
    """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getDniEntidad());
            ps.setString(3, p.getTelefonoEntidad());
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
        String sql = "DELETE FROM cliente WHERE nombre=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre); // Ahora acepta texto correctamente
            ps.executeUpdate();
        }
    }
}
