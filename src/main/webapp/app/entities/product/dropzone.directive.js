(function() {
	'use strict';

	angular.module('rbaccountsApp').directive('dropzone', dropzone);

	dropzone.$inject = [ '$cookies' ];

	function dropzone($cookies) {
		return {
			restrict : 'C',
			link : function(scope, element, attrs) {

				var config = {
					url : 'api/products/upload',
					maxFilesize : 1000,
					paramName : "uploadfile",
					maxThumbnailFilesize : 10,
					parallelUploads : 1,
					autoProcessQueue : false
				};

				var eventHandlers = {
					'addedfile' : function(file) {
						console.log('added file....');
						scope.file = file;
						if (this.files[1] != null) {
							this.removeFile(this.files[0]);
						}
						scope.$apply(function() {
							scope.vm.fileAdded = true;
							scope.vm.file = file;
						});
					},
					'sending' : function(file, xhr, formData) {
						console.log('sending ....');
						xhr.setRequestHeader('X-CSRF-TOKEN', $cookies.get('CSRF-TOKEN'));
					},

					'success' : function(file, response) {
						console.log('file successfully uploaded to server....');
					}

				};

				dropzone = new Dropzone(element[0], config);

				angular.forEach(eventHandlers, function(handler, event) {
					dropzone.on(event, handler);
				});

				scope.processUpload = function() {
					dropzone.processQueue();
				};

				scope.resetUpload = function() {
					dropzone.removeAllFiles();
				};
			}
		}
	}
})();
