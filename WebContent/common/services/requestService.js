ratorApp.factory('requestService', function(){
	
	return {
		buildRequest : function(endpoint, data) {
		return {
			method: 'POST',
			//url: '/Magirator/' + endpoint, //Debug
			url: '/' + endpoint, //Proper
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		}
	}
});