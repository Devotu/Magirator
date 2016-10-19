<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand">${param.header}</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<li>
				<button data-toggle="collapse" data-target="#sort">
					<span class="glyphicon glyphicon-chevron-left">Sort</span>
				</button>
			</li>
			<li>
				<button data-toggle="collapse" data-target="#filter">
					<span class="glyphicon glyphicon-chevron-left">Filter</span>
				</button>
			</li>
			<li>
				<form>
					<button class="btn btn-success">
						<span type="submit" class="glyphicon glyphicon-plus"></span> Add
					</button>    			
					<input type="hidden" name="goView" value=${param.addView}>
					<input type="hidden" name="errorView" value="/ErrorPage.jsp">
				</form>
			</li>
		</ul>
	</div>

	<!-- Sort -->
	<div id="sort" class="well collapse">
		<select id="sortBy">
			<c:forEach items="${requestScope.listContainer.sortOptions}" var="sortOption">
				<option>${sortOption}</option>					
			</c:forEach>
			<!-- <option>Name</option> -->
		</select>
		<button onclick="sort(false)">
			<span class="glyphicon glyphicon-sort-by-alphabet"></span>
		</button>
		<button onclick="sort(true)">
			<span class="glyphicon glyphicon-sort-by-alphabet-alt"></span>
		</button>
	</div>
	<script>
		function sort(za){
			var sorter = $("#sortBy").val().toLowerCase();
			var items = $(".sortee");
			var order = 1;
			if (za) { order = -1; }

			items.sort(function (a,b) {
				var dataA = $(a).data(sorter);
				var dataB = $(b).data(sorter);				
				if (dataA < dataB) return order * -1;
    			if (dataA > dataB) return order * 1;
				return 0;
			});

			var ul = $(".sortable");
			$.each(items, function(i, li){
				ul.append(li);
			});
		}
	</script>

	<!-- Filter -->
	<div id="filter" class="well collapse">
		<select id="filterSelect" onchange="setFilterBy()">
			<c:forEach items="${requestScope.listContainer.filterOptions}" var="filterOption">
				<option value="
					<c:forEach items="${filterOption.value}" var="optionValue">
						:${optionValue}
					</c:forEach>
				">${filterOption.key}</option>					
			</c:forEach>
		</select>
		<select id="filterBy">
		<!--
			<option>Standard</option>
			<option>Block</option>
			<option>Pauper</option>
		-->
		</select>
		<button onclick="filter()">
			<span class="glyphicon glyphicon-filter"></span>
		</button>
		<button onclick="removeFilter()">
			<span class="glyphicon glyphicon-remove"></span>
		</button>
	</div>
	
	<script>
		function filter(){
			var filterValue = "." + $("#filterBy").val();
			$(".filterable").filter(filterValue).show();
			$(".filterable").not(filterValue).hide();	
		}
		function removeFilter(){
			$(".filterable").show();
		}
		function setFilterBy(){
			var filterValues = $("#filterSelect").val();
			filterValues = filterValues.split(":");
			
			for (i = 0; i < filterValues.length; i++) {
				filterValues[i] = filterValues[i].trim();
			}			

			filterValues = filterValues.filter(Boolean);
			
			var filterBySelector = $("#filterBy");
			filterBySelector.find('option').remove();
			
			$.each(filterValues, function (i, item) {
				filterBySelector.append($('<option>', {
			        value: item,
			        text: item
			    }));
			});
		}
	</script>
</nav>

