ratorApp.factory('playerService', function($http, requestService){
	
	return {		
		getPlayer: function(){			
			var getPlayerReq = requestService.buildRequest(
					"GetPlayer",
					{ }
					);					
					
			return	$http(getPlayerReq).then(function(response){					

				return response.data;
			});
		}
	}
});