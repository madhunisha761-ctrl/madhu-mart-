<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Checkout - MadhuMart</title>
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
    <c:choose>
      <c:when test="${not empty param.done}">
        <h2>Order placed!</h2>
        <div class="success">Thank you. Your order number is #<c:out value="${param.done}"/>.</div>
        <a class="btn" href="${pageContext.request.contextPath}/products">Continue shopping</a>
      </c:when>
      <c:otherwise>
        <h2>Checkout</h2>
        <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
        <c:if test="${empty items}"><p>Your cart is empty.</p></c:if>
        <c:forEach var="i" items="${items}">
          <div style="border-top:1px solid #ddd; margin-top:10px; padding-top:8px">
            <b><c:out value="${i.productName}"/></b><br>
            Rs. <c:out value="${i.price}"/> x <c:out value="${i.quantity}"/>
            = Rs. <c:out value="${i.subtotal}"/>
          </div>
        </c:forEach>
        <c:if test="${not empty items}">
          <h3>Total: Rs. <c:out value="${total}"/></h3>
          <form method="post" action="${pageContext.request.contextPath}/checkout">
            <label>Shipping address</label>
            <textarea name="address" rows="3" maxlength="500" required><c:out value="${address}"/></textarea>
            <button class="btn" type="submit">Place Order</button>
          </form>
        </c:if>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</body>
</html>
