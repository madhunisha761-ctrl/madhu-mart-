<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Register - MadhuMart</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav><a class="brand" href="${pageContext.request.contextPath}/">MadhuMart</a></nav>
<div class="card form-card">
  <h2>Create Account</h2>
  <c:if test="${not empty error}"><div class="error"><c:out value="${error}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/register">
    <label>Name</label>
    <input type="text" name="name" value="<c:out value='${name}'/>" required>
    <label>Email</label>
    <input type="email" name="email" value="<c:out value='${email}'/>" required>
    <label>Password (min 8 characters)</label>
    <input type="password" name="password" minlength="8" required>
    <label>I want to</label>
    <select name="role">
      <option value="BUYER" ${role == 'BUYER' ? 'selected' : ''}>Buy products</option>
      <option value="SELLER" ${role == 'SELLER' ? 'selected' : ''}>Sell products</option>
    </select>
    <button class="btn" type="submit">Register</button>
  </form>
  <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
</div>
</body>
</html>
