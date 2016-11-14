<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="Info" class="tabcontent">
	<h1>Alteration</h1>
	<label for="name">Comment:</label>
	<p>${alteration.comment}</p>
	<c:if test="${alteration.nameWas != alteration.nameIs}">
		<label for="name">Name:</label>
		<p>${alteration.nameWas} </p>  
		<span class="glyphicon glyphicon-arrow-down" aria-hidden="true"></span>
		<p>${alteration.nameIs} </p>
	</c:if>
	<c:if test="${alteration.formatWas != alteration.formatIs}">
		<label for="name">Format:</label>
		<p>${alteration.formatWas}</p>
		<span class="glyphicon glyphicon-arrow-down" aria-hidden="true"></span>
		<p>${alteration.formatIs}</p>
	</c:if>
	<c:if test="${alteration.colorPatternWas != alteration.colorPatternIs}">
	<label for="name">Colors:</label>
	<div>
		 <table>
  			<tr>
    			<th>
					<c:choose>
						<c:when test="${true == alteration.blackWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="black"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
    			<th>
					<c:choose>
						<c:when test="${true == alteration.whiteWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="white"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.redWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="red"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.greenWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="green"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.blueWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="blue"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.colorlessWas}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="lightgray"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
  			</tr>
  			<tr>
    			<th>
					<c:choose>
						<c:when test="${true == alteration.blackIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="black"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
    			<th>
					<c:choose>
						<c:when test="${true == alteration.whiteIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="white"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.redIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="red"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.greenIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="green"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.blueIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="blue"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
				<th>
					<c:choose>
						<c:when test="${true == alteration.colorlessIs}">
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="gray" stroke-width="4" fill="lightgray"/>
						</c:when>
						<c:otherwise>
							<svg viewBox="0 0 100 100" class="mana-image"><circle cx="50" cy="50" r="40" stroke="lightgray" stroke-width="4" fill="lightgray"/>
						</c:otherwise>
					</c:choose>
				</th>
  			</tr>
		</table>
	</div>
	</c:if>
	<c:if test="${alteration.themeWas != alteration.themeIs}">
		<label for="name">Theme:</label>
		<p>${alteration.themeWas}</p>
		<span class="glyphicon glyphicon-arrow-down" aria-hidden="true"></span>
		<p>${alteration.themeIs}</p>
	</c:if>
	<label for="name">Altered:</label>
	<p>${alteration.altered}</p>
</div>

