ratorApp.controller('prepareGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {

			//Variables
			$scope.player = JSON.parse(data.player);
			$scope.deckId = deckVarStorage.getCurrentDeck();

			$scope.participants = []; ''

			$scope.addState = "Regular";
			$scope.decks = [];
			$scope.newPlayer = { 'id': 0, 'name': "" };
			$scope.allPlayers = [];

			$scope.opponentIsMinion = function () {
				if ($scope.opponentToAdd != undefined && $scope.opponentToAdd.labels != undefined) {
					for (var i = 0; i < $scope.opponentToAdd.labels.length; i++) {
						if ($scope.opponentToAdd.labels[i] == "Minion") {
							return true;
						}
					}
					return false;
				}
			}



			//Initial
			var getOpponentsReq = requestService.buildRequest(
				"GetOpponents",
				{}
			);

			$http(getOpponentsReq).then(function (response) {
				$scope.result = response.data;

				if (response.data.result == "Success") {
					$scope.opponents = JSON.parse(response.data.opponents);
					$scope.opponentToAdd = $scope.opponents[0];
					if ($scope.opponentToAdd != undefined) {
						$scope.getOpponentDecks();
					}
				}
			},
				function () {
					$scope.result = 'Failure';
				});


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



			//Togglers
			$scope.toggleAddPlayer = function () {

				if ($scope.addState != "Player") {
					$scope.addState = "Player";
				} else {
					$scope.addState = "Regular";
				}

			};

			$scope.toggleAddMinionDeck = function () {

				if ($scope.addState != "Deck") {
					$scope.addState = "Deck";
				} else {
					$scope.addState = "Regular";
				}

			};

			$scope.toggleSearch = function () {

				if ($scope.addState != "Search") {
					if ($scope.allPlayers.length < 1) {
						$scope.getAllPlayers();
					}
					$scope.addState = "Search";
				} else {
					$scope.addState = "Regular";
				}
			};



			//Called
			$scope.getOpponentDecks = function () {

				if ($scope.opponentToAdd != undefined) {
					var getOpponentDecksReq = requestService.buildRequest(
						"GetOpponentDeckList",
						{ id: $scope.opponentToAdd.id }
					);

					$http(getOpponentDecksReq).then(function (response) {
						$scope.result = response.data;

						if (response.data.result == "Success") {
							$scope.decks = {};
							$scope.decks = JSON.parse(response.data.decks);
							$scope.addDeck = $scope.decks[0];
						}

					},
						function () {
							$scope.result = 'Failure';
						});
				}
			}


			$scope.getAllPlayers = function () {

				var getAllPlayersReq = requestService.buildRequest(
					"GetAllPlayers",
					{}
				);

				$http(getAllPlayersReq).then(function (response) {
					$scope.result = response.data;

					if (response.data.result == "Success") {
						$scope.allPlayers = [];
						$scope.allPlayers = JSON.parse(response.data.players);
						$scope.searchName = "";
					}

				},
					function () {
						$scope.result = 'Failure';
					});
			}



			//Adders
			$scope.addParticipant = function () {

				$scope.participants.push(
					{
						deckId: $scope.addDeck.id,
						playerId: $scope.opponentToAdd.id,
						playerName: $scope.opponentToAdd.name,
						deckName: $scope.addDeck.name
					}
				);
				
				console.log($scope.participants);

				//Remove to avoid duplicates
				var opponentz = $scope.opponents.map(function (o) { return o.id; });
				var pIndex = opponentz.indexOf($scope.opponentToAdd.id);

				if (pIndex > -1) {
					$scope.opponents.splice(pIndex, 1);
				}

				//Clean up
				$scope.opponentToAdd = $scope.opponents[0];
				$scope.getOpponentDecks();
			};


			$scope.addMinion = function () {

				var addMinonReq = requestService.buildRequest(
					"AddMinion",
					{
						'name': $scope.newPlayer.name
					}
				);

				$http(addMinonReq).then(function (response) {
					$scope.result = response.data;

					if (response.data.result == "Success") {
						$scope.opponents.unshift(JSON.parse(response.data.opponent));
						$scope.opponentToAdd = $scope.opponents[0];
						$scope.getOpponentDecks();

						$scope.toggleAddPlayer();
					}
				},
					function () {
						$scope.result = 'Failure';
					});
			}


			$scope.addMinionDeck = function () {

				var addMinonDeckReq = requestService.buildRequest(
					"AddMinionDeck",
					{
						'id': $scope.opponentToAdd.id,
						'deck': {
							'name': $scope.newDeck.name,
							'format': $scope.newDeck.format,
							'black': $scope.newDeck.blackCards ? 1 : 0,
							'white': $scope.newDeck.whiteCards ? 1 : 0,
							'red': $scope.newDeck.redCards ? 1 : 0,
							'green': $scope.newDeck.greenCards ? 1 : 0,
							'blue': $scope.newDeck.blueCards ? 1 : 0,
							'colorless': $scope.newDeck.colorlessCards ? 1 : 0,
							'theme': $scope.newDeck.theme,
							'created': Date.now()
						}
					}
				);

				$http(addMinonDeckReq).then(function (response) {
					$scope.result = response.data;

					if (response.data.result == "Success") {
						$scope.decks.unshift(JSON.parse(response.data.deck));
						$scope.addDeck = $scope.decks[0];
						$scope.toggleAddMinionDeck();
					}

				},
					function () {
						$scope.result = 'Failure';
					});

			}


			$scope.addToOpponents = function (player) {

				$scope.opponents.unshift(player);
				$scope.opponentToAdd = $scope.opponents[0];
				$scope.getOpponentDecks();

				$scope.toggleSearch();
			};



			// Start Game
			$scope.startGame = function () {

				var startGameReq = requestService.buildRequest(
					"StartGame",
					{
						deck: $scope.deckId,
						participants: $scope.participants
					}
				);
				
				console.log(startGameReq);

				$http(startGameReq).then(function (response) {
					$scope.result = response.data

					if (response.data.result == "Success") {
						$location.url('/register');
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