ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);


ratorApp.controller('mainController', function($scope, $http, $location, playerService) {
	
	$scope.result = "Waiting for response";
	
	$scope.$on('unconfirmed', function (event, data) {
	    $scope.unconfirmed = data;
	  });
	
	$scope.$on('logged_in', function (event, data) {
	    $scope.logged_in = data;
	  });
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			$location.url('/dashboard');
		} else {
			$location.url('/');
		}
	});
});