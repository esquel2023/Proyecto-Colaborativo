package com.example.proyecto_colaborativo.bd;

import com.example.proyecto_colaborativo.Clases.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAO {
    public static List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setID(rs.getInt("ID"));
                p.setNombre(rs.getString("Nombre"));
                p.setPrecio(rs.getInt("Precio"));
                p.setCantidad(rs.getInt("Cantidad"));
                p.setCodigoTabla(rs.getInt("Codigo"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void insertar(Producto p) {
        String sql = "INSERT INTO Producto(Proveedor, Nombre, Precio,Cantidad,Codigo) VALUES(?,?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, 1);
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getPrecio());
            ps.setInt(4, p.getCantidad());
            ps.setInt(5, p.getCodigo());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Producto buscarPorId(int id) {
        String sql = "SELECT * FROM Producto WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
