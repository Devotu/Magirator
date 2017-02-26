var ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);

// / ROUTES ///

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
	
	.when('/alterdeck', {
		templateUrl : 'pages/alterdeck.html',
		controller : 'alterdeckController'
	})
	
	.when('/alteration', {
		templateUrl : 'pages/alteration.html',
		controller : 'alterationController'
	})
	
	.when('/addgame', {
		templateUrl : 'pages/addgame.html',
		controller : 'addGameController'
	})
	
	.when('/game', {
		templateUrl : 'pages/game.html',
		controller : 'gameController'
	})
	
	.when('/confirmlist', {
		templateUrl : 'pages/confirmlist.html',
		controller : 'confirmlistController'
	})
	
	.when('/confirm', {
		templateUrl : 'pages/confirm.html',
		controller : 'confirmController'
	})
	;
});


// / SERVICES ///

ratorApp.factory('playerService', function($http){
	
	return {		
		getPlayer: function(){			
			return $http.post('/Magirator/GetPlayer').then(function(response){			
			//return $http.post('/GetPlayer').then(function(response){
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
			//url: '/' + endpoint,
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		}
	}
});

ratorApp.factory('deckVarStorage', function() {
	 var currentDeck = {}
	 var goTo = {}
	 
	 function setCurrentDeck(data) {
		 currentDeck = data;
	 }
	 function getCurrentDeck() {
		 return currentDeck;
	 }
	 function setGoTo(data) {
		 goTo = data;
	 }
	 function getGoTo() {
		 return goTo;
	 }

	 return {
		 setCurrentDeck : setCurrentDeck,
		 getCurrentDeck : getCurrentDeck,
		 setGoTo : setGoTo,
		 getGoTo : getGoTo
	 }

	});


// / CONTROLLERS ///

ratorApp.controller('mainController', function($scope, $http, $location, playerService) {
	
	$scope.result = "Waiting for response";
	
	$scope.$on('unconfirmed', function (event, data) {
	    $scope.unconfirmed = data;
	  });
	
	$scope.$on('logged_in', function (event, data) {
	    $scope.logged_in = data;
	  });
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			$location.url('/dashboard');
		} else {
			$location.url('/login');
		}
	});
});


