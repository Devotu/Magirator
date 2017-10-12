ratorApp.controller('viewdeckController', function($scope, $http, $location, playerService, requestService, varStorage, settingsService) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			$scope.deckId = varStorage.getCurrentDeck();
			
			$scope.settings = settingsService.getSettings();

			if ($scope.settings == undefined) {
				settingsService.loadSettings().then(function(data) {
					$scope.settings = data;
				});
			}
			
			// Deck
			$scope.showDelete = false;
			
			$scope.getDeck = function(){
				
				// Get deck
				var getDeckReq = requestService.buildRequest(
						"GetDeck", 
						{id:$scope.deckId}
						);

				$http(getDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.deck = JSON.parse(response.data.deck);
							var datebits = $scope.deck.created.split(" ");
							$scope.deck.created = datebits[0] + " " + datebits[1] + " " + datebits[2]
							$scope.deckname = $scope.deck.name;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
						
			$scope.toggleActive = function(){
				
				// Toggle deck
				var toggleDeckReq = requestService.buildRequest(
						"ToggleDeck", 
						{
							id:$scope.deck.deckid}
						);

				$http(toggleDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
							$scope.deck.active = !$scope.deck.active;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});
			}
			
			$scope.deleteDeck = function(){
				
				// Toggle deck
				var deleteDeckReq = requestService.buildRequest(
						"DeleteDeck", 
						{id:$scope.deck.deckid}
						);

				$http(deleteDeckReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Success';
						}
					}, 
					function(){
						$scope.result = 'Failure';
					});
			}
			
			$scope.showDeleteDialog = function(){
				
				// Toggle delete dialog on
				$scope.showDelete = true;
			}
			
			$scope.toggleDeleteDialog = function(){
				
				// Toggle delete dialog
				($scope.showDelete == true) ? $scope.showDelete = false : $scope.showDelete = true;
			}
			
			// Games			
			$scope.getGames = function(){
				
				// Get Games
				var getGamesReq = requestService.buildRequest(
						"GetGames", 
						{id:$scope.deckId}
						);

				$http(getGamesReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.participations = JSON.parse(response.data.games);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.goAddGame = function(){
				
				varStorage.setCurrentDeck($scope.deckId);
				$location.url('/addgame');
			}
			
			$scope.goPrepareGame = function(){
				
				varStorage.setCurrentDeck($scope.deckId);
				$location.url('/prepare');
			}
			
			$scope.goGame = function(gameId){
				
				varStorage.setGoTo(gameId);
				$location.url('/game');
			}
			
			// Stats
			$scope.getStats = function(){
				
				var getStatsReq = requestService.buildRequest(
						"GetStats", 
						{id:$scope.deckId}
						);

				$http(getStatsReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			// Alterations
			$scope.getAlterations = function(){
				
				var getAlterationsReq = requestService.buildRequest(
						"GetAlterations", 
						{id:$scope.deckId}
						);

				$http(getAlterationsReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = response.data.result;
							$scope.alterations = JSON.parse(response.data.alterations);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.goAlter = function(){
				
				varStorage.setCurrentDeck($scope.deckId);
				$location.url('/alterdeck');
			}
			
			$scope.goAlteration = function(alterationId){
				
				varStorage.setGoTo(alterationId);
				$location.url('/alteration');
			}
			
			// Tabs
		    $scope.setTab = function(newTab){
		      $scope.tab = newTab;
		      switch(newTab) {
			      case 1:
			          $scope.getDeck();
			          break;
			      case 2:
			          $scope.getGames();
			          break;
			      case 4:
			          $scope.getAlterations();
			          break;
			      default:
			    	  $scope.result = "The tabcase defaulted";
		      } 
		    };

		    $scope.isSet = function(tabNum){
		      return $scope.tab === tabNum;
		    };
		    
		    $scope.setTab(1);
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
	
});