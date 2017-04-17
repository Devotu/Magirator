ratorApp.controller('alterdeckController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
		
		$scope.result = "Waiting for response";	
		
		playerService.getPlayer().then(function(data) {
			if (data.result == "Success") {

				$scope.deckId = deckVarStorage.getCurrentDeck();
				
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
				
				
				$scope.alterDeck = function(){
					
					// Alter deck
					var AlterDeckReq = requestService.buildRequest(
							"AlterDeck", 
							{
								deck: {
									'id': $scope.deckId,
									'name': $scope.deck.name,
									'format': $scope.deck.format,
									'black': $scope.deck.blackCards,
									'white': $scope.deck.whiteCards,
									'red': $scope.deck.redCards,
									'green': $scope.deck.greenCards,
									'blue': $scope.deck.blueCards,
									'colorless': $scope.deck.colorlessCards,
									'theme': $scope.deck.theme,
									'created': Date.now()
								},
								comment: $scope.comment
							});
		
					$http(AlterDeckReq).then(function(response){
						$scope.result = response.data;
						
							if (response.data.result == "Success"){
								deckVarStorage.setCurrentDeck(response.data.newDeckId);
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