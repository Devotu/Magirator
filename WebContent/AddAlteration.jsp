<!DOCTYPE html>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/segments/headofpage.jspf" />
<script>
$(document).ready(function() {    
    
    $('#name').val('${deck.name}');
    $('#format select').val('${deck.format}');   
    
    $('#black').prop('checked', ${deck.black});
    $('#white').prop('checked', ${deck.white});
    $('#red').prop('checked', ${deck.red});
    $('#green').prop('checked', ${deck.green});
    $('#blue').prop('checked', ${deck.blue});
    $('#colorless').prop('checked', ${deck.colorless});
    
    $("#theme").val('${deck.theme}');
    
});
</script>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="page" value="Main" />
	<jsp:param name="eol" value="true" />
</jsp:include>

<div class="container-fluid text-center">
	<div class="row content">
		<div class="col-sm-2 sidenav"></div>
		<div class="col-sm-8 text-left">
			<h1>Alter deck</h1>

			<div>
				<div class="form-group">
					<label for="comment">Comment:</label> <input type="text"
						class="form-control" id="comment" name="comment">
				</div>

				<jsp:include page="/segments/addcancelbutton.jsp" />
			</div>

			<div>
				<h3>New Deck details</h3>
				<form action="/Magirator/Magirator" method="post">
					<input type="hidden" name="controllers" value="/AddDeck"> 
					<input type="hidden" name="goView" value="/Main.jsp"> 
					<input type="hidden" name="altView" value="/Main.jsp"> 
					<input type="hidden" name="errorView" value="/ErrorPage.jsp">
					<div class="form-group">
						<label for="name">Name:</label> 
						<input type="text" class="form-control" id="name" name="name" autofocus>
					</div>
					<div class="form-group">
						<label for="format">Format:</label> <select class="form-control" id="format" name="format">
							<option value="Standard">Standard</option>
							<option value="Block">Block</option>
							<option value="Pauper">Pauper</option>
						</select>
					</div>
					<div class="checkbox">
						<label><input id="black" type="checkbox" name="colors" value="Black">Black</label>
						<label><input id="white" type="checkbox" name="colors" value="White">White</label>
						<label><input id="red" type="checkbox" name="colors" value="Red">Red</label>
						<label><input id="green" type="checkbox" name="colors" value="Green">Green</label>
						<label><input id="blue" type="checkbox" name="colors" value="Blue">Blue</label>
						<label><input id="colorless" type="checkbox" name="colors" value="Colorless">Colorless</label>
					</div>
					<div class="form-group">
						<label for="theme">Theme:</label> 
						<input type="text" class="form-control" id="theme" name="theme" value="${deck.theme}">
					</div>
			</div>




			</form>
		</div>
		<jsp:include page="/segments/ads.jspf" />
	</div>
</div>

<footer class="container-fluid text-center"> </footer>

<jsp:include page="/segments/endofpage.jspf" />

