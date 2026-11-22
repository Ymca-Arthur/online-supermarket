package com.name.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    // Show the cart
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        List<Map<String, Object>> items = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT products.name, products.price, cart_items.quantity FROM cart_items JOIN products ON cart_items.product_id = products.id WHERE cart_items.username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", rs.getString("name"));
                item.put("price", rs.getDouble("price"));
                item.put("quantity", rs.getInt("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    // Add to cart
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        int productId = Integer.parseInt(request.getParameter("product_id"));
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO cart_items (username, product_id, quantity) VALUES (?, ?, 1)");
            stmt.setString(1, username);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
