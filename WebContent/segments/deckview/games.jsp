<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="Games" class="tabcontent">
	<h1>Games</h1>
	<c:set var="goView" value="/GameView.jsp" scope="request"/>
	<c:set var="controllers" value="/GetGame" scope="request"/>
	<c:set var="errorView" value="/ErrorPage.jsp" scope="request"/>
	<c:set var="listContainer" value="${requestScope.gameListContainer}" scope="request"/>
	<jsp:include page="/segments/list_header.jsp">
		<jsp:param name="header" value="Deck games"/>
		<jsp:param name="addView" value="/AddGame.jsp"/>
		<jsp:param name="addToId" value="${sessionScope.deck.deckid}"/>
		<jsp:param name="controllers" value="/GetOpponents"/>
	</jsp:include>
    <jsp:include page="/segments/list.jsp"/>
</div>