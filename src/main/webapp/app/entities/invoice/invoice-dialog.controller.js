(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceDialogController', InvoiceDialogController);

    InvoiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invoice', 'InvoiceItem'];

    function InvoiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Invoice, InvoiceItem) {
        var vm = this;

        vm.invoice = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.invoiceitems = InvoiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invoice.id !== null) {
                Invoice.update(vm.invoice, onSaveSuccess, onSaveError);
            } else {
                Invoice.save(vm.invoice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:invoiceUpdate', result);
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
