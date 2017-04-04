ratorApp.controller('addGameController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
    
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {
			
			$scope.player = JSON.parse( data.player );
			$scope.deckId = deckVarStorage.getCurrentDeck();

			$scope.selfAdded = false;
			$scope.addState = "Regular";
			
			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];
			
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
							$scope.playerdeck = JSON.parse(response.data.deck);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
					});				
			}
			
			$scope.getDeck();
		    
			// Get opponent decks
			$scope.getOpponentDecks = function(){
				
				var getOpponentDecksReq = requestService.buildRequest(
						"GetOpponentDeckList", 
						{id:$scope.addOpponent.id}
						);

				$http(getOpponentDecksReq).then(function(response){
					$scope.result = response.data;
					
					if (response.data.result == "Success"){
				    	$scope.decks = JSON.parse(response.data.decks);
				    	$scope.addDeck = $scope.decks[0];
					}
					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			// Get opponents
			var getOpponentsReq = requestService.buildRequest(
					"GetOpponents", 
					{}
					);
	
			$http(getOpponentsReq).then(function(response){
				$scope.result = response.data;
				
					if (response.data.result == "Success"){
				    	$scope.opponents = JSON.parse(response.data.opponents);
				    	$scope.addOpponent = $scope.opponents[0];
				    	$scope.getOpponentDecks();
					}					
				}, 
				function(){
					$scope.result = 'Failure';
			});			

			// Add Self
			$scope.addSelf = function(){
				
				$scope.participants.push(
					{
						deckId : $scope.playerdeck.deckid,
						place : $scope.participants.length +1,
						playerId : $scope.player.id,
						playerName : $scope.player.name,
						deckName : $scope.playerdeck.name,
						confirmed : true,
						comment : "",
						added : Date.now(),
						tags : []
					}
				);

				$scope.selfAdded = true;
			};
			
			// Add Participant
			$scope.addParticipant = function(){
				
				$scope.participants.push(
					{
						deckId : $scope.addDeck.id,
						place : $scope.participants.length +1,
						playerId : $scope.addOpponent.id,
						playerName : $scope.addOpponent.name,
						deckName : $scope.addDeck.name,
						confirmed : false,
						comment : "",
						added : Date.now(),
						tags : []
					}
				);

				//Remove to avoid duplicates
				var opponentz = $scope.opponents.map(function(o) { return o.id; });
				var pIndex = opponentz.indexOf($scope.addOpponent.id);

				if (pIndex > -1) {
					$scope.opponents.splice(pIndex, 1);
				}

				//Restore order
				$scope.addOpponent = $scope.opponents[0];
				$scope.getOpponentDecks();
			};
			
			// Add Tag
			$scope.addTag = function(participant){
				
				if (participant.tag.positive != undefined && participant.tag.positive.length > 0){
					participant.tags.push(
							{
								polarity: 1,
								tag: participant.tag.positive
							}
					);
					
					participant.tag.positive = "";
				}
				
				if (participant.tag.negative != undefined && participant.tag.negative.length > 0){
					participant.tags.push(
							{
								polarity: -1,
								tag: participant.tag.negative
							}
					);
					
					participant.tag.negative = "";
				}
			};
			
			// Add game
			$scope.addGame = function(){
				$scope.result = "Waiting for response";
				
				var updatePlayerEntry = function(participant){
					if (participant.deckId == $scope.deckId){
						participant.comment = $scope.comment; 
					}
				}
				
				$scope.participants.forEach(updatePlayerEntry);
				
				var addGameReq = requestService.buildRequest(
						"AddGame", 
						{
							participants : $scope.participants,
							draw : $scope.draw
						}
				);
		
				$http(addGameReq).then(function(response){
					$scope.result = response.data
					
					if (response.data.result == "Success"){
						deckVarStorage.setCurrentDeck($scope.deckId);
						$location.url('/deck');	
					}
					
					}, 
					function(){
						$scope.result = 'Failure'
				});				
			};
			
			
		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});