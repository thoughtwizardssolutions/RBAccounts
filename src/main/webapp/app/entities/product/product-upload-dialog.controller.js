(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductUploadDialogController', ProductUploadDialogController);

    ProductUploadDialogController.$inject = ['$scope', '$uibModalInstance', '$state'];

    function ProductUploadDialogController ($scope, $uibModalInstance, $state) {
        var vm = this;
        vm.clear = clear;
        vm.close = close;
        vm.uploadFile = uploadFile;
        vm.reset = reset;
        vm.fileAdded = false;
        vm.file = null;
        
        function uploadFile() {
        	vm.isSaving = true;
            $scope.processUpload();
            vm.isSaving = false;
        }

        function reset() {
            $scope.resetUpload();
        }
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        
        function close () {
            $uibModalInstance.dismiss('close');
            $state.go('product', null, { reload: true });
        }
    }
})();


