ratorApp.controller('playGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.token = "";

			$scope.player = JSON.parse(data.player);
			$scope.deckId = deckVarStorage.getCurrentDeck();
			$scope.confirmed = false;
			$scope.done = !$scope.confirmed;


			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];
			
			$scope.rating = {'speed': 0, 'strength': 0, 'synergy': 0, 'control': 0};
			$scope.tags = [];
			
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


			//INIT 

			// Get participants
			var getLiveGameReq = requestService.buildRequest(
				"GetLiveGame",
				{ }
			);

			$http(getLiveGameReq).then(function (response) {
				$scope.result = response.data;

				if (response.data.result == "Success") {
					$scope.participants = JSON.parse(response.data.participants);
					$scope.token = response.data.token;
				}
			},
				function () {
					$scope.result = 'Failure';
			});
			
				
			//RUNNING
			$scope.getGameStatus = function(){
				
				var getGameStatusReq = requestService.buildRequest(
						"API/gamestatus", 
							{
								token : $scope.token
							}
						);
		
				$http(getGameStatusReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'updated ' + Date.now();
							
							var updated = JSON.parse(response.data.status);
							
							updated.forEach(function (up){
								$scope.participants.forEach(function (p){
									if(up.id == p.player.id)
										p.life = up.life;
								})
							});
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			setInterval($scope.getGameStatus, 1000 * 2);
			
			
			//CALLED
			
			$scope.updateGameAttributes = function(){
				
				var updateGameReq = requestService.buildRequest(
						"UpdateLiveGameAttributes", 
							{
								draw : $scope.draw
							}
						);
		
				$http(updateGameReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Game attributes updated';
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			
			$scope.addLife = function(participant, lifechange){
				
				var updateLifeReq = requestService.buildRequest(
						"UpdateLivePlayerLife", 
							{
								id : participant.player.id,
								life : participant.life + lifechange,
								time: Date.now()
							}
						);
		
				$http(updateLifeReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Life updated';
							$scope.getGameStatus();
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			// Add Positive
			$scope.addPositiveTag = function () {
				
				var tag = $scope.tag.positive;

				if (tag != undefined && tag.length > 0) {
					$scope.addTag(1, tag);

					$scope.tag.positive = "";
				}
			};
			
			// Add Negative
			$scope.addNegativeTag = function () {
				
				var tag = $scope.tag.negative;

				if (tag != undefined && tag.length > 0) {
					$scope.addTag(-1, tag);

					$scope.tag.negative = "";
				}
			};			
			
			// Add Tag
			$scope.addTag = function (polarity, tag) {

				$scope.tags.push(
						{
							polarity: polarity,
							tag: tag
						}
					);
			};
			
			$scope.playerHasControl = function(participant){
				if( participant.player.id == $scope.player.id){
					
					return true;
				}
				return false;
			}
			
			//Confirm + end if all players have confirmed
			$scope.confirmResult = function(){
				
				// Confirm Game
				var confirmReq = requestService.buildRequest(
						"ConfirmLiveGame", 
							{
								id : $scope.player.id,
								comment : $scope.comment,
								tags : $scope.tags,
								rating: $scope.rating
							}
						);
		
				$http(confirmReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.confirmed = true;
							$scope.result = 'Confirmed game';
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			
			$scope.abortGame = function(){
				
				// Abort Game
				var abortReq = requestService.buildRequest(
						"AbortLiveGame", 
							{ }
						);
		
				$http(abortReq).then(function(response){
					$scope.result = response.data;
					
						if (response.data.result == "Success"){
							$scope.result = 'Game canceled';
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			
			$scope.leaveGame = function(){
				$location.url('/deck');
			}


		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});