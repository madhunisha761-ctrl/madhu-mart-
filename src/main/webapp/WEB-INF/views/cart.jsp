<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Cart - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <a href="${pageContext.request.contextPath}/products">Products</a>
  <a href="${pageContext.request.contextPath}/cart">Cart</a>
</nav>
<div class="container">
  <div class="card">
    <h2>My Cart</h2>
    <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
    <c:if test="${empty items}"><p>Your cart is empty.</p></c:if>
    <c:forEach var="i" items="${items}">
      <div style="border-top:1px solid #ddd; margin-top:14px; padding-top:10px">
        <b><c:out value="${i.productName}"/></b><br>
        Rs. <c:out value="${i.price}"/> x <c:out value="${i.quantity}"/>
        = Rs. <c:out value="${i.subtotal}"/>
        <form method="post" action="${pageContext.request.contextPath}/cart">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="productId" value="${i.productId}">
          <input type="number" name="quantity" min="0" value="${i.quantity}" style="width:80px">
          <button class="btn" type="submit">Update</button>
        </form>
        <form method="post" action="${pageContext.request.contextPath}/cart">
          <input type="hidden" name="action" value="remove">
          <input type="hidden" name="productId" value="${i.productId}">
          <button class="btn" type="submit" style="background:#c0392b">Remove</button>
        </form>
      </div>
    </c:forEach>
    <c:if test="${not empty items}">
      <h3>Total: Rs. <c:out value="${total}"/></h3>
    </c:if>
  </div>
</div>
</body>
</html>
