(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceItemDialogController', InvoiceItemDialogController);

    InvoiceItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InvoiceItem', 'Imei', 'Invoice'];

    function InvoiceItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InvoiceItem, Imei, Invoice) {
        var vm = this;

        vm.invoiceItem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.imeis = Imei.query();
        vm.invoices = Invoice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invoiceItem.id !== null) {
                InvoiceItem.update(vm.invoiceItem, onSaveSuccess, onSaveError);
            } else {
                InvoiceItem.save(vm.invoiceItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:invoiceItemUpdate', result);
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
