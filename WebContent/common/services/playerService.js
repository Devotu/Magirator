ratorApp.factory('playerService', function($http){
	
	return {		
		getPlayer: function(){			
			return $http.post('/Magirator/GetPlayer').then(function(response){
				return response.data;
			});
		}
	}
});