package com.name.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String username = (String) request.getSession().getAttribute("username");
            if (username == null) {
                response.sendRedirect("index.html");
                return;
        }
        int productId = Integer.parseInt(request.getParameter("product_id"));
                try (Connection conn = Db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO cart_items (username, product_id, quantity) VALUES (?, ?, 1)");
            stmt.setString(1, username);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
            response.sendRedirect("products.html");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        try (Connection conn = Db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT products.name, products.price, cart_items.quantity FROM cart_items JOIN products ON cart_items.product_id = products.id WHERE cart_items.username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
                        StringBuilder json = new StringBuilder("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    json.append(",");
                }
                json.append("{\"name\":\"").append(rs.getString("name")).append("\",");
                json.append("\"price\":").append(rs.getDouble("price")).append(",");
                json.append("\"quantity\":").append(rs.getInt("quantity")).append("}");
                first = false;
            }
            json.append("]");
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
