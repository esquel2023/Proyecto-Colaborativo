package com.example.proyecto_colaborativo.bd;

import com.example.proyecto_colaborativo.Clases.Producto;
import com.example.proyecto_colaborativo.Clases.clienteClase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoProveedorDAO {


    public static List<Producto> listar(int idProveedor) {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT p.* FROM Productos p " +
                "INNER JOIN ProductoProveedor pp ON p.id = pp.idproducto " +
                "WHERE pp.idproveedor = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getDouble("Precio"),
                            rs.getString("CodigoDeBarra")
                    );

                    lista.add(p);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
    // Para el botón Agregar: Inserta una nueva fila en la tabla intermedia
    public static void asociar(int idProducto, int idProveedor) {
        String sql = "INSERT INTO ProductoProveedor (idproducto, idproveedor) VALUES (?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idProveedor);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Para el botón Eliminar: Borra la fila de la relación sin borrar el producto real
    public static void desasociar(int idProducto, int idProveedor) {
        String sql = "DELETE FROM ProductoProveedor WHERE idproducto = ? AND idproveedor = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idProveedor);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



