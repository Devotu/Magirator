ratorApp.controller('requestresetController', function($scope, $http, $location, requestService) {
	
	$scope.request = function(){
		
		$scope.result = "Waiting for response";
		
		var resetReq = requestService.buildRequest(
				"RequestResetPassword", 
				{
					'username': $scope.username
				}
			);

		$http(resetReq).then(function(response){
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$location.url('/resetpassword');		
			}
			
			}, 
			function(){
				$scope.result = 'Failure';
			});
	};
	
    $scope.cancel = function() {
        $location.url('/');
    };
});