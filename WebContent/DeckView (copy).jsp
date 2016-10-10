<!DOCTYPE html>
<%@ page isELIgnored="false" %>

<script src="/scripts/tabs.js"></script>

<style>
body {font-family: "Lato", sans-serif;}

ul.tab {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #f1f1f1;
}

/* Float the list items side by side */
ul.tab li {float: left;}

/* Style the links inside the list items */
ul.tab li a {
    display: inline-block;
    color: black;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
    transition: 0.3s;
    font-size: 17px;
}

/* Change background color of links on hover */
ul.tab li a:hover {
    background-color: #ddd;
}

/* Create an active/current tablink class */
ul.tab li a:focus, .active {
    background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
}
</style>

<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="eol" value="false"/>
</jsp:include>
  
<div class="container-fluid text-center">    
	<div class="row content">
		<div class="col-sm-1 sidenav">
		</div>
		<div class="col-sm-9 text-left">
			<h1>Template</h1>
			<ul class="nav nav-tabs">
				<li><a href="#">Home</a></li>
				<li><a href="#">Menu 1</a></li>
				<li><a href="#">Menu 2</a></li>
				<li><a href="#">Menu 3</a></li>
			</ul>
		</div>    
		<jsp:include page="/segments/ads.jspf"/>
	</div>
</div>
			<ul class="tab">
				<li><a href="#" class="tablinks" onclick="openCity(event, 'London')">London</a></li>
				<li><a href="#" class="tablinks" onclick="openCity(event, 'Paris')">Paris</a></li>
				<li><a href="#" class="tablinks" onclick="openCity(event, 'Tokyo')">Tokyo</a></li>
			</ul>

<div id="London" class="tabcontent">
  <h3>London</h3>
  <p>London is the capital city of England.</p>
</div>

<div id="Paris" class="tabcontent">
  <h3>Paris</h3>
  <p>Paris is the capital of France.</p>
</div>

<div id="Tokyo" class="tabcontent">
  <h3>Tokyo</h3>
  <p>Tokyo is the capital of Japan.</p>
</div>

<jsp:include page="/segments/endofpage.jspf"/>

