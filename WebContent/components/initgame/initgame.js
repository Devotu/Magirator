ratorApp.controller('initGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.player = JSON.parse(data.player);
			
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
		    
		    		    
		    //Start game
		    $scope.goWithDeck = function(deck_name, deck_id){
				
				var startLiveGameReq = requestService.buildRequest(
						"API/startnewgame", 
							{
								player_name: $scope.player.name,
								player_id: $scope.player.id,
								deck_name: deck_name,
								deck_id: deck_id
							}
						);
		
				$http(startLiveGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							
							var token = JSON.parse(response.data.token);
							
							$location.url('/livegame').search({token: 'token'});
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