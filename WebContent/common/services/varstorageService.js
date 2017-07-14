ratorApp.factory('varStorage', function() {
	 var current_deck = {};
	 var go_to = {};
	 var live_token = {};
	 var live_id = {};
	 
	 function setCurrentDeck(data) {
		 current_deck = data;
	 }
	 function getCurrentDeck() {
		 return current_deck;
	 }
	 function setGoTo(data) {
		 go_to = data;
	 }
	 function getGoTo() {
		 return go_to;
	 }
	 function setLiveToken(data) {
		 live_token = data;
	 }
	 function getLiveToken() {
		 return live_token;
	 }

	 return {
		 setCurrentDeck : setCurrentDeck,
		 getCurrentDeck : getCurrentDeck,
		 setGoTo : setGoTo,
		 getGoTo : getGoTo,
		 setLiveToken : setLiveToken,
		 getLiveToken : getLiveToken
	 }
});