ratorApp.controller('loginController', function($scope, $http, $location, requestService) {
	
	// Login
	$scope.login = function(){
		
		$scope.result = "Waiting for response";
		
		var loginReq = requestService.buildRequest(
				"Login", 
				{
					'username': $scope.username,
					'password': $scope.password
				}
			);

		$http(loginReq).then(function(response){
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.$emit('logged_in', true);
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

ratorApp.controller('signupController', function($scope, $http, $location, requestService) {
	
	// Sign up
	$scope.signup = function(){
		
		$scope.result = "Waiting for response";
		
		var signupReq = requestService.buildRequest(
				"Signup", 
				{
					'username': $scope.username,
					'password': $scope.password,
					'retype': $scope.retype,
					'playername': $scope.playername
				}
			);
		
		var minlength = function(s){
			if(s.length > 7){
				return true;
			}
			return false;
		}
		
		var match = function(a, b){
			if (a === b){
				return true;
			}
			return false;
		}
				
		if( minlength($scope.password) ){
			
			if( match($scope.password, $scope.retype) ){
	
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
			
		} else {
			$scope.result = 'Passwords is to short, minimum length is 8 characters';
		}
	};
});

ratorApp.controller('dashboardController', function($scope, $http, $location, playerService, requestService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			// Get updated info
			var getUpdatesReq = requestService.buildRequest(
					"GetDashboard", 
					{}
					);
		    
		    $scope.unconfirmed = 0;
		    
		    $http(getUpdatesReq).then(function(response){
		    	
				$scope.result = response.data.result;
				
				if (response.data.result == "Success"){
					$scope.playername = JSON.parse(response.data.player).playername;
					$scope.unconfirmed = JSON.parse(response.data.unconfirmed);
					$scope.$emit('unconfirmed', $scope.unconfirmed);
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
		    
		    $scope.goConfirmList = function() {
		        $location.url('/confirmlist');
		    };
		    
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});

ratorApp.controller('addDeckController', function($scope, $http, $location, playerService, requestService) {
    
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {			

			// Get formats
			var getFormatsReq = requestService.buildRequest(
					"GetFormats", 
					{}
					);
		    
		    $http(getFormatsReq).then(function(response){
		    	$scope.formats = response.data;
		    	$scope.format = $scope.formats[0];
		    	}, 
		    	function(){
		    		$scope.result += "Could not get formats";
		    	});
		
			// Add deck
			$scope.addDeck = function(){
				$scope.result = "Waiting for response";
				var addDeckReq = requestService.buildRequest(
						"AddDeck", 
						{
							deck: {
								'name': $scope.deck.name,
								'format': $scope.deck.format,
								'black': $scope.deck.black,
								'white': $scope.deck.white,
								'red': $scope.deck.red,
								'green': $scope.deck.green,
								'blue': $scope.deck.blue,
								'colorless': $scope.deck.colorless,
								'theme': $scope.deck.theme,
								'created': Date.now()
							}
						}
				);
		
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
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});

ratorApp.controller('decklistController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			// Get decks
			var getDecksReq = requestService.buildRequest(
					"GetDeckList", 
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

				// Special cases value
				if (sortval === true || sortval === false){
					boolval = sortval;
					sortval = bool_order[boolval];
					deck.badge = bool_badge[boolval];
				}
				
				// Special cases sorter
				if (sorter == 'name'){// Name is always displayed
					deck.badge = "";
				}		
				
		        return sortval;
			}
			
			$scope.goDeck = function(id){
				deckVarStorage.setCurrentDeck(id);
				$location.url('/deck');
			}
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});


ratorApp.controller('viewdeckController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			$scope.deckId = deckVarStorage.getCurrentDeck();
			
			
			// Deck
			$scope.showDelete = false;
			
			$scope.getDeck = function(){
				
				// Get deck
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
						
			$scope.toggleActive = function(){
				
				console.log($scope.deck.deckid);
				
				// Toggle deck
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
				
				// Toggle deck
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
				
				// Toggle delete dialog on
				$scope.showDelete = true;
			}
			
			$scope.toggleDeleteDialog = function(){
				
				// Toggle delete dialog
				($scope.showDelete == true) ? $scope.showDelete = false : $scope.showDelete = true;
			}
			
			// Games
			$scope.getGames = function(){
				
				// Get Games
				var getGamesReq = requestService.buildRequest(
						"GetGames", 
						{id:$scope.deckId}
						);

				$http(getGamesReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.participations = JSON.parse(response.data.games);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.goAddGame = function(){
				
				deckVarStorage.setCurrentDeck($scope.deckId);
				$location.url('/addgame');
			}
			
			$scope.goGame = function(gameId){
				
				deckVarStorage.setGoTo(gameId);
				$location.url('/game');
			}
			
			// Stats
			
			
			// Alterations
			$scope.getAlterations = function(){
				
				// Get Alterations
				var getAlterationsReq = requestService.buildRequest(
						"GetAlterations", 
						{id:$scope.deckId}
						);

				$http(getAlterationsReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.alterations = JSON.parse(response.data.alterations);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.goAlter = function(){
				
				deckVarStorage.setCurrentDeck($scope.deckId);
				$location.url('/alterdeck');
			}
			
			$scope.goAlteration = function(alterationId){
				
				deckVarStorage.setGoTo(alterationId);
				$location.url('/alteration');
			}
			
			// Tabs
		    $scope.setTab = function(newTab){
		      $scope.tab = newTab;
		      switch(newTab) {
			      case 1:
			          $scope.getDeck();
			          break;
			      case 2:
			          $scope.getGames();
			          break;
			      case 4:
			          $scope.getAlterations();
			          break;
			      default:
			    	  $scope.result = "The tabcase defaulted";
		      } 
		    };

		    $scope.isSet = function(tabNum){
		      return $scope.tab === tabNum;
		    };
		    
		    $scope.setTab(1);
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
	
});
	
ratorApp.controller('alterdeckController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
		
		$scope.result = "Waiting for response";	
		
		playerService.getPlayer().then(function(data) {
			if (data.result == "Success") {

				$scope.deckId = deckVarStorage.getCurrentDeck();
				
				$scope.getFormats = function(){
				    
					var getFormatsReq = requestService.buildRequest(
							"GetFormats", 
							{}
							);
				    
				    $http(getFormatsReq).then(function(response){
				    	$scope.formats = response.data;
				    	$scope.format = $scope.formats[0];
				    	}, 
				    	function(){
				    		$scope.result += "Could not get formats";
				    	});
				}
		
				$scope.getFormats();
				
					
				$scope.getDeck = function(){
					
					// Get deck
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
				
				$scope.getDeck();
				
				
				$scope.alterDeck = function(){
					
					// Alter deck
					var AlterDeckReq = requestService.buildRequest(
							"AlterDeck", 
							{
								deck: {
									'id': $scope.deckId,
									'name': $scope.deck.name,
									'format': $scope.deck.format,
									'black': $scope.deck.black,
									'white': $scope.deck.white,
									'red': $scope.deck.red,
									'green': $scope.deck.green,
									'blue': $scope.deck.blue,
									'colorless': $scope.deck.colorless,
									'theme': $scope.deck.theme,
									'created': Date.now()
								},
								comment: $scope.comment
							});
		
					$http(AlterDeckReq).then(function(response){
						$scope.result = response.data;
						
							if (response.data.result == "Success"){
								deckVarStorage.setCurrentDeck(response.data.newDeckId);
								$location.url('/deck');
							}					
						}, 
						function(){
							$scope.result = 'Failure';
						});				
				}

			} else {
				$scope.result = 'Not logged in, please log in and try again';
				$location.url('/');
			}
		});
});


ratorApp.controller('alterationController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.alterationId = deckVarStorage.getGoTo();			
				
			$scope.getAlteration = function(){
				
				// Get alteration
				var getAlterationReq = requestService.buildRequest(
						"GetAlteration", 
						{id:$scope.alterationId}
						);
		
				$http(getAlterationReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.alteration = JSON.parse(response.data.alteration);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getAlteration();
	
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});

});


ratorApp.controller('addGameController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
    
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			$scope.player = JSON.parse( data.player );
			$scope.deckId = deckVarStorage.getCurrentDeck();
			
			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];
			
			$scope.getDeck = function(){
				
				// Get deck
				var getDeckReq = requestService.buildRequest(
						"GetDeck", 
						{id:$scope.deckId}
						);

				$http(getDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.playerdeck = JSON.parse(response.data.deck);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.getDeck();
		    
			// Get opponent decks
			$scope.getOpponentDecks = function(){
				
				var getOpponentDecksReq = requestService.buildRequest(
						"GetOpponentDeckList", 
						{id:$scope.addOpponent.id}
						);

				$http(getOpponentDecksReq).then(function(response){
					$scope.result = response.data;
					
					if (response.data.result == "Success"){
				    	$scope.decks = JSON.parse(response.data.decks);
				    	$scope.addDeck = $scope.decks[0];
					}
					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			// Get opponents
			var getOpponentsReq = requestService.buildRequest(
					"GetOpponents", 
					{}
					);
	
			$http(getOpponentsReq).then(function(response){
				$scope.result = response.data;
				
					if (response.data.result == "Success"){
				    	$scope.opponents = JSON.parse(response.data.opponents);
				    	$scope.addOpponent = $scope.opponents[0];
				    	$scope.getOpponentDecks();
					}					
				}, 
				function(){
					$scope.result = 'Failure';
			});			

			// Add Self
			$scope.addSelf = function(){
				
				$scope.participants.push(
					{
						deckId : $scope.playerdeck.deckid,
						place : $scope.participants.length +1,
						playerId : $scope.player.id,
						deckName : $scope.playerdeck.name,
						confirmed : true,
						comment : "",
						added : Date.now()
					}
				);				
			};
			
			// Add Participant
			$scope.addParticipant = function(){
				
				$scope.participants.push(
					{
						deckId : $scope.addDeck.id,
						place : $scope.participants.length +1,
						playerId : $scope.addOpponent.id,
						deckName : $scope.addDeck.name,
						confirmed : false,
						comment : "",
						added : Date.now()
					}
				);				
			};
			
			// Add game
			$scope.addGame = function(){
				$scope.result = "Waiting for response";
				
				console.log($scope.participants);				
				
				var updatePlayerEntry = function(participant){
					if (participant.deckId == $scope.deckId){
						participant.comment = $scope.comment; 
					}
				}
				
				$scope.participants.forEach(updatePlayerEntry);
				
				var addGameReq = requestService.buildRequest(
						"AddGame", 
						{
							participants : $scope.participants,
							draw : $scope.draw
						}
				);
		
				$http(addGameReq).then(function(response){
					$scope.result = response.data
					
					if (response.data.result == "Success"){
						deckVarStorage.setCurrentDeck($scope.deckId);
						$location.url('/deck');	
					}
					
					}, 
					function(){
						$scope.result = 'Failure'
				});				
			};
			
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});


ratorApp.controller('gameController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	$scope.participants = [];
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.gameId = deckVarStorage.getGoTo();			
				
			$scope.getGame = function(){
				
				// Get Game
				var getGameReq = requestService.buildRequest(
						"GetGame", 
						{id:$scope.gameId}
						);
		
				$http(getGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.participants = JSON.parse(response.data.participants);
							$scope.draw = $scope.participants[0].game.draw;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getGame();
	
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});

});


ratorApp.controller('confirmlistController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			// Unconfirmed games
			$scope.getUnconfirmed = function(){
				
				// Get Games
				var getUnconfirmedReq = requestService.buildRequest(
						"GetUnconfirmed", 
						{}
						);

				$http(getUnconfirmedReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.participations = JSON.parse(response.data.games);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.getUnconfirmed();
			
			$scope.goConfirm = function(gameId){
				
				deckVarStorage.setGoTo(gameId);
				$location.url('/confirm');
			}
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});


ratorApp.controller('confirmController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";	
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.gameId = deckVarStorage.getGoTo();
			$scope.player = JSON.parse( data.player );

			$scope.getGame = function(){
				
				// Get Game
				var getGameReq = requestService.buildRequest(
						"GetGame", 
						{id:$scope.gameId}
						);
		
				$http(getGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Got Game';
							$scope.participants = JSON.parse(response.data.participants);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getGame();
			
			var findSelf = function(p){
				return p.player.id == $scope.player.id;
			}
			
			$scope.confirm = function(response){
				
				$scope.self = $scope.participants.filter(findSelf)[0];
				
				// Confirm Game
				var confirmReq = requestService.buildRequest(
						"ConfirmGame", 
							{
								id : $scope.self.result.id,
								confirm : response,
								comment : $scope.comment
							}
						);
		
				$http(confirmReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Confirmed game';
							$location.url('/confirmlist');
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}

		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});