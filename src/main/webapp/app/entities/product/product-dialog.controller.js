(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductDialogController', ProductDialogController);

    ProductDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Product'];

    function ProductDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Product) {
        var vm = this;

        vm.product = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.isEdit = false;
        
        setup();
        
        function setup() {
        	if(entity && entity.id) {
        		vm.isEdit = true;
        	}
        }

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.product.id !== null) {
                Product.update(vm.product, onSaveSuccess, onSaveError);
            } else {
                Product.save(vm.product, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationTime = false;
        vm.datePickerOpenStatus.modificationTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
