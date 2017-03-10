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