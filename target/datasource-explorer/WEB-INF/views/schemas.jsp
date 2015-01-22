<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Schemas</title>
	<link href="<c:url value="/resources/styles/bootstrap.min.css" />" rel="stylesheet">
	<meta charset="utf-8"/>
</head>
<body>	
	<div class="container">
	    <c:choose>
	        <c:when test="${not empty list}">
	        	<h3 class="text-muted">Found schemas in datasource ${jndi} : </h3>
				<ul>
					<c:forEach var="item" items="${list}">
						<li>
							<a href="tables?schema=${item}">${item}</a>
						</li>
					</c:forEach>
				</ul>
	        </c:when>
	        <c:otherwise>
				<h3 class="text-muted">No schemas found datasource ${jndi} : </h3>
	        </c:otherwise>
	    </c:choose>
	</div>
</body>
</html>
