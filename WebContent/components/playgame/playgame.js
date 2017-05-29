ratorApp.controller('playGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.token = "";

			$scope.player = JSON.parse(data.player);
			$scope.deck = undefined;
			$scope.confirmed = false;
			$scope.done = !$scope.confirmed;
			$scope.confirmOption = "Confirm dead";

			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];

			$scope.ratingSystem = "General";
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
			
			$scope.lifeTimer;


			//INIT 
			var getDeckinLiveGameReq = requestService.buildRequest(
				"GetDeckinLiveGame",
				{ }
			);

			$http(getDeckinLiveGameReq).then(function (response) {
				$scope.result = response.data;
			
				if (response.data.result == "Success") {
					$scope.deck = JSON.parse(response.data.deck);
					deckVarStorage.setCurrentDeck($scope.deck.deckid);
				}
			},
				function () {
					$scope.result = 'Failure';
			});
			

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
					
					$scope.participants.forEach(function (p){
						p.lifeChange = 0;
					})
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
							
							var numAlive = 0;
							
							var updated = JSON.parse(response.data.status);
							
							updated.forEach(function (up){
								
								if(up.confirmed == false){
									numAlive++;
								}
								
								$scope.participants.forEach(function (p){
									if(up.id == p.player.id)
										p.life = up.life;
								})
							});
							
							if(numAlive <= 1){
								$scope.confirmOption = "Confirm win!"
							}
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			var updater = setInterval($scope.getGameStatus, 1000 * 2);
			
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
				console.log('adding ' + lifechange);
				
				clearTimeout($scope.lifeTimer);
				participant.lifeChange = participant.lifeChange + lifechange
				
				console.log('now totals ' + participant.lifeChange);
				
				$scope.lifeTimer = setTimeout(function(){
					
					console.log('updating');
					var updateLifeReq = requestService.buildRequest(
							"UpdateLivePlayerLife", 
								{
									id : participant.player.id,
									life : participant.life + participant.lifeChange,
									time: Date.now()
								}
							);
			
					$http(updateLifeReq).then(function(response){
						$scope.result = response.data;
						
							if (response.data.result == "Success"){
								$scope.result = 'Life updated';
								participant.lifeChange = 0;
								$scope.getGameStatus();
							}					
						}, 
						function(){
							$scope.result = 'Failure';
					});
				}, 2000, participant);
			}
			
			$scope.updateLife = function(participant, lifechange){
				
				console.log('updating');
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
							participant.lifeChange = 0;
							$scope.getGameStatus();
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			
			// Add Positive
			$scope.addPositiveTag = function () {
				
				var tag = { polarity: 1, tag: $scope.tag.positive } ;

				if (tag != undefined && tag.tag.length > 0 && $scope.tagExistsAt(tag) == -1) {
					$scope.tags.push(tag)
					$scope.tag.positive = "";
				}
			};
			
			
			// Add Negative
			$scope.addNegativeTag = function () {
				
				var tag = { polarity: -1, tag: $scope.tag.negative } ;

				if (tag != undefined && tag.tag.length > 0 && $scope.tagExistsAt(tag) == -1) {
					$scope.tags.push(tag)
					$scope.tag.negative = "";
				}
			};
			
			$scope.tagExistsAt = function (tag) {
				
				for(var t in $scope.tags){
					if($scope.tags[t].polarity == tag.polarity && $scope.tags[t].tag == tag.tag){
						return t;
					}
				}
				return -1;
			}
			
			$scope.removeTag = function (tag) {
				
				var i = $scope.tagExistsAt(tag);
				$scope.tags.splice(i, 1);
			};

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
							clearInterval(updater);
						}					
					}, 
					function(){
						$scope.result = 'Failure';
				});
			}
			
			
			$scope.leaveGame = function(){
				clearInterval(updater);
				$location.url('/deck');
			}


		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});