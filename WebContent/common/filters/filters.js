ratorApp.filter('postiveTags', function(){
	return function(tags){
		var positive = [];
		for (var i = 0; i < tags.length; i++) {
		      var tag = tags[i];
		      if (tag.polarity == 1) {
		    	  positive.push(tag.tag);
		      }
		}
		return positive;
	};
});

ratorApp.filter('negativeTags', function(){
	return function(tags){
		var negative = [];
		for (var i = 0; i < tags.length; i++) {
		      var tag = tags[i];
		      if (tag.polarity == -1) {
		    	  negative.push(tag.tag);
		      }
		}
		return negative;
	};
});