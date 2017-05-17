ratorApp.controller('playGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {

			$scope.player = JSON.parse(data.player);
			$scope.deckId = deckVarStorage.getCurrentDeck();

			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];''
			
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


			// Get participants
			var getLiveGameReq = requestService.buildRequest(
				"GetLiveGame",
				{
					playerId: $scope.player.id
				}
			);

			$http(getLiveGameReq).then(function (response) {
				$scope.result = response.data;

				if (response.data.result == "Success") {
					$scope.participants = JSON.parse(response.data.participants);
				}
			},
				function () {
					$scope.result = 'Failure';
				});
			



		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});