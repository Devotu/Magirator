ratorApp.controller('joinGameController', function ($scope, $http, $location, playerService, requestService, varStorage, settingsService) {
	
	$scope.live_id = "";

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.player = JSON.parse(data.player);
			
			$scope.settings = settingsService.getSettings();

			if ($scope.settings == undefined) {
				settingsService.loadSettings().then(function(data) {
					$scope.settings = data;
				});
			}
			
			// Get decks
			var getDecksReq = requestService.buildRequest(
					"GetDeckList", 
					{}
					);

			$http(getDecksReq).then(function(response){
				$scope.result = response.data;
				
				if (response.data.result == "Success"){
					$scope.result = 'Success';
					$scope.decks = JSON.parse(response.data.decks);
				}
				
				}, 
				function(){
					$scope.result = 'Failure';
			});
			
			
		    $scope.order = {
		            field: 'name',
		            reverse: false
		        };
		    
		    $scope.reverseOrder = false;
		    
		    var bool_order = {
		            true: 0,
		            false: 1
		        };
		    
		    var bool_badge = {
		            true: 'yes',
		            false: 'no'
		        };
			
			
			$scope.deckOrder = function(deck) {
				var sorter = $scope.order.field;
				var sortval = deck[sorter];
				deck.badge = sortval;		

				// Special cases value
				if (sortval === true || sortval === false){
					boolval = sortval;
					sortval = bool_order[boolval];
					deck.badge = bool_badge[boolval];
				}
				
				// Special cases sorter
				if (sorter == 'name'){// Name is always displayed
					deck.badge = "";
				}		
				
		        return sortval;
			}
		    
		    		    
		    //Start game
		    $scope.goWithDeck = function(deck_name, deck_id){
		    	$scope.result = '';		    	
				
				var joinLiveGameReq = requestService.buildRequest(
						"API/joingame", 
							{
								player_name: $scope.player.name,
								player_id: $scope.player.id,
								deck_name: deck_name,
								deck_id: deck_id,
								live_id: $scope.live_id
							}
						);
		
				$http(joinLiveGameReq).then(function(response){
					$scope.result = response.data.result;
					
						if (response.data.result == "Success"){
							varStorage.setLiveToken(response.data.token);
							$location.url('/play');
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}


		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});