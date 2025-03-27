package com.dev.supermarkets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:8889/stock_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Charger le driver MySQL
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver JDBC introuvable !");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données !");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion !");
                e.printStackTrace();
            }
        }
    }
}
