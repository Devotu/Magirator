<!DOCTYPE html>
<%@ page isELIgnored="false" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Main"/>
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-1 sidenav">
    </div>
    <div class="col-sm-11 text-left">
      <h1>Welcome ${sessionScope.player.getName()}</h1>
    </div>
    <div class="container">
    	<div class="row">
  			<form class="col-sm-4" action="/Magirator/Magirator" method="post">  				
				<input type="hidden" name="goView" value="/AddDeck.jsp">
				<input type="hidden" name="errorView" value="/ErrorPage.jsp">
  				<button type="submit">
    					<h1>Add Deck</h1>
    					<p>Add a deck to your collection.</p>
  				</button>  				
  			</form>
  			<form class="col-sm-4" action="/Magirator/Magirator" method="post">
				<input type="hidden" name="controllers" value="/GetDecks">			
				<input type="hidden" name="goView" value="/DeckList.jsp">
				<input type="hidden" name="errorView" value="/ErrorPage.jsp">
  				<button type="submit">
    				<h1>View Decks</h1>
    				<p>Look at all your decks.</p>
  				</button>  				
  			</form>
  			<form class="col-sm-4" action="/Magirator/Magirator" method="post">
				<input type="hidden" name="confimedStatus" value="-1">
				<input type="hidden" name="controllers" value="/GetUserGames">			
				<input type="hidden" name="goView" value="/ConfirmGameList.jsp">
				<input type="hidden" name="errorView" value="/ErrorPage.jsp">
  				<button type="submit">
    				<h1>Confirm Games</h1>
    				<p>Confirm games played against your decks.</p>
  				</button>  				
  			</form>
		</div>
    </div>
  </div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>

