ratorApp.filter('postiveTags', function(){
	return function(tag){
		if (tag.polarity == 1){
			return tag.tag;
		}
	};
});

ratorApp.filter('negativeTags', function(){
	return function(tag){
		if (tag.polarity == -1){
			return tag.tag;
		}
	};
});