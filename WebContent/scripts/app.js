// create the module and name it scotchApp
// also include ngRoute for all our routing needs
var mApp = angular.module('magiratorApp', [ 'ngRoute' ]);

// configure our routes
mApp.config(function($routeProvider) {
	$routeProvider

	// route for the home page
	.when('/', {
		templateUrl : 'pages/welcome.html',
		controller : 'mainController'
	})

	// route for the about page
	.when('/about', {
		templateUrl : 'pages/about.html',
		controller : 'aboutController'
	})

	// route for the contact page
	.when('/contact', {
		templateUrl : 'pages/contact.html',
		controller : 'contactController'
	})

	.when('/adddeck', {
		templateUrl : 'pages/adddeck.html',
		controller : 'addDeckController'
	});
});

// create the controller and inject Angular's $scope
mApp.controller('mainController', function($scope) {
	// create a message to display in our view
	$scope.message = 'Everyone come and see how good I look!';
});

mApp.controller('aboutController', function($scope) {
	$scope.message = 'Look! I am an about page.';
});

mApp.controller('contactController', function($scope) {
	$scope.message = 'Contact us! JK. This is just a demo.';
});

mApp.controller('addDeckController', function($scope, $http) {
	
	$scope.addDeck = function(){
		var req = {
				method: 'POST',
				url: '/Magirator/AddDeck',
				headers: {
				   'Content-Type': 'application/json'
				},
				data: JSON.stringify({ 'deck': 'test' })
		}

		$http(req).then(function(){$scope.result = 'Success'}, function(){$scope.result = 'Failure'});
	};

});