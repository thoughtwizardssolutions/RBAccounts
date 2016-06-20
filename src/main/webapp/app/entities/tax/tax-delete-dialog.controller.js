(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('TaxDeleteController',TaxDeleteController);

    TaxDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tax'];

    function TaxDeleteController($uibModalInstance, entity, Tax) {
        var vm = this;

        vm.tax = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tax.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
