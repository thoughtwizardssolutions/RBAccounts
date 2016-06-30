(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductTransactionDeleteController',ProductTransactionDeleteController);

    ProductTransactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductTransaction'];

    function ProductTransactionDeleteController($uibModalInstance, entity, ProductTransaction) {
        var vm = this;

        vm.productTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
