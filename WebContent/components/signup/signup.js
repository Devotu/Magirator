ratorApp.controller('signupController', function($scope, $http, $location, requestService) {
	
	// Sign up
	$scope.signup = function(){
		
		$scope.result = "Waiting for response";
		
		var signupReq = requestService.buildRequest(
				"Signup", 
				{
					'username': $scope.username,
					'password': $scope.password,
					'retype': $scope.retype,
					'playername': $scope.playername
				}
			);
		
		var minlength = function(s){
			if(s.length > 7){
				return true;
			}
			return false;
		}
		
		var match = function(a, b){
			if (a === b){
				return true;
			}
			return false;
		}
				
		if( minlength($scope.password) ){
			
			if( match($scope.password, $scope.retype) ){
	
				$http(signupReq).then(function(response){
					
						$scope.result = response.data.result;
					
						if (response.data.result == "Success"){
							$location.url('/');				
						}
					}, 
					function(){
						$scope.result = 'Request failure';
				});		
			} else {
				$scope.result = 'Passwords does not match';
			}
			
		} else {
			$scope.result = 'Passwords is to short, minimum length is 8 characters';
		}
	};
});