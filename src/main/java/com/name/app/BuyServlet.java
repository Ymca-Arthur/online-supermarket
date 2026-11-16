package com.name.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/buy")
public class BuyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        try (Connection conn = Db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE products SET stock = stock - (SELECT SUM(quantity) FROM cart_items WHERE product_id = products.id AND username = ?) WHERE id IN (SELECT product_id FROM cart_items WHERE username = ?)");
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.executeUpdate();
            PreparedStatement clear = conn.prepareStatement("DELETE FROM cart_items WHERE username = ?");
            clear.setString(1, username);
            clear.executeUpdate();
            response.sendRedirect("thanks.html");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
