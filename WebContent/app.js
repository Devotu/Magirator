ratorApp = angular.module('magiratorApp', [ 'ngRoute' ]);


ratorApp.controller('mainController', function() {
	
});

ratorApp.controller('helpController', function ($scope, settingsService) {
	
	$scope.help = {
			name: '',
			text: ''
	};
    
    $scope.help_initController = function(help, text) {
        $scope.help.name = help;
        $scope.help.text = text;
    }
    
    $scope.help_dismiss = function(help_name) {
    	console.log(help_name);
    	settingsService.dismissHelp(help_name);
    }
});