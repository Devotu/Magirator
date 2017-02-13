var ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);

/// ROUTES ///

ratorApp.config(function($routeProvider) {
	$routeProvider

	.when('/', {
		templateUrl : 'pages/login.html',
		controller : 'mainController'
	})
	
	.when('/login', {
		templateUrl : 'pages/login.html',
		controller : 'loginController'
	})

	.when('/signup', {
		templateUrl : 'pages/signup.html',
		controller : 'signupController'
	})

	.when('/about', {
		templateUrl : 'pages/about.html',
		controller : 'aboutController'
	})

	.when('/modal', {
		templateUrl : 'pages/modal.html',
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
	})
	
	.when('/deck', {
		templateUrl : 'pages/viewdeck.html',
		controller : 'viewdeckController'
	})	
	;
});


/// SERVICES ///

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

ratorApp.factory('tempStorage', function() {
	 var savedData = {}
	 function set(data) {
	   savedData = data;
	 }
	 function get() {
	  return savedData;
	 }

	 return {
	  set: set,
	  get: get
	 }

	});


/// CONTROLLERS ///

ratorApp.controller('mainController', function($scope, $http, $location, playerService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			$location.url('/dashboard');
		} else {
			$location.url('/login');
		}
	});
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
		} else {
			$scope.result = 'Not logged in, please log in and try again';
		}
	});
});

ratorApp.controller('decklistController', function($scope, $http, $location, playerService, requestService, tempStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			//Get decks
			var getDecksReq = requestService.buildRequest(
					"GetDecks", 
					{}
					);

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
			
		    $scope.order = {
		            field: 'name',
		            reverse: false
		        };
		    
		    $scope.reverseOrder = false;
		    
		    var bool_order = {
		            true: 0,
		            false: 1
		        };
		    
		    var bool_badge = {
		            true: 'yes',
		            false: 'no'
		        };
			
			
			$scope.deckOrder = function(deck) {
				var sorter = $scope.order.field;
				var sortval = deck[sorter];
				deck.badge = sortval;		

				//Special cases value
				if (sortval === true || sortval === false){
					boolval = sortval;
					sortval = bool_order[boolval];
					deck.badge = bool_badge[boolval];
				}
				
				//Special cases sorter
				if (sorter == 'name'){//Name is always displayed
					deck.badge = "";
				}		
				
		        return sortval;
			}
			
			$scope.goDeck = function(id){
				tempStorage.set(id);
				$location.url('/deck');
			}
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
		}
	});
});


ratorApp.controller('viewdeckController', function($scope, $http, $location, playerService, requestService, tempStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			$scope.showDelete = false;
			
			$scope.deckId = tempStorage.get();
			
			//Tabs
		    $scope.tab = 1;

		    $scope.setTab = function(newTab){
		      $scope.tab = newTab;
		    };

		    $scope.isSet = function(tabNum){
		      return $scope.tab === tabNum;
		    };
			
			//Deck
			$scope.result = "getting deck " + $scope.deckId;
			
			$scope.getDeck = function(){
				
				//Get deck
				var getDeckReq = requestService.buildRequest(
						"GetDeck", 
						{id:$scope.deckId}
						);

				$http(getDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.deck = JSON.parse(response.data.deck);							
							$scope.deckname = $scope.deck.name;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			//$scope.getDeck();
			
			$scope.doStuff = function(){
				console.log('test');
			}
			
			$scope.toggleActive = function(){
				
				console.log($scope.deck.deckid);
				
				//Toggle deck
				var toggleDeckReq = requestService.buildRequest(
						"ToggleDeck", 
						{id:$scope.deck.deckid}
						);

				$http(toggleDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});
			}
			
			$scope.deleteDeck = function(){
				
				//Toggle deck
				var deleteDeckReq = requestService.buildRequest(
						"DeleteDeck", 
						{id:$scope.deck.deckid}
						);

				$http(deleteDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
						}
					}, 
					function(){
						$scope.result = 'Failure';
					});
			}
			
			$scope.showDeleteDialog = function(){
				
				//Toggle delete dialog on
				$scope.showDelete = true;
			}
			
			$scope.toggleDeleteDialog = function(){
				
				//Toggle delete dialog
				($scope.showDelete == true) ? $scope.showDelete = false : $scope.showDelete = true;
			}
			
			//Games
			
			
			//Stats
			
			//Alterations
			
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
		}
	});
});