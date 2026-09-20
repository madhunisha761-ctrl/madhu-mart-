<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Products - MadhuMart</title>
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
    <form method="get" action="${pageContext.request.contextPath}/products">
      <input type="text" name="q" placeholder="Search products" value="<c:out value='${q}'/>">
      <select name="category">
        <option value="">All categories</option>
        <c:forEach var="cat" items="${categories}">
          <option value="<c:out value='${cat}'/>" ${cat == selectedCategory ? 'selected' : ''}><c:out value="${cat}"/></option>
        </c:forEach>
      </select>
      <button class="btn" type="submit">Search</button>
    </form>
    <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
    <c:if test="${empty products}"><p>No products found.</p></c:if>
    <c:forEach var="p" items="${products}">
      <div style="border-top:1px solid #ddd; margin-top:14px; padding-top:10px">
        <b><a href="${pageContext.request.contextPath}/product?id=${p.id}"><c:out value="${p.name}"/></a></b><br>
        <c:out value="${p.category}"/> | Rs. <c:out value="${p.price}"/><br>
        Seller: <c:out value="${p.sellerName}"/>
      <br><button class="btn" type="button" onclick="addToCart(${p.id})">Add to Cart</button>
      </div>
    </c:forEach>
  </div>
</div>
<script>
function addToCart(id) {
  fetch('${pageContext.request.contextPath}/cart', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: 'action=add&ajax=1&productId=' + id
  }).then(function (r) {
    if (r.redirected) { window.location = r.url; return null; }
    return r.json();
  }).then(function (d) {
    if (d) { alert(d.message + ' (Cart items: ' + d.count + ')'); }
  }).catch(function () { alert('Something went wrong'); });
}
</script>
</body>
</html>
