<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Columns</title>
	<link rel="stylesheet" href="resources/styles/bootstrap/css/bootstrap.css"/>
	<meta charset="utf-8"/>
</head>
<body>	
	<div class="container">
	    <c:choose>
	        <c:when test="${not empty list}">
	        	<h3 class="text-muted">Found columns in schema ${schema} table ${table} : </h3>
	        	<div class="table-responsive">		        	
					<table class="table">
						<thead>
							<tr>
								<td>Name</td>
								<td>Column data type</td>
								<td>Default value</td>
								<td>Referenced column</td>
								<td>Size</td>
								<td>Width</td>
							</tr>
						</thead>
						<c:forEach var="item" items="${list}">
							<tr>
								<td>${item.name}</td>
								<td>${item.columnDataType}</td>
								<td>${item.defaultValue}</td>
								<td>${item.referencedColumn}</td>
								<td>${item.size}</td>
								<td>${item.width}</td>
							</tr>						
						</c:forEach>
					</table>
				</div>
	        </c:when>
	        <c:otherwise>
				<h3 class="text-muted">No tables found by schema ${schema} : </h3>
	        </c:otherwise>
	    </c:choose>
	</div>
</body>
</html>
