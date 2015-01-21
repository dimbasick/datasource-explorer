<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Home</title>
	<meta charset="utf-8"/>
	<link rel="stylesheet" href="datasource-explorer/resources/styles/bootstrap/css/bootstrap.css"/>
</head>
<body>
	<div class="container">
		<form action="/datasource-explorer/schemas" method="get" role="form">
			<div class="form-group">
				<h3 class="text-muted">Please enter datasource JNDI name</h3>
				<input name="jndi" type="text" placeholder="JNDI name" value="jdbc/oracleDS"/>
			</div>
			<button type="submit" class="btn btn-default">Get schemas</button>
		</form>
		
	</div>
</body>
</html>
