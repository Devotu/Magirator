ratorApp.controller('loginController', function($scope, $http, $location, requestService, playerService, settingsService) {
	
	$scope.result = "";
	
	// Login
	$scope.login = function(){
		
		var valid_username = $scope.username != undefined && $scope.username.length > 1;
		var valid_password = $scope.password != undefined && $scope.password.length >= 8;
		
		
		if(valid_username && valid_password){
		
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
					
					settingsService.getSettings();					
					$scope.$emit('logged_in', true);					
					$location.url('/dashboard');		
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
				});
			
		} else {
			
			if(!valid_username){
				$scope.result = "Username is invalid";
			} else if (!valid_password){
				$scope.result = "Password is invalid, min characters i 8";
			} else {
				$scope.result = "Something is fishy";
			}
		}
	};
	
    $scope.goSignup = function() {
        $location.url('/signup');
    };
    
    $scope.goReset = function() {
        $location.url('/requestreset');
    };
});