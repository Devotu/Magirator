ratorApp.controller('resetpasswordController', function($scope, $http, $location, requestService) {
	
	$scope.reset = function(){
		
		$scope.reset = "Waiting for response";
		
		var resetReq = requestService.buildRequest(
				"SetNewPassword", 
				{
					'username': $scope.username,
					'password': $scope.password,
					'retype': $scope.retype,
					'code': $scope.code
				}
			);

		$http(resetReq).then(function(response){
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$location.url('/login');		
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