package com.name.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class DbTest {
    public static void main(String[] args) throws Exception {
        Connection conn =   DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "1234");
        System.out.println("connected");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1");
        rs.next();
        System.out.println("Query result: " + rs.getInt(1));
    }
}
