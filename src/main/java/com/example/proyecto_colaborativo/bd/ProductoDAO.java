package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto ORDER BY idProducto DESC";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("PrecioCosto"),
                        rs.getString("CodigoBarra"));

                lista.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;

    }

    public static void insertar(Producto p) {
        String sql = "INSERT INTO Producto(Nombre, PrecioCosto, Cantidad, CodigoBarra) VALUES(?,?,?,?)";

        // Añadimos Statement.RETURN_GENERATED_KEYS para capturar el ID que cree la base de datos
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            //ps.setString(1, p.getNombre());
            ps.setString(1, p.getNombre());
            //ps.setInt(2, (int) p.getPrecio());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getCodigoBarra());
            ps.executeUpdate();

            // Recuperamos el ID autogenerado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    p.setidProducto(idGenerado);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Producto buscarPorId(int id) {
        String sql = "SELECT * FROM Producto WHERE idProducto = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
           try (ResultSet rs = ps.executeQuery()) {



            if (rs.next()) {
                return new Producto(

                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("PrecioCosto"),
                        rs.getString("codigoBarra")
                       // rs.getInt("id"),
                );
            }

           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(Producto p) throws SQLException {
        String sql = """
            UPDATE Producto
            SET Nombre=?, PrecioCosto=?,Cantidad=? ,CodigoBarra=?
            WHERE idProducto=? ;
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getCodigoBarra());
            ps.setInt(5, p.getidProducto());
            //ps.setString(5, p.getCodigoTabla());

            ps.executeUpdate();
        }
    }

    public static void eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM Producto WHERE idProducto =?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
        }
    }
}
