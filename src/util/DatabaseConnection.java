package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/reservasi_perpus";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver (optional for newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi Database Berhasil!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Koneksi Database Gagal: " + e.getMessage());
        }
        return connection;
    }
}
