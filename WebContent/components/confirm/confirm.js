ratorApp.controller('confirmController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";	
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.gameId = deckVarStorage.getGoTo();
			$scope.player = JSON.parse( data.player );
			
			$scope.getGame = function(){
				
				// Get Game
				var getGameReq = requestService.buildRequest(
						"GetGame", 
						{id:$scope.gameId}
						);
		
				$http(getGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Got Game';
							$scope.participants = JSON.parse(response.data.participants);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});				
			}
			
			$scope.getGame();
			
			$scope.tags = [];
			
			function Tag(tagged, polarity, tag){
				this.tagged = tagged;
				this.polarity = polarity;
				this.tag = tag;
			}
			
			// Add Tag
			$scope.addTag = function(participant){
				
				if (participant.tag.positive != undefined && participant.tag.positive.length > 0){
					
					var tag = new Tag(participant.player.id, 1, participant.tag.positive);
					participant.tags.push(tag);
					$scope.tags.push(tag);
					
					participant.tag.positive = "";
				}
				
				if (participant.tag.negative != undefined && participant.tag.negative.length > 0){

					var tag = new Tag(participant.player.id, -1, participant.tag.negative);
					participant.tags.push(tag);
					$scope.tags.push(tag);
					
					participant.tag.negative = "";
				}
			};
			
			var findSelf = function(p){
				return p.player.id == $scope.player.id;
			}
			
			$scope.confirm = function(response){
				
				$scope.self = $scope.participants.filter(findSelf)[0];
				
				// Confirm Game
				var confirmReq = requestService.buildRequest(
						"ConfirmGame", 
							{
								gameId : $scope.gameId,
								id : $scope.self.result.id,
								confirm : response,
								comment : $scope.comment,
								tags : $scope.tags
							}
						);
		
				$http(confirmReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Confirmed game';
							$location.url('/confirmlist');
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