package com.name.app.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Gatekeeper: runs before these URLs. Not logged in -> /login. Not admin -> can't open admin pages.
@WebFilter(urlPatterns = {"/products", "/cart", "/buy", "/thanks", "/admin", "/delete"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String ctx = request.getContextPath();

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(ctx + "/login");
            return;
        }

        String path = request.getServletPath();
        boolean adminPage = path.equals("/admin") || path.equals("/delete");
        if (adminPage && !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            response.sendRedirect(ctx + "/products");
            return;
        }

        chain.doFilter(req, res);
    }
}
