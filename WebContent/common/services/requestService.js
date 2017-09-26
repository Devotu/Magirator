ratorApp.factory('requestService', function(){
		
	//var base_path = "/";
	var base_path = "/Magirator/"; //Debug
	
	return {
		buildRequest : function(endpoint, data) {
		return {
			method: 'POST',
			url: base_path + endpoint,
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		},
		buildPost : function(endpoint, data) {
			return {
				method: 'POST',
				url: base_path + endpoint,
				headers: {
				   'Content-Type': 'application/json'
				}, 
				data: data
				}
			},
		buildGet : function(endpoint, data) {
		return {
			method: 'GET',
			url: base_path + endpoint,
			headers: {
			   'Content-Type': 'application/json'
			}, 
			data: data
			}
		}
	}
});