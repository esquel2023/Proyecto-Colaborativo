package com.example.proyecto_colaborativo.bd;


import com.example.proyecto_colaborativo.Clases.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto ORDER BY nombre";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                //p.setId(rs.getInt("id"));
               // p.setNombre(rs.getString("nombre")),
               // p.setCantidad(rs.getInt("cantidad")),
               // p.setPrecio(rs.getDouble("precio")),
               // p.setCodigoTabla(rs.getString("codigoTabla"))
                        rs.getString("nombre"),
                        rs.getInt("Precio"),
                        rs.getInt("cantidad"),
                        rs.getString("codigoDeBarra"));

                lista.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(Producto p) {
        String sql = "INSERT INTO Producto(Nombre, Precio, codigoDeBarra, cantidad) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            //ps.setString(1, p.getNombre());
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCodigoTabla());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Producto buscarPorId(int id) {
        String sql = "SELECT * FROM Producto WHERE id_Producto =?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Producto(

                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getInt("PrecioCosto"),
                        rs.getString("codigoDeBarra")
                       // rs.getInt("id"),
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void actualizar(Producto p) throws SQLException {
        String sql = """
            UPDATE Producto
            SET Nombre=?, Precio=?,codigoDeBarra=? ,cantidad=?                                                        //*, obra_social=?*/
            WHERE codigoDeBarra=?;
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getCodigoTabla());
            ps.setInt(4, p.getCantidad());
            ps.setString(5, p.getCodigoTabla());

            ps.executeUpdate();
        }
    }

    public static void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Producto WHERE id=?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
