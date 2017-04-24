ratorApp.controller('addGameController', function ($scope, $http, $location, playerService, requestService, deckVarStorage) {

	playerService.getPlayer().then(function (data) {
		if (data.result == "Success") {

			$scope.player = JSON.parse(data.player);
			$scope.deckId = deckVarStorage.getCurrentDeck();

			$scope.selfAdded = false;
			$scope.addState = "Regular";
			$scope.opponentIsMinion = true;

			$scope.comment = "";
			$scope.draw = false;
			$scope.participants = [];''
			$scope.decks = [];

			$scope.newPlayer = { 'id': 0, 'name': "" };
			$scope.allPlayers = [];
			
			$scope.rating = [0,0,0,0];
			$scope.generalRating = function(){ return ($scope.rating.reduce(function(a, b) { return a + b; }, 0))/4; }

			$scope.newDeck = {
				'name': "",
				'format': "",
				'blackCards': 0,
				'whiteCards': 0,
				'redCards': 0,
				'greenCards': 0,
				'blueCards': 0,
				'colorlessCards': 0,
				'theme': ""
			}

			$scope.getDeck = function () {

				// Get deck
				var getDeckReq = requestService.buildRequest(
					"GetDeck",
					{ id: $scope.deckId }
				);

				$http(getDeckReq).then(function (response) {
					$scope.result = response.data;

					if (response.data.result == "Success") {
						$scope.result = 'Success';
						$scope.playerdeck = JSON.parse(response.data.deck);
					}
				},
					function () {
						$scope.result = 'Failure';
					});
			}


			// Get opponents
			var getOpponentsReq = requestService.buildRequest(
				"GetOpponents",
				{}
			);

			$http(getOpponentsReq).then(function (response) {
				$scope.result = response.data;

				if (response.data.result == "Success") {
					$scope.opponents = JSON.parse(response.data.opponents);
					$scope.opponentToAdd = $scope.opponents[0];
					if ($scope.opponentToAdd != undefined){
						$scope.getOpponentDecks();
					}					
				}
			},
				function () {
					$scope.result = 'Failure';
				});

			$scope.getDeck();
			
			$scope.opponentIsMinion = function(){
				if ($scope.opponentToAdd != undefined && $scope.opponentToAdd.labels != undefined){
				    for (var i = 0; i < $scope.opponentToAdd.labels.length; i++) {
				        if ($scope.opponentToAdd.labels[i] == "Minion") {
				            return true;
				        }
				    }
				    return false;
				}
			}

			// Get opponent decks
			$scope.getOpponentDecks = function () {
				
				if ($scope.opponentToAdd != undefined){
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
							'black': $scope.newDeck.blackCards 	? 1:0,
							'white': $scope.newDeck.whiteCards 	? 1:0,
							'red': $scope.newDeck.redCards 		? 1:0,
							'green': $scope.newDeck.greenCards 	? 1:0,
							'blue': $scope.newDeck.blueCards 	? 1:0,
							'colorless': $scope.newDeck.colorlessCards ? 1:0,
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


			// Add Self
			$scope.addSelf = function () {

				$scope.participants.push(
					{
						deckId: $scope.playerdeck.deckid,
						place: $scope.participants.length + 1,
						playerId: $scope.player.id,
						playerName: $scope.player.name,
						deckName: $scope.playerdeck.name,
						confirmed: true,
						comment: "",
						added: Date.now(),
						tags: []
					}
				);

				$scope.selfAdded = true;
			};

			// Add Participant
			$scope.addParticipant = function () {

				$scope.participants.push(
					{
						deckId: $scope.addDeck.id,
						place: $scope.participants.length + 1,
						playerId: $scope.opponentToAdd.id,
						playerName: $scope.opponentToAdd.name,
						deckName: $scope.addDeck.name,
						confirmed: false,
						comment: "",
						added: Date.now(),
						tags: []
					}
				);

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


			$scope.getAllPlayers = function () {

				var getAllPlayersReq = requestService.buildRequest(
					"GetAllPlayers",
					{ }
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
			
			$scope.addToOpponents = function (player) {
				
				$scope.opponents.unshift(player);
				$scope.opponentToAdd = $scope.opponents[0];
				$scope.getOpponentDecks();

				$scope.toggleSearch();
			};

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
					if($scope.allPlayers.length < 1){
						$scope.getAllPlayers();
					}
					$scope.addState = "Search";
				} else {
					$scope.addState = "Regular";
				}
			};
			
			
			//Rating
			$scope.rate = function(parameter, value){				
				
				switch(parameter) {
			    case 'general':
			        $scope.rating = [value, value, value, value];
			        break;
			    default:
			    	$scope.rating = [value, value, value, value];
				}				

				console.log(parameter, value, $scope.generalRating());
			}


			// Add Tag
			$scope.addTag = function (participant) {

				if (participant.tag.positive != undefined && participant.tag.positive.length > 0) {
					participant.tags.push(
						{
							polarity: 1,
							tag: participant.tag.positive
						}
					);

					participant.tag.positive = "";
				}

				if (participant.tag.negative != undefined && participant.tag.negative.length > 0) {
					participant.tags.push(
						{
							polarity: -1,
							tag: participant.tag.negative
						}
					);

					participant.tag.negative = "";
				}
			};


			// Add game
			$scope.addGame = function () {
				$scope.result = "Waiting for response";

				var updatePlayerEntry = function (participant) {
					if (participant.deckId == $scope.deckId) {
						participant.comment = $scope.comment;
					}
				}

				$scope.participants.forEach(updatePlayerEntry);

				var addGameReq = requestService.buildRequest(
					"AddGame",
					{
						participants: $scope.participants,
						draw: $scope.draw
					}
				);

				$http(addGameReq).then(function (response) {
					$scope.result = response.data

					if (response.data.result == "Success") {
						deckVarStorage.setCurrentDeck($scope.deckId);
						$location.url('/deck');
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