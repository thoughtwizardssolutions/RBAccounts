(function() {
	'use strict';

	angular.module('rbaccountsApp')
	.directive("moveFocusNext", function() {
		return {
			restrict : "A",
			link : function($scope, element) {
				element.bind("keyup", function(e) {
					var n = $("input:text").length;
				    if (e.which == 13) 
				    { //Enter key
				      e.preventDefault(); //Skip default behavior of the enter key
				      var nextIndex = $('input:text').index(this) + 1;
				      if(nextIndex < n)
				        $('input:text')[nextIndex].focus();
				    }
				});
			}
		}
	});
})();

