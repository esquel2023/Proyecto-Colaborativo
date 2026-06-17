package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.claseFactura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public static List<claseFactura> listar() {
        List<claseFactura> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente ORDER BY apellido";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                claseFactura p = new claseFactura();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setEdad(rs.getInt("edad"));
                p.setObraSocial(rs.getString("obra_social"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(claseFactura p) {
        String sql = "INSERT INTO paciente(nombre, apellido, edad, obra_social) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setInt(3, p.getEdad());
            ps.setString(4, p.getObraSocial());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static claseFactura buscarPorId(int id) {
        String sql = "SELECT * FROM paciente WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new claseFactura(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("edad"),
                        rs.getDouble(""),
                        rs.getDouble("id"),
                        rs.getString("nombre"),
                        rs.getString("nombre")


                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(claseFactura p) throws SQLException {
        String sql = """
            UPDATE paciente
            SET nombre=?, apellido=?, edad=?, obra_social=?
            WHERE id=?
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setInt(3, p.getEdad());
            ps.setString(4, p.getObraSocial());
            ps.setInt(5, p.getId());

            ps.executeUpdate();
        }
    }

    public static void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
