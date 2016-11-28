<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
	$(function() {
		var i, tabcontent, tablinks;
		var defaultTab = ${param.defaultTab};

	    // Get all elements with class="tabcontent" and hide them
	    tabcontent = document.getElementsByClassName("tabcontent");
	    for (i = 0; i < tabcontent.length; i++) {
	        tabcontent[i].style.display = "none";
	    }

	    // Get all elements with class="tablinks" and remove the class "active"
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }
	    
	    // Show the current tab, and add an "active" class to the link the requested tab
	    document.getElementById('${param.defaultTab}').style.display = "inline";
	    document.getElementById('id_${param.defaultTab}').className += " active";
	});
	
	function openTab(evt, tabName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    document.getElementById(tabName).style.display = "inline";
    evt.currentTarget.className += " active";
}</script>

<ul class="nav nav-tabs">
	<c:forEach items="${paramValues.tabItems}" var="tabItem">
		<li><a href="#" id='id_${tabItem}' class="tablinks" onclick="openTab(event, '${tabItem}')">${tabItem}</a></li>
	</c:forEach>
</ul>
