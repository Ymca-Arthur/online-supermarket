<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Online Supermarket</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>Online Supermarket</h1>

    <c:if test="${not empty error}">
        <p><c:out value="${error}"/></p>
    </c:if>

    <div id="login_area">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="text" name="username" placeholder="Username">
            <input type="password" name="password" placeholder="Password">
            <button type="submit">Login</button>
        </form>
        <form action="${pageContext.request.contextPath}/signup" method="post">
            <input type="text" name="username" placeholder="New username">
            <input type="password" name="password" placeholder="New password">
            <button type="submit">Sign Up</button>
        </form>
    </div>
</body>
</html>
