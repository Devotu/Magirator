<!DOCTYPE html>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="/Magirator/Magirator" method="post">  				
	<input type="hidden" name="goView" value="${requestScope.goView}">
	<input type="hidden" name="controllers" value="${requestScope.controllers}">
	<input type="hidden" name="errorView" value="${requestScope.errorView}">
	<ul class="list-group sortable">
		<c:forEach items="${requestScope.listContainer.listItems}" var="listItem">
			<button	class="list-group-item filterable
			    <c:choose>
        			<c:when test="${listItem.colorCode == 0}"></c:when>
        			<c:when test="${listItem.colorCode == 1}">color-Bad</c:when>
        			<c:when test="${listItem.colorCode == 2}">color-Negative</c:when>
        			<c:when test="${listItem.colorCode == 3}">color-Neutral</c:when>
        			<c:when test="${listItem.colorCode == 4}">color-Positive</c:when>
        			<c:when test="${listItem.colorCode == 5}">color-Good</c:when>
        			<c:otherwise>undefined</c:otherwise>
    			</c:choose>
				<c:forEach items="${listItem.filterables}" var="filterable">
					${filterable.value}
				</c:forEach> sortee" 
				<c:forEach items="${listItem.sortables}" var="sortable">
					data-${sortable.key}="${sortable.value}"
				</c:forEach>
				type="submit" name="id" value="${listItem.id}">${listItem.displayname}</button>
			</li>
		</c:forEach>
	</ul>
</form>
