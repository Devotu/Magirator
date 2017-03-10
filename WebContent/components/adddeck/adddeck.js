ratorApp.controller('addDeckController', function($scope, $http, $location, playerService, requestService) {
    
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {			

			// Get formats
			var getFormatsReq = requestService.buildRequest(
					"GetFormats", 
					{}
					);
		    
		    $http(getFormatsReq).then(function(response){
		    	$scope.formats = response.data;
		    	$scope.format = $scope.formats[0];
		    	}, 
		    	function(){
		    		$scope.result += "Could not get formats";
		    	});
		
			// Add deck
			$scope.addDeck = function(){
				$scope.result = "Waiting for response";
				var addDeckReq = requestService.buildRequest(
						"AddDeck", 
						{
							deck: {
								'name': $scope.deck.name,
								'format': $scope.deck.format,
								'black': $scope.deck.black,
								'white': $scope.deck.white,
								'red': $scope.deck.red,
								'green': $scope.deck.green,
								'blue': $scope.deck.blue,
								'colorless': $scope.deck.colorless,
								'theme': $scope.deck.theme,
								'created': Date.now()
							}
						}
				);
		
				$http(addDeckReq).then(function(response){
					$scope.result = response.data
					
					if (response.data.result == "Success"){
						$location.url('/dashboard');		
					}
					
					}, 
					function(){
						$scope.result = 'Failure'
				});				
			};
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});