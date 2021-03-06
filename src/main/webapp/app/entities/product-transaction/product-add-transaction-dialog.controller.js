(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductAddTransactionDialogController', ProductAddTransactionDialogController);

    ProductAddTransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductTransaction'];

    function ProductAddTransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductTransaction) {
        var vm = this;

        vm.product = entity;
        vm.productTransaction = {};
        vm.productTransaction.newStock = false;
        vm.productTransaction.returnStock = false;
        vm.stockType = null;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.selectStockType = selectStockType;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.productTransaction.product = vm.product;
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
        
        function selectStockType(stockType) {
        	console.log(stockType);
        	switch(stockType) {
        	case 'newStock' : {
        		vm.productTransaction.newStock = true;
        		vm.productTransaction.returnStock = false;
        		break;
        	}
        	case 'returnStock' : {
        		vm.productTransaction.returnStock = true;
        		vm.productTransaction.newStock = false;
        		break;
        	}
        	}
        }
    }
})();
