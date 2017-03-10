ratorApp.factory('deckVarStorage', function() {
	 var currentDeck = {}
	 var goTo = {}
	 
	 function setCurrentDeck(data) {
		 currentDeck = data;
	 }
	 function getCurrentDeck() {
		 return currentDeck;
	 }
	 function setGoTo(data) {
		 goTo = data;
	 }
	 function getGoTo() {
		 return goTo;
	 }

	 return {
		 setCurrentDeck : setCurrentDeck,
		 getCurrentDeck : getCurrentDeck,
		 setGoTo : setGoTo,
		 getGoTo : getGoTo
	 }
});