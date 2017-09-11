ratorApp.controller('gameController', function($scope, $http, $location, playerService, requestService, varStorage) {
	
	$scope.result = "Waiting for response";
	
	$scope.participants = [];
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.player = JSON.parse( data.player );

			$scope.gameId = varStorage.getGoTo();
			
			$scope.rating = {'speed': 0, 'strength': 0, 'synergy': 0, 'control': 0};
			
			$scope.generalRating = function(){
				var total = 0;
				var keys = 0;
				for (var key in $scope.rating) {
					  if ($scope.rating.hasOwnProperty(key) && key != 'id') {
					    total += $scope.rating[key];
					    keys++;
					  }
					}
				return total/keys; 
				}
			
			$scope.setRatings = function(ps){
				for (i = 0; i < ps.length; i++) {
				    if (ps[i].rating != undefined){
				    	$scope.rating = ps[i].rating;
				    }
				}
			}
				
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
							$scope.setRatings($scope.participants);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getGame();

			
			$scope.ratingSystem = "General";

			$scope.toggleRatingSystem = function () {

				if ($scope.ratingSystem != "SSSC") {
					$scope.ratingSystem = "SSSC";
				} else {
					$scope.ratingSystem = "General";
				}
			};
	
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});