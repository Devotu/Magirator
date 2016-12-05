<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/include/bootstrap_head.jsp"/>
<jsp:include page="/segments/include/magirator_style_head.jsp"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Confirm games list"/>
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">
	<div class="row content">
		<div class="col-sm-2 sidenav">
		</div>
		<div class="col-sm-8 text-left">
			<c:set var="goView" value="/ConfirmGameView.jsp" scope="request"/>	
			<c:set var="controllers" scope="request" value="/GetGame"/>
			<c:set var="errorView" value="/ErrorPage.jsp" scope="request"/>
			<c:set var="listContainer" value="${requestScope.gameListContainer}" scope="request"/>
			<jsp:include page="/segments/list_header.jsp">
				<jsp:param name="header" value="Your unconfirmed games"/>
				<jsp:param name="addView" value="/AddDeck.jsp"/>
			</jsp:include>
      		<jsp:include page="/segments/list.jsp"/>
    	</div>
    	<jsp:include page="/segments/ads.jspf"/>
  	</div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>