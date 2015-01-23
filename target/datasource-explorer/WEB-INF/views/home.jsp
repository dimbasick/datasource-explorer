<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Home</title>
	<meta charset="utf-8"/>
	<link href="<c:url value="/resources/styles/bootstrap.min.css" />" rel="stylesheet">
</head>
<body>
	<div class="container">
		<form action="<c:url value="/schemas" />" method="get" role="form">
			<div class="form-group">
				<h3 class="text-muted">Please enter datasource JNDI name</h3>
				<input name="jndi" type="text" placeholder="JNDI name" value="oracleDS"/>
			</div>
			<button type="submit" class="btn btn-default">Get schemas</button>
		</form>
		<form action="<c:url value="/ddl" />" method="get" role="form">
			<div class="form-group">
				<h3 class="text-muted">Please enter datasource JNDI name</h3>
				<input name="jndi" type="text" placeholder="JNDI name" value="oracleDS"/>
			</div>
			<button type="submit" class="btn btn-default">Get DDL</button>
		</form>
	</div>
</body>
</html>