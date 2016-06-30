(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductTransactionDialogController', ProductTransactionDialogController);

    ProductTransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductTransaction'];

    function ProductTransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductTransaction) {
        var vm = this;

        vm.productTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productTransaction.id !== null) {
                ProductTransaction.update(vm.productTransaction, onSaveSuccess, onSaveError);
            } else {
                ProductTransaction.save(vm.productTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:productTransactionUpdate', result);
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
