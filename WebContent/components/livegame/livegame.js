ratorApp.controller('liveGameController', function ($scope, $http, $location, requestService, varStorage) {
	
	//INIT//
	$scope.participants = [];
	$scope.checksum = 0;
	$scope.live_id = "";
	$scope.lifeUpdates = [];
	
	$scope.lifeUpdate = null;
	
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
	    		$scope.result = 'Failure getting status'
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
	$scope.addToLifeChange = function(player_id, change){
		
		clearInterval($scope.lifeUpdater);
		
		var i = 0;
		var done = false;
		
		while (i < $scope.lifeUpdates){
	        if (player_id === $scope.lifeUpdates[i].player_id) {
	        	$scope.lifeUpdates[i].new_life = $scope.lifeUpdates[i].new_life + change;
	        }
	        i++;
		}
		
		if (!done){
			
			while (i < $scope.participants.length){
		        if (player_id === $scope.participants[i].player_id) {
		        	
		        	$scope.lifeUpdates.push( { player_id: player_id, new_life: $scope.participants[i].life + change } );
		        }
		        i++;
			}
		}
		
		console.log($scope.lifeUpdates);
		
		$scope.lifeUpdater = setInterval($scope.alterLife, 1000 * 2);
	}
	
	
	$scope.alterLife = function(){

		console.log($scope.live_id);
		console.log("altering life");
		clearInterval($scope.lifeUpdater);
		
		var alterLifeReq = requestService.buildRequest(
			"API/alterlife", 
			{
				live_id: $scope.live_id,
				token: $scope.player_token,
				life_updates: $scope.lifeUpdates
			}
		);
		
	    $http(alterLifeReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.updateStatus();
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure updating life'
	    	});				
	}
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