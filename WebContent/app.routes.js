ratorApp.config(function($routeProvider) {
	$routeProvider

	.when('/', {
		templateUrl : 'components/login/login.html',
		controller : 'mainController'
	})
	
	.when('/login', {
		templateUrl : 'components/login/login.html',
		controller : 'loginController'
	})

	.when('/signup', {
		templateUrl : 'components/signup/signup.html',
		controller : 'signupController'
	})

	.when('/about', {
		templateUrl : 'components/about/about.html',
		controller : 'aboutController'
	})

	.when('/adddeck', {
		templateUrl : 'components/adddeck/adddeck.html',
		controller : 'addDeckController'
	})
	
	.when('/dashboard', {
		templateUrl : 'components/dashboard/dashboard.html',
		controller : 'dashboardController'
	})
	
	.when('/decklist', {
		templateUrl : 'components/decklist/decklist.html',
		controller : 'decklistController'
	})
	
	.when('/deck', {
		templateUrl : 'components/viewdeck/viewdeck.html',
		controller : 'viewdeckController'
	})	
	
	.when('/alterdeck', {
		templateUrl : 'components/alterdeck/alterdeck.html',
		controller : 'alterdeckController'
	})
	
	.when('/alteration', {
		templateUrl : 'components/alteration/alteration.html',
		controller : 'alterationController'
	})
	
	.when('/addgame', {
		templateUrl : 'components/addgame/addgame.html',
		controller : 'addGameController'
	})
	
	.when('/game', {
		templateUrl : 'components/game/game.html',
		controller : 'gameController'
	})
	
	.when('/confirmlist', {
		templateUrl : 'components/confirmlist/confirmlist.html',
		controller : 'confirmlistController'
	})
	
	.when('/confirm', {
		templateUrl : 'components/confirm/confirm.html',
		controller : 'confirmController'
	})
	;
});