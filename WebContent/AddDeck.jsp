<!DOCTYPE html>
<%@ page isELIgnored="false" %>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-2 sidenav">
    </div>
    <div class="col-sm-8 text-left">
      <h1>Add Deck</h1>
	 	<form action="/Magirator/Magirator" method="post">
			<input type="hidden" name="controller" value="/AddDeck">
			<input type="hidden" name="goView" value="/Main.jsp">
			<input type="hidden" name="altView" value="/Main.jsp">
			<input type="hidden" name="errorView" value="/ErrorPage.jsp">
  			<div class="form-group">
    				<label for="name">Name:</label>
    				<input type="text" class="form-control" id="name" name="name" autofocus>
  			</div>				
			<div class="form-group">
				<label for="format">Format:</label>
  				<select class="form-control" id="format" name="format">
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
    				<input type="text" class="form-control" id="theme" name="theme">
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

