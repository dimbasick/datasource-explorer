<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Tables</title>
	<link href="<c:url value="/resources/styles/bootstrap.min.css" />" rel="stylesheet"/>
	<meta charset="utf-8"/>
	 
	<link rel="stylesheet" href="//cdn.jsdelivr.net/highlight.js/8.4/styles/default.min.css">
	<script src="//cdn.jsdelivr.net/highlight.js/8.4/highlight.min.js"></script>
	<script>hljs.initHighlightingOnLoad();</script>
	<!-- 
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
	-->
	
</head>
<body>	
	<div class="container">
	    <c:choose>
	        <c:when test="${not empty ddl}">
	        	<!--
	        	<pre class="prettyprint lang-sql">
	        		${ddl}
	        	</pre>
	        	-->	
	        	<pre>
	        		<code class="sql">
	        			${ddl}
	        		</code>
	        	</pre>
	        </c:when>
	        <c:otherwise>
				<h3 class="text-muted">No ddl found by jndi name : ${jndi}</h3>
	        </c:otherwise>
	    </c:choose>
	</div>
</body>
</html>
