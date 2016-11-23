<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div id="Info" class="tabcontent">
	<h1>Deck info</h1>
	<div>
		<form action="/Magirator/Magirator" method="post" onsubmit="return confirm('Do you really want to delete the deck?');">
			<input type="hidden" name="deckid" value="${deck.deckid}">
			<input type="hidden" name="controllers" value="/DeleteDeck,/GetDecks">			
			<input type="hidden" name="goView" value="/DeckList.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
			<button type="submit">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
			</button>
		</form>
		<form action="/Magirator/Magirator" method="post">
			<input type="hidden" name="deckid" value="${deck.deckid}">
			<input type="hidden" name="id" value="${deck.deckid}">
			<input type="hidden" name="controllers" value="/ToggleDeck,/GetDeck">			
			<input type="hidden" name="goView" value="/DeckView.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
			<button type="submit">
				<c:choose>
					<c:when test="${true == deck.active}">
						<span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
					</c:when>
					<c:otherwise>
						<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
					</c:otherwise>
				</c:choose>
			</button>
		</form>		
	</div>
	
	<label for="name">Name:</label>
	<p>${deck.name}</p>
	<label for="name">Format:</label>
	<p>${deck.format}</p>
	<label for="name">Colors:</label>
	<div>
		 <table>
  			<tr>
    			<th>
					<c:choose>
						<c:when test="${true == deck.black}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="black"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
    			<th>
					<c:choose>
						<c:when test="${true == deck.white}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="white"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == deck.red}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="red"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == deck.green}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="green"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == deck.blue}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="blue"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == deck.colorless}">
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
	<label for="name">Theme:</label>
	<p>${deck.theme}</p>
	<label for="name">Active:</label>
	<p>${deck.active}</p>
	<label for="name">Created:</label>
	<p>${deck.created}</p>
</div>

