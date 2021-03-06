ratorApp.controller('viewStatsController', function ($scope, $http, $location, playerService, requestService, varStorage, settingsService) {
		
	$scope.games = [];
	$scope.selected_games = [];
	$scope.recent_games = [];
	$scope.withs = [];
	$scope.againsts = [];
	$scope.winrate = 0;
	$scope.self_decks = [];
	$scope.opponents_decks = [];
	$scope.opponents = [];

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			//Data//
			$scope.player = JSON.parse(data.player);
			
			$scope.settings = settingsService.getSettings();

			if ($scope.settings == undefined) {
				settingsService.loadSettings().then(function(data) {
					$scope.settings = data;
				});
			}
			
			// Get games
			var getGamesReq = requestService.buildRequest(
					"GetPlayerGames", 
					{}
					);

			$http(getGamesReq).then(function(response){
				$scope.result = response.data;
				
				if (response.data.result == "Success"){
					$scope.result = 'Success';
					$scope.games = response.data.games;
					$scope.selected_games = $scope.games;
					updateWinrate($scope.selected_games);
					populateOptions();
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
			});
			
			// Get withs
			$scope.with_options = ["Color", "Format", "Deck", "Active"];
			$scope.with_option = $scope.with_options[0];

			// Get aganists
			$scope.against_options = ["Color", "Format", "Deck", "Opponent"];
			$scope.against_option = $scope.against_options[0];
			
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
			
			
			//Logic//
			var updateWinrate = function(){
				$scope.selected_games = $scope.games;
				
				$scope.withs.forEach(function(w){
					$scope.selected_games = $scope.selected_games.filter(withFilter, w);
				});
				
				$scope.againsts.forEach(function(a){
					$scope.selected_games = $scope.selected_games.filter(againstFilter, a);
				});
				
				var yesterday = new Date().getTime() - (24 * 60 * 60 * 1000);
				$scope.recent_games = $scope.selected_games.filter(timeFilter, yesterday);
				
				$scope.winrate = calculateWinrate($scope.selected_games);
				$scope.recent_winrate = calculateWinrate($scope.recent_games);				
			}
			
			var calculateWinrate = function(games){
				if(games.length > 0){
					var wins = 0;
					games.forEach(function(game, i){
						if(game.place == 1){
							wins++;
						}
					});
	
					return Math.round((wins/games.length)*100);
				} else {
					return 0;
				}
			}
			
			var withFilter = function(game){
				
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
			    	throw new Error('Invalid with filter.');
				}
			}

			var againstFilter = function(game){
				
				switch(this.what) {
			    case 'Color':
			    	return (game.opponent_deck[this.value] == 1);
			    case 'Format':
			    	return game.opponent_deck.format == this.value;
			    case 'Deck':
			    	return game.opponent_deck.name == this.value;
			    case 'Opponent':
			    	return game.opponent.name == this.value;
			    default:
			    	throw new Error('Invalid against filter.');
				}
			}
			
			var timeFilter = function(game){
				return game.played > this;
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
				updateWinrate($scope.selected_games);				
			}

			$scope.addAgainst = function(){
				switch($scope.against_option) {
			    case 'Color':
			    	$scope.againsts.push({what:'Color', value:$scope.against_color});
			    	break;
			    case 'Format':
			    	$scope.againsts.push({what:'Format', value:$scope.against_format});
			    	break;
			    case 'Deck':
			    	$scope.againsts.push({what:'Deck', value:$scope.against_deck});
			    	break;
			    case 'Opponent':
			    	$scope.againsts.push({what:'Opponent', value:$scope.against_opponent});
			    	break;
			    default:
			    	throw new Error('Invalid with.');
				}
				updateWinrate($scope.selected_games);				
			}
			
			$scope.removeWith = function(remove_with){
				remove_index = $scope.withs.indexOf(remove_with);
				$scope.withs.splice(remove_index,1);
				updateWinrate($scope.selected_games);	
			}
			
			$scope.removeAgainst = function(remove_against){
				remove_index = $scope.againsts.indexOf(remove_against);
				$scope.againsts.splice(remove_index,1);
				updateWinrate($scope.selected_games);	
			}
			
			var populateOptions = function(){
				$scope.games.forEach(function(game, i){
					pushIfNotPresent($scope.self_decks, game.deck.name);
					pushIfNotPresent($scope.opponents_decks, game.opponent_deck.name);
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