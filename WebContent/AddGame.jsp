<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Main"/>
	<jsp:param name="eol" value="true"/>
</jsp:include>

<script>

	$(function() {
			console.log("documentr ready");
			showAddGame();
		}
	)

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
	
	function showAddSpawn(){
		console.log("showing Add Spawn");
		$("#idAddGameForm").hide();
		$("#addSpawnForm").show();	
	}
	
	function showAddGame(){
		console.log("showing Add Game");
		$("#idAddGameForm").show();
		$("#addSpawnForm").hide();
	}
	
	function addOpponent(){
	}
</script>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-2 sidenav">
    </div>
    <div class="col-sm-8 text-left">
    	<h1>Add Game</h1>      
		<form id="idAddGameForm" action="/Magirator/Magirator" method="post">
			<input type="hidden" name="controllers" value="/AddGame,/GetGames">
			<!-- AddGame --><input type="hidden" name="playedDeck" value="${param.addToId}">
			<!-- GetGames --><input type="hidden" name="id" value="${param.addToId}">
			<input type="hidden" name="goView" value="/DeckView.jsp">
			<input type="hidden" name="altView" value="/DeckView.jsp">
				<input type="hidden" name="altcontrollers" value="/GetAlterations,/GetGames">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
			
			<div class="form-group">
    			<label for="comment">Comment:</label>
    			<input type="text" class="form-control" id="comment" name="comment">
  			</div> 
  			
			<div class="form-group">
				<label for="opponent">Opponent:</label>
				<select class="form-control" id="opponents" name="opponent" onchange="setOpponentDecks()">
					<option value="-1">Select opponent</option>
	  				<c:forEach items="${requestScope.opponents}" var="opponent">
						<option value="${opponent.id}" data-decks='${opponent.deckArray}'>${opponent.name}</option>					
					</c:forEach>
  				</select>
  				<button onclick="showAddSpawn()">
  					<span id="idShowAddOpponent" class="glyphicon glyphicon-plus" aria-hidden="true"></span>
  				</button>  				
			</div>
			
			<div class="form-group">
				<label for="decks">Opponent Deck:</label>
  				<select class="form-control" id="decks" name="opponentDeck">
					<option value="-1">Select deck</option>
  				</select>
			</div>
			
			<div>
				<!-- List opponents -->
				 <ul class="list-group">
  					<li class="list-group-item">First item</li>
  					<li class="list-group-item">Second item</li>
  					<li class="list-group-item">Third item</li>
				</ul>
			</div>
  			
  			<div class="checkbox">
  				<label for="colors">Win/Draw/Loss:</label>
    				<label><input type="checkbox" name="result" value="Win">Win</label>
    				<label><input type="checkbox" name="result" value="Draw">Draw</label>
    				<label><input type="checkbox" name="result" value="Loss">Loss</label>
  			</div>
  			<jsp:include page="/segments/addcancelbutton.jsp"/>
		</form>
		
		<form id="addSpawnForm" action="">
			<div class="form-group">
    			<label for="name">Name:</label>
    			<input type="text" class="form-control" id="idName" name="Name" placeholder="Name">
  			</div>
			<div class="form-group">
    			<label for="name">Deck name:</label>
    			<input type="text" class="form-control" id="idDeckName" name="deckName" placeholder="Deck name">
  			</div>				
			<div class="form-group">
				<label for="format">Format:</label>
  				<select class="form-control" id="idFormat" name="format">
    				<option value="Standard">Standard</option>
    				<option value="Block">Block</option>
    				<option value="Pauper">Pauper</option>
  				</select>
			</div>
  			<div class="checkbox">
    			<label><input type="checkbox" name="colors" value="Black">Black</label>
    			<label><input type="checkbox" name="colors" value="White">White</label>
    			<label><input type="checkbox" name="colors" value="Red">Red</label>
    			<label><input type="checkbox" name="colors" value="Green">Green</label>
    			<label><input type="checkbox" name="colors" value="Blue">Blue</label>
    			<label><input type="checkbox" name="colors" value="Colorless">Colorless</label>
  			</div>
  			<div class="form-group">
				<label for="theme">Theme:</label>
				<input type="text" class="form-control" id="idTheme" name="theme">
  			</div>
  			
  			<div class="btn-group">
					<button type="button" class="btn btn-warning" onclick="showAddGame()">Cancel</button>
					<button type="button" class="btn btn-success" onclick="showAddGame()">Add</button>
			</div>
		</form>
    </div>
    <jsp:include page="/segments/ads.jspf"/>
  </div>
</div>

<footer class="container-fluid text-center">
	
</footer>

<jsp:include page="/segments/endofpage.jspf"/>

