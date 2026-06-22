package com.example.proyecto_colaborativo.bd;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    //url de su archivo de db
    private static final String URL = "jdbc:sqlite:C:/Users/opcd10/Downloads/SQLiteDatabaseBrowserPortable/Data/settings/Sistadeventas.db";

    public static Connection getConnection() {
        try {
            System.out.println("Conectando a BD");
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException("Error conexión SQLite", e);
        }
    }
}
