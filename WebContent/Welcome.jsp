<!DOCTYPE html>
<jsp:include page="/segments/headofpage.jspf"/>
<jsp:include page="/segments/navbar.jsp">
	<jsp:param name="content" value="welcome"/>
</jsp:include> 

<script>
	function checkPasswords(){
		
		var valid = true;
		
		if($("#pwdInput").val() != $("#pwdConfirm").val()){
			valid = false;
			$("#pwdMatchAlert").show();
		} else {
			$("#pwdMatchAlert").hide();
		}
		
		if($("#pwdInput").val().length < 5){
			valid = false;
			$("#pwdShortAlert").show();
		} else {
			$("#pwdShortAlert").hide();
		}
		
		if(valid){
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					if(this.responseText != ""){
						$("#userCreated").show();
						$("#username").text(this.responseText);
						console.log(this.responseText);
						toggleSigninup();
					}					
				}				
        	}
			xhttp.open("POST", "/Magirator/Signup", true);
			xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    		xhttp.send("username=" + $("#signupName").val() + "&password=" + $("#pwdInput").val());
		}		
	}

	function toggleSigninup(){
		$("#signin").toggle();
		$("#signup").toggle();
		$("#signinForm").toggle();
		$("#signupForm").toggle();
	}
</script>

<div class="container-fluid text-center">    
	<div class="row content">
		<div class="col-sm-2 sidenav">
		</div>
		<!--Signin-->
		<div id="signinForm" class="col-sm-8 text-left">
			<h1 id="title">Welcome</h1>
			<form action="/Magirator/Login" method="post">
				<input type="hidden" name="goView" value="/Main.jsp">
				<input type="hidden" name="errorView" value="/ErrorPage.jsp">
				<div id="userCreated" class="alert alert-success" role="alert" style="display: none">User created</div>
				<div class="form-group">
					<label for="username">Username:</label>
					<input type="text" class="form-control" id="username" name="username" autofocus>
				</div>
				<div class="form-group">
					<label for="pwd">Password:</label>
					<input type="password" class="form-control" id="pwd" name="password">
				</div>
				<button type="submit" class="btn btn-default">Login</button>
			</form>
		</div>
		<!--Signup-->
		<div id="signupForm" class="col-sm-8 text-left" style="display: none">
			<form action="#" method="post">
				<input type="hidden" name="goView" value="/Welcome.jsp">
				<input type="hidden" name="errorView" value="/ErrorPage.jsp">
				<div id="pwdMatchAlert" class="alert alert-warning" role="alert" style="display: none">Passwords does not match</div>
				<div id="pwdShortAlert" class="alert alert-warning" role="alert" style="display: none">Password minimum length is 5</div>
				<div id="userExistsAlert" class="alert alert-warning" role="alert" style="display: none">Username already exists</div>
				<div class="form-group">
					<label for="username">Username:</label>
					<input id="signupName"type="text" class="form-control" name="username" autofocus>
				</div>
				<div class="form-group">
					<label for="pwd">Password:</label>
					<input id="pwdInput" type="password" class="form-control" name="password">
				</div>
				<div class="form-group">
					<label for="pwd">Confirm password:</label>
					<input id="pwdConfirm" type="password" class="form-control" name="passwordConfirmation">
				</div>
				<button type="button" class="btn btn-default" onclick="checkPasswords()">Sign up</button>
			</form>
		</div>
		<!--ADS-->
		<jsp:include page="/segments/ads.jspf"/>
	</div>
</div>

<jsp:include page="/segments/endofpage.jspf"/>

