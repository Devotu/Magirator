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