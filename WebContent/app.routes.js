ratorApp.config(function($routeProvider) {
	$routeProvider

	.when('/', {
		templateUrl : 'progressinfo.html'
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
	
	.when('/requestreset', {
		templateUrl : 'components/requestreset/requestreset.html',
		controller : 'requestresetController'
	})
	
	.when('/resetpassword', {
		templateUrl : 'components/resetpassword/resetpassword.html',
		controller : 'resetpasswordController'
	})
	
	.when('/prepare', {
		templateUrl : 'components/preparegame/preparegame.html',
		controller : 'prepareGameController'
	})	
	
	.when('/play', {
		templateUrl : 'components/livegame/livegame.html',
		controller : 'liveGameController'
	})
	
	.when('/init', {
		templateUrl : 'components/initgame/initgame.html',
		controller : 'initGameController'
	})
	
	.when('/join', {
		templateUrl : 'components/joingame/joingame.html',
		controller : 'joinGameController'
	})
	
	.when('/stats', {
		templateUrl : 'components/viewstats/viewstats.html',
		controller : 'viewStatsController'
	})
	;
});