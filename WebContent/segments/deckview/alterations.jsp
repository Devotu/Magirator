<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="Alterations" class="tabcontent">
	<h1>Alterations</h1>
	<c:set var="goView" value="/AlterationView.jsp" scope="request"/>
	<c:set var="controllers" value="/GetAlteration" scope="request"/>
	<c:set var="errorView" value="/ErrorPage.jsp" scope="request"/>
	<c:set var="listContainer" value="${requestScope.alterationListContainer}" scope="request"/>
	<jsp:include page="/segments/list_header.jsp">
		<jsp:param name="header" value="Deck alterations"/>
		<jsp:param name="addView" value="/AddAlteration.jsp"/>
		<jsp:param name="addToId" value="${sessionScope.deck.deckid}"/>
		<jsp:param name="controllers" value=""/>
	</jsp:include>
    <jsp:include page="/segments/list.jsp"/>
</div>