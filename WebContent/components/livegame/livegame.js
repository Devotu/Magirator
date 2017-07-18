ratorApp.controller('liveGameController', function ($scope, $http, $location, requestService, varStorage) {
	
	//INIT//
	$scope.participants = [];
	$scope.live_id = "";
	
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
			$scope.live_id = response.data.id;
			console.log($scope.live_id);
			
			$scope.updateStatus();
		}
    	
    	}, 
    	function(){
    		$scope.result = $scope.result + 'Failure getting Id';
    	});
	
	//Hämta current status
    //Uppdatera vyn
	$scope.updateStatus = function(){
		
		console.log("lvie id:" + $scope.live_id);
		
		var getStatusReq = requestService.buildRequest(
			"API/gamestatus", 
			{
				live_id: $scope.live_id
			}
		);
		
		console.log(getStatusReq);
		
	    $http(getStatusReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				var new_updated_participants = JSON.parse(response.data.participants).participants; //TODO Testa angular.toJson
				if(new_updated_participants != $scope.participants){
					console.log(new_updated_participants);
					console.log($scope.participants);
					console.log("updationg participants");
					$scope.participants = JSON.parse(response.data.participants).participants;
				}
				
				console.log($scope.participants);
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure'
	    	});				
	}
	
	//$scope.updater = setInterval($scope.updateStatus, 1000 * 2 * 5);
	//clearInterval($scope.updater);
	
	//$scope.updateStatus();
	
	
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