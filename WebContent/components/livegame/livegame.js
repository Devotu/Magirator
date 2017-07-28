ratorApp.controller('liveGameController', function ($scope, $http, $location, requestService, varStorage) {
	
	//INIT//
	$scope.participants = [];
	$scope.checksum = 0;
	$scope.live_id = "";
	$scope.lifeUpdates = [];
	$scope.is_admin = false;
	$scope.lifeUpdate = null;
	$scope.tab = 'Life';
	$scope.next_death;
	$scope.self_tags = [{id:1, text:"hk", polarity:1}, {id:2, text:"hk2", polarity:-1}];
	
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
			
			$scope.updateStatus();
			$scope.updateTags();			
		}
    	
    	}, 
    	function(){
    		$scope.result = $scope.result + 'Failure getting Id';
    	});
    
	//Hämta om player är admin
	var getPlayerIsAdminReq = requestService.buildRequest(
		"API/isadmin",
		{
			token: $scope.player_token
		}
	);
	
    $http(getPlayerIsAdminReq).then(function(response){
    	
		$scope.result = response.data.result;
		
		if (response.data.result == "Success"){
			$scope.is_admin = response.data.is_admin;
		}
    	
    	}, 
    	function(){
    		$scope.result = $scope.result + 'Failure getting admin';
    	});
	
	//Get current status
    //Update view if needed
	$scope.updateStatus = function(){
		
		var getStatusReq = requestService.buildRequest(
			"API/gamestatus", 
			{
				live_id: $scope.live_id,
				token: $scope.player_token
			}
		);
		
	    $http(getStatusReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			console.log(response.data);
			
			if (response.data.result == "Success"){
				var new_status = JSON.parse(response.data.status);
				
				if(new_status.checksum != $scope.checksum){
					$scope.participants = new_status.participants;
					$scope.checksum = new_status.checksum;
					$scope.next_death = new_status.next_death;
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
	//Update life
	$scope.addToLifeChange = function(player_token, change){
		
		clearInterval($scope.lifeUpdater);
		
		var i = 0;
		var done = false;
		
		while (i < $scope.lifeUpdates){
	        if (player_token === $scope.lifeUpdates[i].player_token) {
	        	$scope.lifeUpdates[i].new_life = $scope.lifeUpdates[i].new_life + change;
	        }
	        i++;
		}
		
		if (!done){
			
			while (i < $scope.participants.length){
		        if (player_token === $scope.participants[i].player_token) {
		        	
		        	$scope.lifeUpdates.push( { player_token: player_token, new_life: $scope.participants[i].life + change } );
		        }
		        i++;
			}
		}
				
		$scope.lifeUpdater = setInterval($scope.alterLife, 1000 * 2);
	}
	
	
	$scope.alterLife = function(){

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
	$scope.declareDead = function(player_token){
		
		var declareDeadReq = requestService.buildRequest(
			"API/declaredead", 
			{
				live_id: $scope.live_id,
				token: player_token,
				place: $scope.next_death
			}
		);
		
	    $http(declareDeadReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.updateStatus();
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure declaring dead';	    			
	    	});				
	}
	
	$scope.updateTags = function(){
		
		console.log("updating tags");
		
		var getTagReq = requestService.buildRequest(
			"API/gettags", 
			{
				live_id: $scope.live_id,
				token: $scope.player_token
			}
		);
		
	    $http(getTagReq).then(function(response){
	    	
			$scope.result = response.data.result;
			console.log(response.data);
			
			if (response.data.result == "Success"){
				$scope.self_tags = JSON.parse(response.data.tags);
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure fetching tags.';	    			
	    	});				
	}
	
	$scope.addTag = function(token, text, polarity){
		
		console.log("adding tag");
		
		var addTagReq = requestService.buildRequest(
			"API/addtag", 
			{
				live_id: $scope.live_id,
				token: token,
				text: text,
				polarity: polarity
			}
		);
		
	    $http(addTagReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.updateTags();
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure adding tag';	    			
	    	});				
	}
	
	$scope.deleteTag = function(tag_id){
		
		console.log("removing tag");
		
		var removeTagReq = requestService.buildRequest(
			"API/removetag", 
			{
				live_id: $scope.live_id,
				token: $scope.player_token,
				tag_id: tag_id
			}
		);
		
	    $http(removeTagReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.updateTags();
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure adding tag';	    			
	    	});				
	}
	
	//Rate game
	//Leave game
	
	//ADMIN//
	//(Admin)
	//Remove player
	//Kill player
	
	//Abort game
	$scope.cancelGame = function(){
		
		clearInterval($scope.lifeUpdater);
		
		var cancelGameReq = requestService.buildRequest(
			"API/cancelgame", 
			{
				live_id: $scope.live_id,
				token: $scope.player_token
			}
		);
		
	    $http(cancelGameReq).then(function(response){
	    	
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$location.url('/dashboard');
			}
	    	
	    	}, 
	    	function(){
	    		$scope.result = 'Failure aborting game';	    			
	    	});				
	}
		
});