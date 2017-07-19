ratorApp.controller('liveGameController', function ($scope, $http, $location, requestService, varStorage) {
	
	//INIT//
	$scope.participants = [];
	$scope.checksum = 0;
	$scope.live_id = "";
	$scope.lifeUpdates = [];
	
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
	
	//Get current status
    //Update view if needed
	$scope.updateStatus = function(){
		
		var getStatusReq = requestService.buildRequest(
			"API/gamestatus", 
			{
				live_id: $scope.live_id
			}
		);
		
	    $http(getStatusReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				var new_updated_status = JSON.parse(response.data.status);
				
				
				if(new_updated_status.checksum != $scope.checksum){
					$scope.participants = new_updated_status.participants;
					$scope.checksum = new_updated_status.checksum;
				}
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