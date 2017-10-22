ratorApp.controller('addDeckController', function ($scope, $http, $location, playerService, requestService, settingsService) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {			
			
/*			$scope.helps = {
					adddeck_colors: {name: 'adddeck_colors', display: true},
					adddeck_details: {name: 'adddeck_details', display: true}
			}*/
			
			$scope.settings = settingsService.getSettings();

			if ($scope.settings == undefined) {
				settingsService.loadSettings().then(function(data) {
					$scope.settings = data;
				});
			}

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
							'black': $scope.deck.black,
							'white': $scope.deck.white,
							'red': $scope.deck.red,
							'green': $scope.deck.green,
							'blue': $scope.deck.blue,
							'colorless': $scope.deck.colorless,
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