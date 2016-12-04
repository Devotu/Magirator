<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/basedoc/html_head.jsp"/>
<jsp:include page="/segments/include/bootstrap_head.jsp"/>
<jsp:include page="/segments/include/magirator_style_head.jsp"/>
<jsp:include page="/segments/basedoc/head_body.jsp"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
	<jsp:param name="page" value="Deck ${sessionScope.deck.name} - Game"/>
</jsp:include>
  
<div class="container-fluid text-center">    
	<div class="row content">
		<div class="col-sm-1 sidenav">
		</div>
		<div class="col-sm-9 text-left">
				<h1>Confirm Game</h1>
				<label for="name">Date:</label>
				<p>${requestScope.game.game.datePlayed}</p>
				<c:forEach items='${requestScope.game.results}' var='result'>
					<div class="well">
						<label for="name">Player:</label>
						<h3>${result.play.place}</h3><p>${result.player.name}</p>
						<label for="name">Player colors:</label>
						<!-- Colors -->
						<div>
							 <table>
					  			<tr>
					    			<th>
										<c:choose>
											<c:when test="${true == result.deck.black}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="black"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
					    			<th>
										<c:choose>
											<c:when test="${true == result.deck.white}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="white"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
									<th>
										<c:choose>
											<c:when test="${true == result.deck.red}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="red"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
									<th>
										<c:choose>
											<c:when test="${true == result.deck.green}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="green"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
									<th>
										<c:choose>
											<c:when test="${true == result.deck.blue}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="blue"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
									<th>
										<c:choose>
											<c:when test="${true == result.deck.colorless}">
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="lightgray"/>
											</c:when>
											<c:otherwise>
												<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
											</c:otherwise>
										</c:choose>
									</th>
					  			</tr>
							</table>
						</div>
						<label for="name">Comment:</label>
						<p>${result.play.comment}</p>
					</div>
					
				</c:forEach>
				


	
	
	</div>    
		<jsp:include page="/segments/ads.jspf"/>
	</div>
</div>

<jsp:include page="/segments/basedoc/body_html.jsp"/>

