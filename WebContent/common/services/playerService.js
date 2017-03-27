ratorApp.factory('playerService', function($http){
	
	return {		
		getPlayer: function(){			
			//return $http.post('/Magirator/GetPlayer').then(function(response){ //Debug
			return $http.post('/GetPlayer').then(function(response){ //Proper
				return response.data;
			});
		}
	}
});