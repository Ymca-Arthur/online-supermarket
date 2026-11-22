<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
</head>
<body>
    <h1>Welcome Admin</h1>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>

    <form action="${pageContext.request.contextPath}/admin" method="post">
        <input type="text" name="name" placeholder="product name">
        <input type="number" name="price" placeholder="price" step="0.01">
        <input type="number" name="stock" placeholder="stock">
        <button type="submit">Add product</button>
    </form>

    <div id="product_list">
        <ul>
            <c:forEach var="p" items="${products}">
                <li><c:out value="${p.name}"/> - $${p.price} (${p.stock} in stock)
                    <form action="${pageContext.request.contextPath}/delete" method="post">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Delete</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>
