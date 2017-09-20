ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);


ratorApp.controller('mainController', function($scope, $http, $location, playerService) {
	
	$scope.player = {};
	$scope.result = "Waiting for response";
	
	
	$scope.verifyPlayer = function() {
		$scope.reloadPlayer();
	};
	
	$scope.reloadPlayer = function(fetchedPlayer) {
		playerService.getPlayer().then(function(data) {
			if (data.result == "Success") {
				$scope.player = JSON.parse(data.player);
				$location.url('/dashboard');
			} else {
				$location.url('/');
			}
		});
	};
	
	$scope.reloadPlayer();
	
	$scope.updatePlayer = function(newPlayer) {
		$scope.player = newPlayer;
	}
	
	$scope.clearPlayer = function() {
		$scope.player = undefined;
	};
	
	$scope.loggedIn = function(){
		return $scope.player.id > 0; //Not really a proper solution...		
	}
});

ratorApp.controller('helpController', function ($scope) {
    
	$scope.help = {};

    $scope.initHelpController = function(help) {
    	console.log(help);
        $scope.help = help;
    }
});