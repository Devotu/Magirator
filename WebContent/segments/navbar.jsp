<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
	      		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>                        
	      		</button>
				<form action="/Magirator/Magirator">
					<button type="submit">Main</button> 				
					<input type="hidden" name="goView" value="/Main.jsp">
				</form>
	      		<a class="navbar-brand" href="#">: ${param.page}</a>
	    	</div>
	    	<div class="collapse navbar-collapse" id="myNavbar">
			<c:if test="${param.content == 'main'}">
				<jsp:include page="/segments/navbar_standard.jspf"/>
			</c:if>
			<c:if test="${param.content == 'welcome'}">
				<jsp:include page="/segments/navbar_welcome.jspf"/>
			</c:if>
		</div>
	</div>
</nav>
