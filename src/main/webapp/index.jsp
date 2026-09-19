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
</body>


</html>
