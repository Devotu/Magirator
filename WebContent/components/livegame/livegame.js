ratorApp.controller('liveGameController', function ($scope, $http, $location, requestService, varStorage) {
	
	//INIT//
	//Hämta player live player token	
	$scope.player_token = varStorage.getLiveToken();
	
	//Hämta player live game id	
	var getPlayerLiveGameIdReq = requestService.buildRequest(
			"API/playerlivegameid",
			{
				token: $scope.player_token
			}
			);
	
    $http(getPlayerLiveGameIdReq).then(function(response){
    	
		$scope.result = response.data.result;
		
		if (response.data.result == "Success"){
			$scope.game_id = response.data.id;
			console.log($scope.game_id);
		}
    	
    	}, 
    	function(){
    		$scope.result = $scope.result + 'Failure getting Id';
    	});
	
	//Hämta current status
	//Uppdatera vyn
	
	
	//RUN//
	//Repeat uppdatera
	 
	//Events
	//Switch tab
	//Update life
	//Declare dead
	//Add tags
	//Rate game
	//Leave game
	
	//ADMIN//
	//(Admin)
	//Remove player
	//Kill player
	//End game
		
});