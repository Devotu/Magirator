<!DOCTYPE html>
<%@ page isErrorPage="true" %>
<%@ page isELIgnored="false" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">    
	<div class="row content">
		<div class="col-sm-1 sidenav">
		</div>
		<div class="col-sm-11 text-left">
			<h1>Error</h1>
			Unfortunately something went wrong.
			This to be specific: ${requestScope.exception}
		</div>
	</div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>
