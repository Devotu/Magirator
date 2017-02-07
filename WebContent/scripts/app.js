// create the module and name it scotchApp
// also include ngRoute for all our routing needs
var ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);

// configure our routes
ratorApp.config(function($routeProvider) {
	$routeProvider

	// route for the home page
	.when('/', {
		templateUrl : 'pages/login.html',
		controller : 'loginController'
	})
	
	.when('/login', {
		templateUrl : 'pages/login.html',
		controller : 'loginController'
	})

	.when('/signup', {
		templateUrl : 'pages/signup.html',
		controller : 'signupController'
	})

	// route for the about page
	.when('/about', {
		templateUrl : 'pages/about.html',
		controller : 'aboutController'
	})

	// route for the contact page
	.when('/contact', {
		templateUrl : 'pages/contact.html',
		controller : 'contactController'
	})

	.when('/adddeck', {
		templateUrl : 'pages/adddeck.html',
		controller : 'addDeckController'
	})
	
	.when('/dashboard', {
		templateUrl : 'pages/dashboard.html',
		controller : 'dashboardController'
	})
	
	.when('/decklist', {
		templateUrl : 'pages/decklist.html',
		controller : 'decklistController'
	});
});



ratorApp.factory('playerService', function($http){
	
	return {		
		getPlayer: function(){			
			return $http.post('/Magirator/GetPlayer').then(function(response){
				if (response.data.result == "Success"){
					console.log("Logged in as " + JSON.parse( response.data.player ).playername );
				} else {
					console.log("Not logged in");
				}
				return response.data;
			});
		}
	}
});

ratorApp.factory('requestService', function(){
	
	return {
		buildRequest : function(endpoint, data) {
		return {
			method: 'POST',
			url: '/Magirator/' + endpoint,
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		}
	}
});


// create the controller and inject Angular's $scope
ratorApp.controller('mainController', function($scope) {
	// create a message to display in our view
	$scope.message = 'Everyone come and see how good I look!';
});

ratorApp.controller('aboutController', function($scope) {
	$scope.message = 'Look! I am an about page.';
});

ratorApp.controller('contactController', function($scope) {
	$scope.message = 'Contact us! JK. This is just a demo.';
});

ratorApp.controller('addDeckController', function($scope, $http, $location, playerService, requestService) {
    
	playerService.getPlayer().then(function(data){
		
		if (data.result == "Success"){
			
			$scope.player = JSON.parse(data.player);
			
			//Get formats
		    var getFormatsReq = {
		    		method: 'POST',
		    		url: '/Magirator/GetFormats'
		    }
		    
		    $http(getFormatsReq).then(function(response){
		    	$scope.formats = response.data;
		    	$scope.format = $scope.formats[0];
		    	}, 
		    	function(){
		    		$scope.result += "Could not get formats";
		    	});

			//Add deck
			$scope.addDeck = function(){
				$scope.result = "Waiting for response";
				var addDeckReq = requestService.buildRequest(
						"AddDeck", 
						{
							player: $scope.player,
							deck: {
								'name': $scope.name,
								'format': $scope.format,
								'colors': $scope.colors,
								'theme': $scope.theme,
								'created': Date.now()
							}
						});

				$http(addDeckReq).then(function(response){
					$scope.result = response.data
					
					if (response.data.result == "Success"){
						$location.url('/dashboard');		
					}
					
					}, 
					function(){
						$scope.result = 'Failure'
					});
			};
		}
	});
});

ratorApp.controller('loginController', function($scope, $http, $location) {
	
	//Login
	$scope.login = function(){
		
		$scope.result = "Waiting for response";
		
		var loginReq = {
				method: 'POST',
				url: '/Magirator/Login',
				headers: {
				   'Content-Type': 'application/json'
				},
				data: { 
					'username': $scope.username,
					'password': $scope.password
				}
		}

		$http(loginReq).then(function(response){
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$location.url('/dashboard');		
			}
			
			}, 
			function(){
				$scope.result = 'Failure';
			});
	};
	
    $scope.goSignup = function() {
        $location.url('/signup');
    };
});

ratorApp.controller('signupController', function($scope, $http, $location) {
	
	//Sign up
	$scope.signup = function(){
		
		$scope.result = "Waiting for response";
		
		var signupReq = {
				method: 'POST',
				url: '/Magirator/Signup',
				headers: {
				   'Content-Type': 'application/json'
				},
				data: { 
					'username': $scope.username,
					'password': $scope.password,
					'retype': $scope.retype,
					'playername': $scope.playername
				}
		}
		
		if( $scope.password === $scope.retype ){

			$http(signupReq).then(function(response){
				
					$scope.result = response.data.result;
				
					if (response.data.result == "Success"){
						$location.url('/');				
					}
				}, 
				function(){
					$scope.result = 'Request failure';
			});		
		} else {
			$scope.result = 'Passwords does not match';
		}
	};
});

ratorApp.controller('dashboardController', function($scope, $http, $location, playerService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			//Get updated info
		    var getUpdatesReq = {
		    		method: 'POST',
		    		url: '/Magirator/GetDashboard'
		    }
		    
		    $http(getUpdatesReq).then(function(response){
		    	
				$scope.result = response.data.result;
				
				if (response.data.result == "Success"){
					var player = JSON.parse(response.data.player);
					$scope.playername = player.playername;	
				}
		    	
		    	}, 
		    	function(){
		    		$scope.result = 'Failure'
		    	});
		    
		    $scope.goAddDeck = function() {
		        $location.url('/adddeck');
		    };
		    
		    $scope.goDeckList = function() {
		        $location.url('/decklist');
		    };
		}
	});
});

ratorApp.controller('decklistController', function($scope, $http, $location, playerService, requestService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			//Get decks
			var getDecksReq = requestService.buildRequest(
					"GetDecks", 
					{
						player: $scope.player
					});

			$http(getDecksReq).then(function(response){
				$scope.result = response.data;
				
				if (response.data.result == "Success"){
					$scope.result = 'Success';
					$scope.decks = JSON.parse(response.data.decks);
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
				});
		} else {
			$scope.result = 'Not logged in, please log in and try again';
		}
	});

});