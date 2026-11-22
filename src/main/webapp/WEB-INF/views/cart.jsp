<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>Your Cart</h1>
    <a href="${pageContext.request.contextPath}/products">Back to store</a>

    <div id="cart_list">
        <ul>
            <c:forEach var="item" items="${items}">
                <li><c:out value="${item.name}"/> - $${item.price} x ${item.quantity}</li>
            </c:forEach>
        </ul>
    </div>

    <form action="${pageContext.request.contextPath}/buy" method="post">
        <button type="submit">Buy</button>
    </form>
</body>
</html>
