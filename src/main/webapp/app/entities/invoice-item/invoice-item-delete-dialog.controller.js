(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceItemDeleteController',InvoiceItemDeleteController);

    InvoiceItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'InvoiceItem'];

    function InvoiceItemDeleteController($uibModalInstance, entity, InvoiceItem) {
        var vm = this;

        vm.invoiceItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InvoiceItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
