ratorApp.controller('gameController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	$scope.participants = [];
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.player = JSON.parse( data.player );

			$scope.gameId = deckVarStorage.getGoTo();
			
			$scope.rating = [0,0,0,0];
			$scope.generalRating = function(){ return ($scope.rating.reduce(function(a, b) { return a + b; }, 0))/4; }
			
			$scope.setRating = function(r){
				console.log(r['speed']);
				$scope.rating = [r['speed'], r['strength'], r['synergy'], r['control']];
			}
			
			$scope.setRatings = function(ps){
				for (i = 0; i < ps.length; i++) {
					console.log(ps[i].rating);
				    if (ps[i].rating != undefined && ps[i].player.id == $scope.player.id){
				    	$scope.setRating(ps[i].rating)
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