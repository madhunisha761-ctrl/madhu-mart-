<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Product - MadhuMart</title>
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
    <h2><c:out value="${product.name}"/></h2>
    <p><c:out value="${product.description}"/></p>
    <p><c:out value="${product.category}"/> | Rs. <c:out value="${product.price}"/> | Seller: <c:out value="${product.sellerName}"/></p>
    <p><b>Rating: <fmt:formatNumber value="${average}" maxFractionDigits="1"/> / 5</b> (<c:out value="${reviews.size()}"/> reviews)</p>
  </div>
  <div class="card" style="margin-top:16px">
    <h3>Reviews</h3>
    <c:if test="${not empty param.error}"><div class="error"><c:out value="${param.error}"/></div></c:if>
    <c:if test="${empty reviews}"><p>No reviews yet.</p></c:if>
    <c:forEach var="r" items="${reviews}">
      <div style="border-top:1px solid #ddd; padding:8px 0">
        <b><c:out value="${r.userName}"/></b> - <c:out value="${r.rating}"/> stars<br>
        <c:out value="${r.reviewText}"/>
      </div>
    </c:forEach>
    <c:if test="${canReview}">
      <form method="post" action="${pageContext.request.contextPath}/reviews">
        <input type="hidden" name="productId" value="${product.id}">
        <label>Rating</label>
        <select name="rating">
          <option value="5">5 stars</option><option value="4">4 stars</option>
          <option value="3">3 stars</option><option value="2">2 stars</option>
          <option value="1">1 star</option>
        </select>
        <label>Your review</label>
        <textarea name="text" rows="3" maxlength="1000"></textarea>
        <button class="btn" type="submit">Submit Review</button>
      </form>
    </c:if>
  </div>
</div>
</body>
</html>
