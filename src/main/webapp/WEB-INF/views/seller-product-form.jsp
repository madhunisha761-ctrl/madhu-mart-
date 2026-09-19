<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Product Form - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <a href="${pageContext.request.contextPath}/seller/products">My Products</a>
</nav>
<div class="card form-card">
  <h2><c:choose><c:when test="${not empty product or not empty param.id}">Edit Product</c:when><c:otherwise>Add Product</c:otherwise></c:choose></h2>
  <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/seller/products">
    <input type="hidden" name="id" value="<c:out value='${not empty product ? product.id : param.id}'/>">
    <label>Name</label>
    <input type="text" name="name" maxlength="150" value="<c:out value='${not empty product ? product.name : param.name}'/>" required>
    <label>Description</label>
    <textarea name="description" rows="3" maxlength="2000"><c:out value="${not empty product ? product.description : param.description}"/></textarea>
    <label>Category</label>
    <input type="text" name="category" list="cats" maxlength="50" value="<c:out value='${not empty product ? product.category : param.category}'/>" required>
    <datalist id="cats">
      <option value="Electronics"><option value="Fashion"><option value="Books"><option value="Home">
    </datalist>
    <label>Price (Rs.)</label>
    <input type="number" name="price" step="0.01" min="0.01" value="<c:out value='${not empty product ? product.price : param.price}'/>" required>
    <label>Stock</label>
    <input type="number" name="stock" min="0" value="<c:out value='${not empty product ? product.stock : param.stock}'/>" required>
    <label>Image URL (optional)</label>
    <input type="text" name="imageUrl" maxlength="300" value="<c:out value='${not empty product ? product.imageUrl : param.imageUrl}'/>">
    <button class="btn" type="submit">Save</button>
  </form>
</div>
</body>
</html>
