package com.name.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.name.app.db.DatabaseManager;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("products", loadProducts());
        request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
    }

    // Also used by AdminServlet.
    public static List<Map<String, Object>> loadProducts() throws ServletException {
        List<Map<String, Object>> products = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, name, price, stock FROM products")) {
            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", rs.getInt("id"));
                p.put("name", rs.getString("name"));
                p.put("price", rs.getDouble("price"));
                p.put("stock", rs.getInt("stock"));
                products.add(p);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        return products;
    }
}
