ratorApp.controller('addDeckController', function ($scope, $http, $location, playerService, requestService) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {

			// Get formats
			var getFormatsReq = requestService.buildRequest(
				"GetFormats",
				{}
			);

			$http(getFormatsReq).then(function (response) {
				$scope.formats = response.data;
				$scope.format = $scope.formats[0];
			},
				function () {
					$scope.result += "Could not get formats";
				});

			$scope.getColorValue = function (cards, lands) {

				if (cards > 0) {
					return cards + 0;
				} else {
					return 0;
				}
			}

			// Add deck
			$scope.addDeck = function () {
				$scope.result = "Waiting for response";

				var addDeckReq = requestService.buildRequest(
					"AddDeck",
					{
						deck: {
							'name': $scope.deck.name,
							'format': $scope.deck.format,
							'black': $scope.getColorValue($scope.deck.blackCards, $scope.deck.blackLands),
							'white': $scope.getColorValue($scope.deck.whiteCards, $scope.deck.whiteLands),
							'red': $scope.getColorValue($scope.deck.redCards, $scope.deck.redLands),
							'green': $scope.getColorValue($scope.deck.greenCards, $scope.deck.greenLands),
							'blue': $scope.getColorValue($scope.deck.blueCards, $scope.deck.blueLands),
							'colorless': $scope.getColorValue($scope.deck.colorlessCards, $scope.deck.colorlessLands),
							'theme': $scope.deck.theme,
							'created': Date.now()
						}
					}
				);

				$http(addDeckReq).then(function (response) {
					$scope.result = response.data

					if (response.data.result == "Success") {
						$location.url('/dashboard');
					}

				},
					function () {
						$scope.result = 'Failure'
					});
			};

		} else {
			$scope.result = 'Not logged in, please log in and try again';
			$location.url('/');
		}
	});
});