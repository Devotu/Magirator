ratorApp.controller('dashboardController', function($scope, $http, $location, playerService, requestService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			// Get updated info
			var getUpdatesReq = requestService.buildRequest(
					"GetDashboard", 
					{}
					);
		    
		    $scope.unconfirmed = 0;
		    $scope.liveGame = 'none';
		    
		    $http(getUpdatesReq).then(function(response){
		    	
				$scope.result = response.data.result;
				
				if (response.data.result == "Success"){
					$scope.playername = JSON.parse(response.data.player).name;
					$scope.unconfirmed = JSON.parse(response.data.unconfirmed);
					$scope.$emit('unconfirmed', $scope.unconfirmed);
				}
		    	
		    	}, 
		    	function(){
		    		$scope.result = 'Failure'
		    	});
		    
			$scope.hasLiveGame = function(){
				
				var hasLiveGameReq = requestService.buildRequest(
						"HasLiveGame", 
						{id:$scope.deckId}
						);

				$http(hasLiveGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.liveGame = response.data.live;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.hasLiveGame();
			
		    $scope.goAddDeck = function() {
		        $location.url('/adddeck');
		    };
		    
		    $scope.goDeckList = function() {
		        $location.url('/decklist');
		    };
		    
		    $scope.goConfirmList = function() {
		        $location.url('/confirmlist');
		    };
			
			$scope.goLiveGame = function(){
				$location.url('/play');
			}
			
			$scope.goStartGame = function(){
				$location.url('/init');
			}
			
			$scope.goJoinGame = function(){
				$location.url('/join');
			}
		    
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});