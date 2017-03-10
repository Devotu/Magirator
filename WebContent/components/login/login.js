ratorApp.controller('loginController', function($scope, $http, $location, requestService) {
	
	// Login
	$scope.login = function(){
		
		$scope.result = "Waiting for response";
		
		var loginReq = requestService.buildRequest(
				"Login", 
				{
					'username': $scope.username,
					'password': $scope.password
				}
			);

		$http(loginReq).then(function(response){
			$scope.result = response.data.result;
			
			if (response.data.result == "Success"){
				$scope.$emit('logged_in', true);
				$location.url('/dashboard');		
			}
			
			}, 
			function(){
				$scope.result = 'Failure';
			});
	};
	
    $scope.goSignup = function() {
        $location.url('/signup');
    };
});