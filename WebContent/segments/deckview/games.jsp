<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="Games" class="tabcontent">
	<h1>Games</h1>
	<c:set var="goView" value="/DeckView.jsp" scope="request"/>
	<c:set var="controllers" value="/GetDeck" scope="request"/>
	<c:set var="errorView" value="/ErrorPage.jsp" scope="request"/>
	<c:set var="listContainer" value="${requestScope.gameListContainer}" scope="request"/>
	<jsp:include page="/segments/list_header.jsp">
		<jsp:param name="header" value="Your decks"/>
		<jsp:param name="addView" value="/AddDeck.jsp"/>
	</jsp:include>
    <jsp:include page="/segments/list.jsp"/>
</div>