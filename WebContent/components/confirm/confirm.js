ratorApp.controller('confirmController', function($scope, $http, $location, playerService, requestService, varStorage) {
	
	$scope.result = "Waiting for response";	
	
	playerService.getPlayer().then(function(data) {
		if (data.result == "Success") {

			$scope.gameId = varStorage.getGoTo();
			$scope.player = JSON.parse( data.player );
			
			$scope.rating = {'speed': 0, 'strength': 0, 'synergy': 0, 'control': 0};
			
			$scope.generalRating = function(){
				var total = 0;
				var keys = 0;
				for (var key in $scope.rating) {
					  if ($scope.rating.hasOwnProperty(key) && key != 'id') {
					    total += $scope.rating[key];
					    keys++;
					  }
					}
				return total/keys; 
				}
			
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
			
			
			$scope.rate = function(parameter, value){
				if (parameter == 'general'){
					for (var key in $scope.rating) {
						  if ($scope.rating.hasOwnProperty(key)) {
							  $scope.rating[key] = value;
						  }
						}
				} else {
					$scope.rating[parameter] = value;
				}
			}

		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});