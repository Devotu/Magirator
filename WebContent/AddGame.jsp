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
			<input type="hidden" name="controllers" value="/AddGame">
			<input type="hidden" name="goView" value="/DeckView.jsp">
			<input type="hidden" name="altView" value="/DeckView.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
			
			<div class="form-group">
				<label for="opponent">Opponent:</label>
				<select class="form-control" id="opponents" name="opponent" onchange="setOpponentDecks('${requestScope.opponents}')">
					<option value="-1">Select opponent</option>
	  				<c:forEach items="${requestScope.opponents}" var="opponent">
						<option value="${opponent.id}">${opponent.name}</option>					
					</c:forEach>
  				</select>
			</div>
			<div class="form-group">
				<label for="opponent">Opponent Deck:</label>
  				<select class="form-control" id="decks" name="deck">
					<option value="-1">Select deck</option>
    				<option value="Top">Top1</option>
    				<option value="Rick">To2</option>
    				<option value="Dan">T3</option>
  				</select>
			</div>
			<script>
				function setOpponentDecks(opponents){
					var opponentId = $("#opponents").val();
					
					var decks = $("#decks");
					decks.find('option').remove();
					
					console.log(opponents);
					$.each(opponents, function (i, item) {
						console.log(item);
					});
					
					var opponentDecks = opponents.find(isChosen(opponentId)).decks;
					
					function isChosen(iteratorId, chosenId){
						return iteratorId == chosenId;
					}
										
					$.each(opponentDecks, function (i, item) {
						decks.append($('<option>', {
					        value: item,
					        text: item
					    }));
					});
				}
			</script>			

			
  			<div class="form-group">
    			<label for="comment">Comment:</label>
    			<input type="text" class="form-control" id="comment" name="theme">
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

