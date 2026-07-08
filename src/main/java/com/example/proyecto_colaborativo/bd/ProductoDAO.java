package com.example.proyecto_colaborativo.bd;

import com.example.proyecto_colaborativo.Clases.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // ✅ CORREGIDO: Ahora recupera los productos ACTIVOS (activado = 1) para tu pantalla principal
    public static List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE activado = 1 ORDER BY idProducto DESC";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("Nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("Precio"),
                        rs.getString("CodigoDeBarra"),
                        rs.getBoolean("activado")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    // ✅ NUEVO: Carga estrictamente los productos INACTIVOS (activado = 0) para tu nueva pantalla
    public static List<Producto> listarInactivos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE activado = 0 ORDER BY idProducto DESC";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("Nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("Precio"),
                        rs.getString("CodigoDeBarra"),
                        rs.getBoolean("activado")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public static void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO Producto(Nombre, Precio, CodigoDeBarra, cantidad, activado) VALUES(?,?,?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getCodigoBarra());
            ps.setInt(4, p.getCantidad());
            ps.setBoolean(5, p.isActivado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setidProducto(rs.getInt(1));
                }
            }
        }
    }

    public static Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Producto WHERE idProducto = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("idProducto"),
                            rs.getString("Nombre"),
                            rs.getInt("cantidad"),
                            rs.getDouble("Precio"),
                            rs.getString("CodigoDeBarra"),
                            rs.getBoolean("activado")
                    );
                }
            }
        }
        return null;
    }

    // ✅ OPTIMIZADO: Este es tu método oficial y seguro para guardar cambios
    public static void actualizar(Producto p) throws SQLException {
        String sql = """
            UPDATE Producto
            SET Nombre=?, Precio=?, CodigoDeBarra=?, cantidad=?, activado=?
            WHERE idProducto=?;
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getCodigoBarra());
            ps.setInt(4, p.getCantidad());
            ps.setBoolean(5, p.isActivado());
            ps.setInt(6, p.getidProducto());

            ps.executeUpdate();
        }
    }

    public static void eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM Producto WHERE idProducto = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
        }
    }
}
