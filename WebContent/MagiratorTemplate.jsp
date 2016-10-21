<!DOCTYPE html>
<%@ page isELIgnored="false" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Page"/>
	<jsp:param name="content" value="main"/>
</jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-1 sidenav">
    </div>
    <div class="col-sm-9 text-left">
      <h1>Template</h1>
    </div>
    <jsp:include page="/segments/ads.jspf"/>
  </div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>

