<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Orders - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <a href="${pageContext.request.contextPath}/products">Products</a>
  <a href="${pageContext.request.contextPath}/cart">Cart</a>
  <a href="${pageContext.request.contextPath}/orders">Orders</a>
</nav>
<div class="container">
  <div class="card">
    <h2>My Orders</h2>
    <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
    <c:if test="${empty orders}"><p>You have no orders yet.</p></c:if>
    <c:forEach var="o" items="${orders}">
      <div style="border-top:1px solid #ddd; margin-top:14px; padding-top:10px">
        <b>Order #<c:out value="${o.id}"/></b> | <c:out value="${o.status}"/><br>
        Placed: <c:out value="${o.createdAt}"/><br>
        Ship to: <c:out value="${o.shippingAddress}"/><br>
        <c:forEach var="it" items="${o.items}">
          - <c:out value="${it.productName}"/> x <c:out value="${it.quantity}"/>
          = Rs. <c:out value="${it.subtotal}"/><br>
        </c:forEach>
        <b>Total: Rs. <c:out value="${o.totalAmount}"/></b>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>
