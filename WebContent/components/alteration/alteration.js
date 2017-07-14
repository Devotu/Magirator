ratorApp.controller('alterationController', function($scope, $http, $location, playerService, requestService, varStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.alterationId = varStorage.getGoTo();			
				
			$scope.getAlteration = function(){
				
				// Get alteration
				var getAlterationReq = requestService.buildRequest(
						"GetAlteration", 
						{id:$scope.alterationId}
						);
		
				$http(getAlterationReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.alteration = JSON.parse(response.data.alteration);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getAlteration();
	
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});

});