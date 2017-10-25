ratorApp.controller('viewStatsController', function ($scope, $http, $location, playerService, requestService, varStorage) {
	
	//Get Graph types
	//Get Colors
	//Get Formats
	//Get Decks name,id
	//Get Opponents
	//Get Timespans
	
	$scope.games = [];
	$scope.selected_games = [];
	$scope.withs = [];
	$scope.againsts = [];
	$scope.winrate = 0;
	$scope.self_decks = [];
	$scope.opponent_decks = [];
	$scope.opponents = [];

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			//Data
			$scope.player = JSON.parse(data.player);
			
			// Get games
			var getGamesReq = requestService.buildRequest(
					"GetPlayerGames", 
					{}
					);

			$http(getGamesReq).then(function(response){
				$scope.result = response.data;
				
				if (response.data.result == "Success"){
					$scope.result = 'Success';
					$scope.games = response.data.games; console.log($scope.games);
					$scope.selected_games = $scope.games;
					calculateWinrate();
					populateOptions();
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
			});
			
			// Get withs
			$scope.with_options = ["Color", "Format", "Deck", "Active"];
			$scope.with_option = $scope.with_options[0];
			
			// Get colors
			$scope.colors = ["black", "white", "red", "green", "blue", "colorless"];
			$scope.with_colors = $scope.colors[0];
			
			// Get formats
			var getFormatsReq = requestService.buildRequest(
				"GetFormats",
				{}
			);

			$http(getFormatsReq).then(function (response) {
				$scope.formats = response.data;
				$scope.with_format = $scope.formats[0];
			},
			function () {
				$scope.result += "Could not get formats";
			});
			
			
			//Logic
			var getFilter = function(game){
				
				switch(this.what) {
			    case 'Color':
			    	return (game.deck[this.value] == 1);
			    case 'Format':
			    	return game.deck.format == this.value;
			    case 'Deck':
			    	return game.deck.name == this.value;
			    case 'Active':
			    	if(this.value == 'true'){
			    		return game.deck.active;
			    	}
			    	return !game.deck.active;
			    default:
			    	throw new Error('Invalid filter.');
				}
			}
			
			var calculateWinrate = function(){
				$scope.selected_games = $scope.games;
				
				$scope.withs.forEach(function(w){
					$scope.selected_games = $scope.selected_games.filter(getFilter, w);
				});
				
				console.log($scope.selected_games);
				
				if($scope.selected_games.length > 0){
					var wins = 0;
					$scope.selected_games.forEach(function(game, i){
						if(game.place == 1){
							wins++;
						}
					});
	
					$scope.winrate = Math.round((wins/$scope.selected_games.length)*100);
				} else {
					$scope.winrate = 0;
				}
			}
			
			$scope.addWith = function(){
				switch($scope.with_option) {
			    case 'Color':
			    	$scope.withs.push({what:'Color', value:$scope.with_color});
			    	break;
			    case 'Format':
			    	$scope.withs.push({what:'Format', value:$scope.with_format});
			    	break;
			    case 'Deck':
			    	$scope.withs.push({what:'Deck', value:$scope.with_deck});
			    	break;
			    case 'Active':
			    	$scope.withs.push({what:'Active', value:$scope.with_active});
			    	break;
			    default:
			    	throw new Error('Invalid with.');
				}
				calculateWinrate();				
			}
			
			$scope.removeWith = function(remove_with){
				remove_index = $scope.withs.indexOf(remove_with);
				$scope.withs.splice(remove_index,1);
				calculateWinrate();	
			}
			
			var populateOptions = function(){
				$scope.games.forEach(function(game, i){
					pushIfNotPresent($scope.self_decks, game.deck.name);
					pushIfNotPresent($scope.opponent_decks, game.opponent_deck.name);
					pushIfNotPresent($scope.opponents, game.opponent.name);
				});
			}
			
			var pushIfNotPresent = function(array, item){
				if(array.indexOf(item) == -1){
					array.push(item);
				}
			}

		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});