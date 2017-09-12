ratorApp.controller('viewStatsController', function ($scope, $http, $location, playerService, requestService, varStorage) {
	
	$scope.live_id = "";
	
	//If player logged in - else display message
	
	//Get Graph types
	//Get Colors
	//Get Formats
	//Get Decks name,id
	//Get Opponents
	//Get Timespans
	
	

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.player = JSON.parse(data.player);
			
			// Get decks
			var getDecksReq = requestService.buildRequest(
					"GetDeckList", 
					{}
					);

			$http(getDecksReq).then(function(response){
				$scope.result = response.data;
				
				if (response.data.result == "Success"){
					$scope.result = 'Success';
					$scope.decks = JSON.parse(response.data.decks);
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
			});
			


		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});