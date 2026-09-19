<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My Products - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <span style="color:#fff">Hello, <c:out value="${sessionScope.user.name}"/></span>
  <form method="post" action="${pageContext.request.contextPath}/logout" style="margin:0">
    <button type="submit">Logout</button>
  </form>
</nav>
<div class="container">
  <div class="card">
    <h2>My Products</h2>
    <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
    <a class="btn" href="${pageContext.request.contextPath}/seller/products?action=new">+ Add Product</a>
    <c:if test="${empty products}"><p>No products yet.</p></c:if>
    <c:forEach var="p" items="${products}">
      <div style="border-top:1px solid #ddd; margin-top:14px; padding-top:10px">
        <b><c:out value="${p.name}"/></b><br>
        <c:out value="${p.category}"/> | Rs. <c:out value="${p.price}"/> | Stock: <c:out value="${p.stock}"/><br>
        <a class="btn" href="${pageContext.request.contextPath}/seller/products?action=edit&id=${p.id}">Edit</a>
        <form method="post" action="${pageContext.request.contextPath}/seller/products" style="display:inline"
              onsubmit="return confirm('Delete this product?');">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="id" value="${p.id}">
          <button class="btn" type="submit" style="background:#c0392b">Delete</button>
        </form>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>
