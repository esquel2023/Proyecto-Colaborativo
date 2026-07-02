package com.example.proyecto_colaborativo.bd;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoProveedorDAO {


    public static List<Producto> listar(int idProveedor) {
        List<Producto> lista = new ArrayList<>();
        // MODIFICADO: Seleccionamos los datos del producto y ADEMÁS el precio de costo de la tabla intermedia
        String sql = "SELECT p.idproducto, p.nombre, p.cantidad, p.CodigoDeBarra, pp.precioCosto " +
                "FROM Producto p " +
                "INNER JOIN ProductoProvedor pp ON p.idproducto = pp.idProducto " +
                "WHERE pp.idProveedor = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("idproducto"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precioCosto"),
                            rs.getString("CodigoDeBarra")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    // Para el botón Agregar: Inserta una nueva fila en la tabla intermedia
    public static void asociar(int idProducto, int idProveedor, double precioCosto) {
        String sql = "INSERT INTO ProductoProvedor (idProducto, idProveedor, precioCosto) VALUES (?, ?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idProveedor);
            ps.setDouble(3, precioCosto);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Para el botón Eliminar: Borra la fila de la relación sin borrar el producto real
    public static void desasociar(int idProducto, int idProveedor) {
        String sql = "DELETE FROM ProductoProvedor WHERE idProducto = ? AND idProveedor = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idProveedor);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void actualizarPrecioCosto(int idProducto, int idProveedor, double nuevoCosto) {
        String sql = "UPDATE ProductoProvedor SET precioCosto = ? WHERE idProducto = ? AND idProveedor = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDouble(1, nuevoCosto);
            ps.setInt(2, idProducto);
            ps.setInt(3, idProveedor);
            ps.executeUpdate();
            System.out.println("¡Precio de costo actualizado en la BD!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



