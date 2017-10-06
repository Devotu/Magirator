ratorApp.controller('dashboardController', function($scope, $http, $location, playerService, settingsService, requestService, varStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			$scope.updatePlayer(JSON.parse(data.player)); 
			
			$scope.settings = settingsService.getSettings();
			
			if ($scope.settings == undefined){
				settingsService.loadSettings().then(function(data) {
					$scope.settings = data;
				});				
			}
			
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
				}
		    	
		    	}, 
		    	function(){
		    		$scope.result = 'Failure'
		    	});
		    
			$scope.hasLiveGame = function(){
				
				var hasLiveGameReq = requestService.buildRequest(
						"HasLiveGame", 
						{}
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
			
			$scope.goStartGame = function(){
				$location.url('/init');
			}
			
			$scope.goJoinGame = function(){
				$location.url('/join');
			}
			
			$scope.goStats = function(){
				$location.url('/stats');
			}
			
			$scope.goToGame = function(){
				
				var getPlayerLiveGameTokenReq = requestService.buildRequest(
						"GetPlayerLiveGameToken", 
						{}
						);
				
			    $http(getPlayerLiveGameTokenReq).then(function(response){
			    	
					$scope.result = response.data.result;
					
					if (response.data.result == "Success"){
						varStorage.setLiveToken(response.data.token);
						$location.url('/play');
					}
			    	
			    	}, 
			    	function(){
			    		$scope.result = 'Failure'
			    	});				
			}
		    
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});