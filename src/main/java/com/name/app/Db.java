package com.name.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    public static Connection getConnection() throws SQLException {
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "1234");
    }
}
