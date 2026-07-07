package com.example.proyecto_colaborativo.bd;

import com.example.proyecto_colaborativo.Clases.proovedorClase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public static List<proovedorClase> listar() {
        List<proovedorClase> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor ORDER BY nombre"; // Ordenado por nombre para consistencia visual

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                proovedorClase p = new proovedorClase();
                p.setId(rs.getInt("idproveedor"));
                p.setNombreEntidad(rs.getString("nombre"));
                p.setTelefonoEntidad(rs.getString("telefono"));
                p.setEmailEntidad(rs.getString("email"));
                p.setCuitcuilEntidad(rs.getString("cuitcuil"));
                p.setCondicionIva(rs.getString("condicion_iva"));
                p.setPais(rs.getString("pais"));
                p.setProvincia(rs.getString("provincia"));
                p.setCiudad(rs.getString("ciudad"));

                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error al listar proveedores:");
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(proovedorClase p) {
        String sql = """
            INSERT INTO proveedor(nombre, telefono, email, cuitcuil, condicion_iva, pais, provincia, ciudad) 
            VALUES(?,?,?,?,?,?,?,?)
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getTelefonoEntidad());
            ps.setString(3, p.getEmailEntidad());
            ps.setString(4, p.getCuitcuilEntidad());
            ps.setString(5, p.getCondicionIva());
            ps.setString(6, p.getPais());
            ps.setString(7, p.getProvincia());
            ps.setString(8, p.getCiudad());

            ps.executeUpdate();
            System.out.println("Proveedor insertado con éxito.");

        } catch (Exception e) {
            System.out.println("Error al insertar proveedor:");
            e.printStackTrace();
        }
    }

    public static proovedorClase buscarPorId(int idProveedor) {
        String sql = "SELECT * FROM proveedor WHERE idproveedor = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new proovedorClase(
                            rs.getInt("idproveedor"),
                            rs.getString("nombre"),
                            rs.getString("cuitcuil"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getString("condicion_iva"),
                            rs.getString("pais"),
                            rs.getString("provincia"),
                            rs.getString("ciudad")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor por ID:");
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(proovedorClase p) throws SQLException {
        String sql = """
            UPDATE proveedor
            SET nombre=?, telefono=?, email=?, cuitcuil=?, condicion_iva=?, pais=?, provincia=?, ciudad=?
            WHERE idproveedor=?;
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombreEntidad());
            ps.setString(2, p.getTelefonoEntidad());
            ps.setString(3, p.getEmailEntidad());
            ps.setString(4, p.getCuitcuilEntidad());
            ps.setString(5, p.getCondicionIva());
            ps.setString(6, p.getPais());
            ps.setString(7, p.getProvincia());
            ps.setString(8, p.getCiudad());

            ps.setInt(9, p.getId());

            int filasAfectadas = ps.executeUpdate();
            System.out.println("Filas actualizadas en la base de datos: " + filasAfectadas);

        } catch (Exception e) {
            System.out.println("Error al actualizar proveedor:");
            e.printStackTrace();
        }
    }

    public static void eliminar(String nombre) throws SQLException {
        String sql = "DELETE FROM proveedor WHERE nombre=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.executeUpdate();
            System.out.println("Proveedor eliminado con éxito.");
        }
    }
}
