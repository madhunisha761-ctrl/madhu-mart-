<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav><a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a></nav>
<div class="card form-card">
  <h2>Login</h2>
  <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
  <c:if test="${param.registered == '1'}"><div class="success">Registered! Please login.</div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <label>Email</label>
    <input type="email" name="email" value="<c:out value='${email}'/>" required>
    <label>Password</label>
    <input type="password" name="password" required>
    <button class="btn" type="submit">Login</button>
  </form>
  <p>New here? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</div>
</body>
</html>
