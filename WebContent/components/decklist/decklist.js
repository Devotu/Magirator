ratorApp.controller('decklistController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
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
			
			$scope.goDeck = function(id){
				deckVarStorage.setCurrentDeck(id);
				$location.url('/deck');
			}
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});