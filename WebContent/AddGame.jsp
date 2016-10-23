<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Main"/>
	<jsp:param name="eol" value="true"/>
</jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-2 sidenav">
    </div>
    <div class="col-sm-8 text-left">
    	<h1>Add Game</h1>      
		<form action="/Magirator/Magirator" method="post">
			<input type="hidden" name="controllers" value="/AddGame,/GetGames">
			<!-- AddGame --><input type="hidden" name="playedDeck" value="${param.addToId}">
			<!-- GetGames --><input type="hidden" name="id" value="${param.addToId}">
			<input type="hidden" name="goView" value="/DeckView.jsp">
			<input type="hidden" name="altView" value="/DeckView.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
			
			<div class="form-group">
				<label for="opponent">Opponent:</label>
				<select class="form-control" id="opponents" name="opponent" onchange="setOpponentDecks()">
					<option value="-1">Select opponent</option>
	  				<c:forEach items="${requestScope.opponents}" var="opponent">
						<option value="${opponent.id}" data-decks='${opponent.deckArray}'>${opponent.name}</option>					
					</c:forEach>
  				</select>
			</div>
			<div class="form-group">
				<label for="decks">Opponent Deck:</label>
  				<select class="form-control" id="decks" name="opponentDeck">
					<option value="-1">Select deck</option>
  				</select>
			</div>
			<script>
				function setOpponentDecks(){
					//Remove all current options
					var decks = $("#decks");
					decks.find('option').remove();
					
					//Get the opponent decks
					var opponentDecks = $("#opponents").find(':selected').data('decks');
					console.log(opponentDecks);
					
					//Populate with all the found opponents decks
					$.each(opponentDecks, function (i, item) {
						console.log(item);
						decks.append($('<option>', {
					        value: item.id,
					        text: item.name
					    }));
					});
				}
			</script>
			
  			<div class="form-group">
    			<label for="comment">Comment:</label>
    			<input type="text" class="form-control" id="comment" name="comment">
  			</div> 
  			
  			<div class="checkbox">
  				<label for="colors">Win/Draw/Loss:</label>
    				<label><input type="checkbox" name="result" value="Win">Win</label>
    				<label><input type="checkbox" name="result" value="Draw">Draw</label>
    				<label><input type="checkbox" name="result" value="Loss">Loss</label>
  			</div>
  			<jsp:include page="/segments/addcancelbutton.jsp"/>
		</form>
    </div>
    <jsp:include page="/segments/ads.jspf"/>
  </div>
</div>

<footer class="container-fluid text-center">
	
</footer>

<jsp:include page="/segments/endofpage.jspf"/>

