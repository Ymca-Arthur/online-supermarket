<%@ page language="java" %>
<%
// Traffic router: logged in -> shop (or admin page), otherwise -> login.
String ctx = request.getContextPath();
if (session.getAttribute("username") == null) {
    response.sendRedirect(ctx + "/login");
} else if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
    response.sendRedirect(ctx + "/admin");
} else {
    response.sendRedirect(ctx + "/products");
}
%>
