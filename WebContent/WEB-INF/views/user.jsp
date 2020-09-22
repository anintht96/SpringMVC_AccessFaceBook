<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user-page</title>
</head>
<body>

	<h1>User Page</h1>
	<h3>Welcome: ${pageContext.request.userPrincipal.name }</h3>
	<a href="<c:url value='/admin' />">Admin Page</a> <br>
	
	<form action="<c:url value='/j_spring_security_logout'/>" method="post">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<button type="submit">Logout</button>
	</form>

</body>
</html>