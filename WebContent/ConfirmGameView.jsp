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
				<label for="date">Date:</label>
				<p>${requestScope.game.game.datePlayed}</p>
				<c:forEach items='${requestScope.game.results}' var='result'>
					<div class="well">
						<h3>${result.play.place}</h3>
						<label for="name">Player:</label>
						<p>${result.player.name}</p>
						<label for="colors">Player colors:</label>
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
						
						<div>
							<label for="comment">Comment:</label>							
							<c:choose>
    							<c:when test="${result.player.id == sessionScope.player.id}">
    								<form action="/Magirator/Magirator" method="post">
										<input type="hidden" name="playId" value="${result.play.id}"><!-- ConfirmGame -->
										<input type="hidden" name="confimedStatus" value="-1"><!-- GetUserGames (unconfirmed games) -->
										<input type="hidden" name="controllers" value="/ConfirmGame,/GetUserGames">			
										<input type="hidden" name="goView" value="/ConfirmGameList.jsp">
										<input type="hidden" name="altView" value="/ConfirmGameList.jsp">
										<input type="hidden" name="altcontrollers" value="/ConfirmGame,/GetUserGames">		
										<input type="hidden" name="errorView" value="/ErrorPage.jsp">    								
										
    									<input id="comment" type="text" name="comment"> 
    									<div class="btn-group">
											<button type="submit" class="btn btn-warning" name="action" value="alt">Reject</button>
											<button type="submit" class="btn btn-success" name="action" value="go">Confirm</button>
										</div>
									</form>								
    							</c:when>    
    							<c:otherwise>  									
									<p id="comment">${result.play.comment}</p>
    							</c:otherwise>
							</c:choose>
						</div>

					</div>
					
				</c:forEach>
	
	</div>    
		<jsp:include page="/segments/ads.jspf"/>
	</div>
</div>

<jsp:include page="/segments/basedoc/body_html.jsp"/>

