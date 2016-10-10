<!DOCTYPE html>
<%@ page isELIgnored="false" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="true"/>
</jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-2 sidenav">
    </div>
    <div class="col-sm-8 text-left">
      <h1>Add Game</h1>
	 	<form action="/Magirator/Magirator" method="post">
			<input type="hidden" name="goView" value="/DeckView.jsp">
			<input type="hidden" name="altView" value="/Main.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">			
			<div class="form-group">
				<label for="opponent">Opponent:</label>
  				<select class="form-control" id="opponent" name="opponent">
    				<option value="Top">Top</option>
    				<option value="Rick">Ricksr</option>
    				<option value="Dan">Dan</option>
  				</select>
				</div>
  			<div class="checkbox">
  				<p>To become opponent deck</p>
  				<label for="colors">Opponent colors:</label>
    				<label><input type="checkbox" name="colors" value="Black">Black</label>
    				<label><input type="checkbox" name="colors" value="White">White</label>
    				<label><input type="checkbox" name="colors" value="Red">Red</label>
    				<label><input type="checkbox" name="colors" value="Green">Green</label>
    				<label><input type="checkbox" name="colors" value="Blue">Blue</label>
  			</div>
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
  			<div class="btn-group">
				<button type="submit" class="btn btn-warning" name="action" value="alt">Cancel</button>
  				<button type="submit" class="btn btn-success" name="action" value="go">Add</button>
			</div>
		</form>
    </div>
    <jsp:include page="/segments/ads.jspf"/>
  </div>
</div>

<footer class="container-fluid text-center">
	
</footer>

<jsp:include page="/segments/endofpage.jspf"/>

