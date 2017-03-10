ratorApp.factory('requestService', function(){
	
	return {
		buildRequest : function(endpoint, data) {
		return {
			method: 'POST',
			url: '/Magirator/' + endpoint,
			//url: '/' + endpoint,
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		}
	}
});