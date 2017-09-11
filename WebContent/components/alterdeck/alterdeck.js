ratorApp.controller('alterdeckController', function($scope, $http, $location, playerService, requestService, varStorage) {
		
		$scope.result = "Waiting for response";	
		
		playerService.getPlayer().then(function(data) {
			if (data.result == "Success") {

				$scope.deckId = varStorage.getCurrentDeck();
				
				$scope.getFormats = function(){
				    
					var getFormatsReq = requestService.buildRequest(
							"GetFormats", 
							{}
							);
				    
				    $http(getFormatsReq).then(function(response){
				    	$scope.formats = response.data;
				    	$scope.format = $scope.formats[0];
				    	}, 
				    	function(){
				    		$scope.result += "Could not get formats";
				    	});
				}
		
				$scope.getFormats();
				
					
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
								$scope.deckname = $scope.deck.name;
							}					
						}, 
						function(){
							$scope.result = 'Failure';
						});				
				}
				
				$scope.getDeck();
				
				
				$scope.toggleColor = function(color){
					
					color = color + "Cards";
					
					if(!$scope.color_details){
						$scope.deck[color] == 1 ? $scope.deck[color] = 0 : $scope.deck[color] = 1;
					}				
				}
				
				
				$scope.alterDeck = function(){
					
					// Alter deck
					var AlterDeckReq = requestService.buildRequest(
							"AlterDeck", 
							{
								deck: {
									'id': $scope.deckId,
									'name': $scope.deck.name,
									'format': $scope.deck.format,
									'black': $scope.deck.blackCards ? 1 : $scope.deck.blackCards,
									'white': $scope.deck.whiteCards ? 1 : $scope.deck.whiteCards,
									'red': $scope.deck.redCards ? 1 : $scope.deck.redCards,
									'green': $scope.deck.greenCards ? 1 : $scope.deck.greenCards,
									'blue': $scope.deck.blueCards ? 1 : $scope.deck.blueCards,
									'colorless': $scope.deck.colorlessCards ? 1 : $scope.deck.colorlessCards,
									'theme': $scope.deck.theme,
									'created': Date.now()
								},
								comment: $scope.comment == undefined ? "" : $scope.comment
							});
		
					$http(AlterDeckReq).then(function(response){
						$scope.result = response.data;
						
							if (response.data.result == "Success"){
								varStorage.setCurrentDeck(response.data.newDeckId);
								$location.url('/deck');
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