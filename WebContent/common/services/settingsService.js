ratorApp.factory('settingsService', function($http, requestService){
	
	var settings = {};
	
	return {	
		loadSettings: function(){
			
			var loadSettingsReq = requestService.buildRequest(
					"GetSettings",
					{ }
			);
			
			return $http(loadSettingsReq).then(function(response){
				
				this.settings = JSON.parse(response.data.settings);
				
				return this.settings;
			});
		},	
		getSettings: function(){
			
			return this.settings;
		},
		dismissHelp: function(help_name){
			
			console.log('dismissing ' + help_name);
			
			var dismissHelpReq = requestService.buildRequest(
					"DismissHelp",
					{
						name: help_name
					}
			);
			
			$http(dismissHelpReq);
		}
	}
	
});