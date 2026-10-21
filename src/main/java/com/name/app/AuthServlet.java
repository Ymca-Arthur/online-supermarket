package com.name.app;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try (Connection conn = Db.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT is_admin FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                if (rs.getBoolean("is_admin")){
                    response.sendRedirect("admin.html");
                } else {
                    response.sendRedirect("products.html");
                }
            }else{
                response.getWriter().write("wrong username or password.");
            }
                


        } catch (SQLException e){
            throw new ServletException(e);
        }   
    }
}
