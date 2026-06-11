package com.example.proyecto_colaborativo.bd;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    //url de su archivo de db
    private static final String URL = "jdbc:sqlite:C:/Users/elsez/Documents/ventas.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException("Error conexión SQLite", e);
        }
    }
}
