<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="/Magirator/Magirator" method="post">  				
	<input type="hidden" name="goView" value="${requestScope.goView}">
	<input type="hidden" name="controller" value="${requestScope.controller}">
	<input type="hidden" name="errorView" value="${requestScope.errorView}">
	<ul class="list-group sortable" style="list-style-type:none">
		<c:forEach items="${requestScope.listItems}" var="listValue">
			<li 
				class="filterable ${listValue.format} sortee"
				data-name="${listValue.displayname}"
				>
				<button type="submit" class="list-group-item" name="id" value="${listValue.id}">${listValue.displayname}</button>
			</li>
		</c:forEach>
	</ul>
</form>
