<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">
	<div class="row content">
		<div class="col-sm-2 sidenav">
		</div>
		<div class="col-sm-8 text-left">
      		<jsp:include page="/segments/list_header.jsp">
			<jsp:param name="header" value="Your decks"/>
		</jsp:include>
		<c:set var="goView" value="/DeckView.jsp" scope="request"/>
		<c:set var="controller" value="/GetDeck" scope="request"/>
		<c:set var="errorView" value="/ErrorPage.jsp" scope="request"/>
		<c:set var="listItems" value="${requestScope.deckList}" scope="request"/>
      		<jsp:include page="/segments/list.jsp"/>
    	</div>
    	<jsp:include page="/segments/ads.jspf"/>
  	</div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>

