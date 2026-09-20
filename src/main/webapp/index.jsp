<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
  <a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a>
  <a href="${pageContext.request.contextPath}/products">Products</a>
  <c:if test="${not empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/cart">Cart</a>
    <a href="${pageContext.request.contextPath}/orders">Orders</a>
  </c:if>
  <c:if test="${sessionScope.user.role == 'SELLER'}">
    <a href="${pageContext.request.contextPath}/seller/products">My Products</a>
  </c:if>
  <c:if test="${sessionScope.user.role == 'ADMIN'}">
    <a href="${pageContext.request.contextPath}/admin/panel">Admin</a>
  </c:if>
  <c:choose>
    <c:when test="${not empty sessionScope.user}">
      <span style="color:#fff">Hello, <c:out value="${sessionScope.user.name}"/> (<c:out value="${sessionScope.user.role}"/>)</span>
      <form method="post" action="${pageContext.request.contextPath}/logout" style="margin:0">
        <button type="submit">Logout</button>
      </form>
    </c:when>
    <c:otherwise>
      <a href="${pageContext.request.contextPath}/login">Login</a>
      <a href="${pageContext.request.contextPath}/register">Register</a>
    </c:otherwise>
  </c:choose>
</nav>
<div class="container">
  <div class="card">
    <h1>Welcome to MadhuMart</h1>
    <p>Multi-seller online marketplace.</p>
  </div>
</div>
<div style="position:fixed;right:12px;bottom:12px;width:280px;background:#fff;border:1px solid #ccc;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,.3)">
  <div style="background:#e67e22;color:#fff;padding:8px;font-weight:bold">MadhuBot</div>
  <div id="chatlog" style="height:140px;overflow:auto;padding:8px;font-size:14px"></div>
  <div style="display:flex;padding:6px;gap:4px">
    <input id="chatmsg" type="text" placeholder="Ask me..." style="flex:1;margin:0">
    <button class="btn" style="margin:0;padding:6px 10px" onclick="sendChat()">Send</button>
  </div>
</div>
<script src="${pageContext.request.contextPath}/js/chat.js"></script>
</body>


</html>
