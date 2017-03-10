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
		    
		    $http(getUpdatesReq).then(function(response){
		    	
				$scope.result = response.data.result;
				
				if (response.data.result == "Success"){
					$scope.playername = JSON.parse(response.data.player).playername;
					$scope.unconfirmed = JSON.parse(response.data.unconfirmed);
					$scope.$emit('unconfirmed', $scope.unconfirmed);
				}
		    	
		    	}, 
		    	function(){
		    		$scope.result = 'Failure'
		    	});
		    
		    $scope.goAddDeck = function() {
		        $location.url('/adddeck');
		    };
		    
		    $scope.goDeckList = function() {
		        $location.url('/decklist');
		    };
		    
		    $scope.goConfirmList = function() {
		        $location.url('/confirmlist');
		    };
		    
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});