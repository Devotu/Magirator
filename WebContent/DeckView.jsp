<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/basedoc/html_head.jsp"/>
<jsp:include page="/segments/include/bootstrap_head.jsp"/>
<jsp:include page="/segments/include/magirator_style_head.jsp"/>
<jsp:include page="/segments/basedoc/head_body.jsp"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
	<jsp:param name="page" value="Deck ${sessionScope.deck.name}"/>
</jsp:include>
  
<div class="container-fluid text-center">    
	<div class="row content">
		<div class="col-sm-1 sidenav">
		</div>
		<div class="col-sm-9 text-left">
			<h1>${sessionScope.deck.name}</h1>
			<jsp:include page="/segments/tabs.jsp">
				<jsp:param name="defaultTab" value="Info"/>
				<jsp:param name="tabItems" value="Info"/>
				<jsp:param name="tabItems" value="Statistics"/>
				<jsp:param name="tabItems" value="Games"/>
				<jsp:param name="tabItems" value="Alterations"/>
			</jsp:include>
			<jsp:include page="segments/deckview/deckinfo.jsp">
				<jsp:param name="deck" value="${sessionScope.deck}"/>
			</jsp:include>
			<jsp:include page="segments/deckview/games.jsp">
				<jsp:param name="games" value="${sessionScope.gameListContainer}"/>
			</jsp:include>
			<jsp:include page="segments/deckview/alterations.jsp">
				<jsp:param name="games" value="${sessionScope.alterationListContainer}"/>
			</jsp:include>
		</div>    
		<jsp:include page="/segments/ads.jspf"/>
	</div>
</div>

<jsp:include page="/segments/basedoc/body_html.jsp"/>

