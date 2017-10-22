ratorApp.factory('settingsService', function($http, requestService){
	
	var settings = {};
	
	var loadSettings = function(){
		
		var loadSettingsReq = requestService.buildRequest(
				"GetSettings",
				{ }
		);
		
		return $http(loadSettingsReq).then(function(response){
			
			this.settings = JSON.parse(response.data.settings);
			
			return this.settings;
		});
	}
	
	return {	
		loadSettings: loadSettings,	
		getSettings: function(){
			
			return this.settings;
		},
		dismissHelp: function(help_id){
			
			var dismissHelpReq = requestService.buildRequest(
					"DismissHelp",
					{
						id: help_id
					}
			);
			
			$http(dismissHelpReq).then(function(response){
				
				loadSettings();
			});
		}
	}
	
});