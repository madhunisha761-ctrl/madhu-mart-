<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Admin - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <a href="${pageContext.request.contextPath}/admin/panel">Admin</a>
</nav>
<div class="container">
  <div class="card">
    <h2>Users</h2>
    <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
    <c:forEach var="u" items="${users}">
      <div style="border-top:1px solid #ddd; padding:8px 0">
        <b><c:out value="${u.name}"/></b> (<c:out value="${u.role}"/>)<br>
        <c:out value="${u.email}"/> | <c:out value="${u.active ? 'Active' : 'Disabled'}"/>
        <c:if test="${u.role != 'ADMIN'}">
          <form method="post" action="${pageContext.request.contextPath}/admin/panel">
            <input type="hidden" name="id" value="${u.id}">
            <input type="hidden" name="action" value="${u.active ? 'disableUser' : 'enableUser'}">
            <button class="btn" type="submit"><c:out value="${u.active ? 'Disable' : 'Enable'}"/></button>
          </form>
        </c:if>
      </div>
    </c:forEach>
  </div>
  <div class="card" style="margin-top:16px">
    <h2>Orders</h2>
    <c:if test="${empty orders}"><p>No orders.</p></c:if>
    <c:forEach var="o" items="${orders}">
      <div style="border-top:1px solid #ddd; padding:8px 0">
        #<c:out value="${o.id}"/> | <c:out value="${o.buyerName}"/> | Rs. <c:out value="${o.totalAmount}"/> | <c:out value="${o.status}"/>
        <c:if test="${o.status != 'COMPLETED'}">
          <form method="post" action="${pageContext.request.contextPath}/admin/panel">
            <input type="hidden" name="id" value="${o.id}">
            <input type="hidden" name="action" value="completeOrder">
            <button class="btn" type="submit">Mark Completed</button>
          </form>
        </c:if>
      </div>
    </c:forEach>
  </div>
  <div class="card" style="margin-top:16px">
    <h2>Listings</h2>
    <c:forEach var="p" items="${products}">
      <div style="border-top:1px solid #ddd; padding:8px 0">
        <b><c:out value="${p.name}"/></b> by <c:out value="${p.sellerName}"/><br>
        Rs. <c:out value="${p.price}"/> | Stock: <c:out value="${p.stock}"/> | <c:out value="${p.active ? 'Visible' : 'Hidden'}"/>
        <form method="post" action="${pageContext.request.contextPath}/admin/panel">
          <input type="hidden" name="id" value="${p.id}">
          <input type="hidden" name="action" value="${p.active ? 'hideProduct' : 'showProduct'}">
          <button class="btn" type="submit"><c:out value="${p.active ? 'Hide' : 'Show'}"/></button>
        </form>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>
