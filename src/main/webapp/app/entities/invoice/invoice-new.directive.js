(function() {
	'use strict';

	angular.module('rbaccountsApp')
	.directive("moveNextOnMaxlength", function() {
		return {
			restrict : "A",
			link : function($scope, element) {
				element.bind("keyup", function(e) {
					if (e.which == 13) {
						var $nextElement = element.next();
						if ($nextElement.length) {
							$nextElement[0].focus();
						} else {
							$('#field_imei1').focus();
						}
					}
					e.preventDefault();
				});
			}
		}
	});
})();