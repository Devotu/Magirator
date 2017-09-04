ratorApp.controller('addDeckController', function ($scope, $http, $location, playerService, requestService) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {
			
			$scope.deck = {
					name: "",
					format: "",
					theme: "",
					black: 0,
					white: 0,
					red: 0,
					green: 0,
					blue: 0,
					colorless: 0
			}
			
			$scope.color_details = false;

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
			
			
			$scope.toggleColor = function(color){
				
				if(!$scope.color_details){
					$scope.deck[color] == 1 ? $scope.deck[color] = 0 : $scope.deck[color] = 1;
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
							'black': $scope.black,
							'white': $scope.white,
							'red': $scope.red,
							'green': $scope.green,
							'blue': $scope.blue,
							'colorless': $scope.colorless,
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