<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
	<form id="isSpawnDeckForm">
		<div class="form-group">
    		<label for="name">Name:</label>
    		<input type="text" class="form-control" id="idDeckName" name="deckName">
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
	</form>
</div>