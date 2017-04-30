ratorApp.controller('confirmController', function($scope, $http, $location, playerService, requestService, deckVarStorage) {
	
	$scope.result = "Waiting for response";	
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.gameId = deckVarStorage.getGoTo();
			$scope.player = JSON.parse( data.player );
			
			$scope.ratingSystem = "General";
			$scope.rating = [0,0,0,0];
			$scope.generalRating = function(){ return ($scope.rating.reduce(function(a, b) { return a + b; }, 0))/4; }
			
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
								tags : $scope.tags,
								rating: $scope.rating
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
			
			
			$scope.ratingSystem = "General";

			$scope.toggleRatingSystem = function () {

				if ($scope.ratingSystem != "SSSC") {
					$scope.ratingSystem = "SSSC";
				} else {
					$scope.ratingSystem = "General";
				}
			};
			
			//Rating (not particulary elegant)
			$scope.rate = function(parameter, value){
				
				switch(parameter) {
			    case 'general':
			        $scope.rating = [value, value, value, value];
			        break;
			    case 'speed':
			        $scope.rating = [value, $scope.rating[1], $scope.rating[2], $scope.rating[3]];
			        break;
			    case 'strength':
			        $scope.rating = [$scope.rating[0], value, $scope.rating[2], $scope.rating[3]];
			        break;
			    case 'synergy':
			        $scope.rating = [$scope.rating[0], $scope.rating[1], value, $scope.rating[3]];
			        break;
			    case 'control':
			        $scope.rating = [$scope.rating[0], $scope.rating[1], $scope.rating[2], value];
			        break;
			    default:
			    	$scope.rating = [value, value, value, value];
				}
			}

		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});