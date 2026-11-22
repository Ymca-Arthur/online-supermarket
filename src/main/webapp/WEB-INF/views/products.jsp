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
    <div>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>

    <div id="products">
        <ul>
            <c:forEach var="p" items="${products}">
                <li><c:out value="${p.name}"/> - $${p.price} (${p.stock} in stock)
                    <form action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="product_id" value="${p.id}">
                        <c:choose>
                            <c:when test="${p.stock > 0}">
                                <button type="submit">Add to cart</button>
                            </c:when>
                            <c:otherwise>Out of stock</c:otherwise>
                        </c:choose>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </div>

    <p>copyright 2026 do not distribute</p>
</body>
</html>
