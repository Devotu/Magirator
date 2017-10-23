ratorApp.controller('viewStatsController', function ($scope, $http, $location, playerService, requestService, varStorage) {
	
	//Get Graph types
	//Get Colors
	//Get Formats
	//Get Decks name,id
	//Get Opponents
	//Get Timespans
	
	$scope.games = [];
	$scope.selected_games = [];
	$scope.winrate = 0;

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
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
					$scope.games = response.data.games;
					$scope.selected_games = $scope.games;
					calculateWinrate();
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
			});
			
			
			var calculateWinrate = function(){
				if($scope.selected_games.length > 0){
					var wins = 0;
					$scope.selected_games.forEach(function(game, i){
						if(game.place == 1){
							wins++;
						}
					});
	
					$scope.winrate = Math.round((wins/$scope.selected_games.length)*100);
				}
			}
			


		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});